package hu.cubix.balage.logisticsapplication.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank String username, @NotBlank String password) {
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}