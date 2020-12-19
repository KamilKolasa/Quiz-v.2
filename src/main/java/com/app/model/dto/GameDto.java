package com.app.model.dto;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDto {

    private Long id;
    private QuestionDto question;
    private QuizDto quiz;
}
