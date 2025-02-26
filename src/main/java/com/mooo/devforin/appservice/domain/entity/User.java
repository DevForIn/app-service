package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column
    private String id;

    @Column
    private String password;

    @Column
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();


    @Builder
    public User(String id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }
}
