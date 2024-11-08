package com.MTBBE.MTB.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "id_sequence")
public class IdSequence {

    @Id
    private String name;

    @Column(nullable = false)
    private Long currentId;
}
