package com.app.sulwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "colaborador")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ColaboradorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(length = 250)
    private String nome;

    @Column(length = 11, unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    private List<String> itens;

    @Column(nullable = true)
    private boolean entregue;
}
