package com.example.Instagram.entity;



import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "text", nullable = false)
    private String message;



    @Column(updatable = false)
    private LocalDateTime dateOfCreation;
    @PrePersist
    protected void onCreate(){
        this.dateOfCreation = LocalDateTime.now();
    }


    public Comment() {
    }
}
