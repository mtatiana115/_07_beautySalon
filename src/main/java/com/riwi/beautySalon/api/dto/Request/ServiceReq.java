package com.riwi.beautySalon.api.dto.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReq {
  @NotNull(message = "service name is required")
  @NotBlank(message = "service name is required")
  private String name;
  private String description;
  @NotNull(message = "the price is required")
  @DecimalMin(value = "0.01", message = "the value of the services must be lower than zero")
  private BigDecimal price;
}
