package com.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private String filename;
    private Level level;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Game> games = new HashSet<>();
}
