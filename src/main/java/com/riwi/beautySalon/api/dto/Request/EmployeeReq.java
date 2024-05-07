package com.riwi.beautySalon.api.dto.Request;

import com.riwi.beautySalon.utils.enums.RoleEmployee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReq {
  @NotBlank(message = "The name is required")
  private String firstName;
  @NotBlank(message = "The lastname is required")
  private String lastName;
  @Size(
    min = 10,
    max = 20,
    message = "The phone number must be between 10 and 20 characteres"
    )
  private String phone;
  @Email(message = "invalid email")
  @Size(
    min = 5,
    max = 100,
    message = "the email must be between 5 and 100 characteres")
  private String email;
@NotBlank(message = "rol is required")
  private RoleEmployee role;
}
