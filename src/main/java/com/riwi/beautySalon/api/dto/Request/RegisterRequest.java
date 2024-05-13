package com.riwi.beautySalon.api.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 150, message = "username must be between 3 and 150 characteres")
  private String username;
  @NotBlank(message = "The password is required")
  @Size(min = 6, max = 150, message = "password must be between 6 and 150 characteres")
  //@Pattern(regexp = "Expresión regular", message = "La contraseña debe tener ....")
  private String password;
}
