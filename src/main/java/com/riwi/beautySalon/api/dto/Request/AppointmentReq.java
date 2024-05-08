package com.riwi.beautySalon.api.dto.Request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentReq {
  @NotBlank(message = "date and time is required")
  private LocalDateTime dateTime;
  //Se debe validar desde el servicio que la cita no sea menos que la de ayer
  @Min(value = 10, message = "the duration can not be downer than 10")
  @Max(value = 720, message = "The duration can not be lower than 12 hours")
  private Integer duration;
  private String comments;
  @NotNull(message = "id customer is required") //not null es para numeros y notBlanck para string
  private Long customerId;
  @NotNull(message = "id service is required")
  private Long serviceId;
  @NotNull(message = "id employee is required")
  private Long employeeId;
}
