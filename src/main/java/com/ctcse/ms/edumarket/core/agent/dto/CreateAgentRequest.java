package com.ctcse.ms.edumarket.core.agent.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import lombok.Data;

@Data
public class CreateAgentRequest {

    @NotBlankWithMessage(fieldName = "id de la persona")
    @SizeWithMessage(fieldName = "id de la persona", min = 1, max = 100)
    private Long idPerson;
}
