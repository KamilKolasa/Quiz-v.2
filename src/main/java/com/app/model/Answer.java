package com.app.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private boolean correct;

    @ManyToOne/*(cascade = CascadeType.PERSIST)*/
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Question question;
}
