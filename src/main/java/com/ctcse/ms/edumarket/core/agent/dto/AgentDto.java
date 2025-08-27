package com.ctcse.ms.edumarket.core.agent.dto;

import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import lombok.Data;

@Data
public class AgentDto {

    private Long id;
    private PersonDto person;
}
