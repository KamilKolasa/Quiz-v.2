package com.app.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Quiz {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime date;
    private Integer result;
    private Integer goodAnswer;

    @ManyToOne/*(cascade = CascadeType.PERSIST)*/
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quiz")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Game> games = new HashSet<>();
}
