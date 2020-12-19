package com.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsWithAnswersDto {
    private List<QuestionDto> questions;
    private List<Integer> answers;
    private QuizDto quiz;
}
