package com.teamtable.teamtable_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    //fix create 2 user same mail
    @Column(unique = true)
    private String email;

    private String pictureUrl;

    private String roles;

    @Column(columnDefinition = "TEXT")
    private String idToken;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    public Account( String firstName, String lastName, String email, String pictureUrl, String idToken, String accessToken, String refreshToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Account() {

    }
}

