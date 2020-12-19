package com.app.model.dto;

import com.app.model.Level;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

    private Long id;
    private String text;
    private CategoryDto category;
    private Level level;
    private List<AnswerDto> answers;
    private Integer goodAnswer;
    private MultipartFile multipartFile;
    private String filename;
}
