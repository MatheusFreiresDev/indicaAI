package com.indicaAI.dtos;

import lombok.Builder;
import org.springframework.web.ErrorResponse;
@Builder
public record MovieDto(
                        String title,
                        String poster_path,
                        String overview) {
}
