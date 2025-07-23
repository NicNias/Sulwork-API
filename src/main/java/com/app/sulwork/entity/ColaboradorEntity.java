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
    private UUID id;

    @Column(length = 250)
    private String nome;

    @Column(length = 11, unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    private List<String> itens;

    private boolean entregue;
}
