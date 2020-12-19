package com.app.model.dto;

import com.app.model.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionFilterDto {
    private Long categoryId;
    private Level level;
}
