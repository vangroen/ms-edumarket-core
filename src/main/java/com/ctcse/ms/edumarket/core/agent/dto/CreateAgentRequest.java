package com.ctcse.ms.edumarket.core.agent.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAgentRequest {

    @NotNull(message = "El campo 'id de la persona' no puede ser nulo.")
    @Min(value = 1, message = "El valor del 'id de la persona' debe ser como mínimo 1.")
    @Max(value = 100, message = "El valor del 'id de la persona' debe ser como máximo 100.")
    private Long idPerson;
}
