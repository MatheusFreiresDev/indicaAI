package com.indicaAI.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indicaAI.dtos.RecommendationResponse;
import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.exception.InvalidDataException;
import com.indicaAI.exception.NotFoundException;
import com.indicaAI.model.Review;
import com.indicaAI.model.User;
import com.indicaAI.repository.ReviewRepository;
import com.indicaAI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TmbdService tmbdService;
    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .build();

    public List<RecommendationResponse> recommend(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        List<Review> reviews = reviewRepository.findAllByUserMovieUserId(user.getId());

        if (reviews.isEmpty()) {
            throw new InvalidDataException("Você ainda não tem reviews. Assista e avalie alguns filmes primeiro!");
        }

        String prompt = buildPrompt(reviews);
        String iaResponse = callOpenAiApi(prompt);

        List<Map<String, String>> iaRecommendations = parseIaResponse(iaResponse);

        return iaRecommendations.stream()
                .map(rec -> {
                    String title = rec.get("title");
                    String reason = rec.get("reason");

                    List<TmdbMovieDto> results = tmbdService.searchFilm(title);
                    if (results.isEmpty()) return null;

                    TmdbMovieDto movie = results.get(0);
                    return new RecommendationResponse(
                            movie.id(),
                            movie.title(),
                            movie.poster_path(),
                            reason
                    );
                })
                .filter(r -> r != null)
                .toList();
    }

    private String buildPrompt(List<Review> reviews) {
        StringBuilder sb = new StringBuilder();

        sb.append("Baseado nos filmes que esse usuário assistiu e avaliou, recomende exatamente 3 filmes que ele provavelmente vai curtir.\n");
        sb.append("IMPORTANTE:\n");
        sb.append("- NÃO recomende filmes que já estão na lista abaixo.\n");
        sb.append("- NÃO repita nenhum dos títulos fornecidos.\n");
        sb.append("- Evite sugestões óbvias ou muito populares se já estiverem na lista.\n");
        sb.append("- Priorize recomendações variadas, mas ainda coerentes com o gosto do usuário.\n\n");

        sb.append("Filmes assistidos:\n");

        for (Review review : reviews) {
            String title = review.getUserMovie().getMovie().getTitle();
            Integer nota = review.getNota();
            String descricao = review.getDescricao();

            sb.append(String.format("- %s | Nota: %d/10", title, nota));

            if (descricao != null && !descricao.isBlank()) {
                sb.append(String.format(" | Comentário: \"%s\"", descricao));
            }

            sb.append("\n");
        }

        sb.append("\nResponda APENAS com um JSON válido, sem texto fora do JSON, sem markdown, sem explicações.\n");
        sb.append("Use EXATAMENTE este formato:\n");
        sb.append("{\"recommendations\":[");
        sb.append("{\"title\":\"Nome do Filme\",\"reason\":\"Motivo personalizado\"},");
        sb.append("{\"title\":\"Nome do Filme\",\"reason\":\"Motivo personalizado\"},");
        sb.append("{\"title\":\"Nome do Filme\",\"reason\":\"Motivo personalizado\"}");
        sb.append("]}");

        return sb.toString();
    }
    private String callOpenAiApi(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "max_tokens", 500
        );

        Map response = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> parseIaResponse(String json) {
        try {
            String clean = json.trim().replaceAll("```json", "").replaceAll("```", "").trim();
            Map<String, Object> parsed = objectMapper.readValue(clean, Map.class);
            return (List<Map<String, String>>) parsed.get("recommendations");
        } catch (Exception e) {
            throw new InvalidDataException("Erro ao interpretar resposta da IA: " + e.getMessage());
        }
    }
}