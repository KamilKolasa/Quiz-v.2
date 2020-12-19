package com.app.model.dto;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDto {

    private Long id;
    private String text;
    private Boolean correct;
    private Long questionId;
}
