package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Builder
    public User(String id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }
}
