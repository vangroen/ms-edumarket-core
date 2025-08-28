package com.ctcse.ms.edumarket.core.agent.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAgentRequest {

    @NotNull(message = "El campo 'id de la persona' no puede ser nulo.")
    private Long idPerson;
}