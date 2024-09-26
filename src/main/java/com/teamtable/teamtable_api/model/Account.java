package com.teamtable.teamtable_api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "account")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberCard> memberCards = new HashSet<>();

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

