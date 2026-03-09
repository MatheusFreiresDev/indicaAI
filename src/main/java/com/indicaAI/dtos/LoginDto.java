package com.indicaAI.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginDto(@NotNull @Email String email, String password) {
}
