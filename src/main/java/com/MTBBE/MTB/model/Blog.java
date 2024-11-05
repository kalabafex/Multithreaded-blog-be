package com.MTBBE.MTB.model;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Change from 'long' to 'Long' to support nullability before assignment

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
    }
}


