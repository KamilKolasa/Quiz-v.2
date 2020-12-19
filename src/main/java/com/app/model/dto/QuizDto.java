package com.app.model.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDto {

    private Long id;
    private UserDto user;
    private LocalDateTime date;
    private Integer result;
    private Integer goodAnswer;
}
