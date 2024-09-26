package com.teamtable.teamtable_api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member_card")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MemberCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        OWNER,
        ADMIN,
        MEMBER
    }

    public MemberCard(String cardNumber, Account account, Group group, Role role) {
        this.cardNumber = cardNumber;
        this.account = account;
        this.group = group;
        this.role = role;
    }

    public MemberCard() {}

    @PreRemove
    public void checkIfOwnerCanBeRemoved() {
        if (this.role == Role.OWNER && this.group != null && this.group.getMemberCards().size() > 1) {
            throw new IllegalStateException("Owner cannot be removed from the group unless the group is being deleted.");
        }
    }
}
