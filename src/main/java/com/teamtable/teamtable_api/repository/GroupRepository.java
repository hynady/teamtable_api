package com.teamtable.teamtable_api.repository;

import com.teamtable.teamtable_api.model.Account;
import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.model.MemberCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Set<Group> findByMemberCards(Set<MemberCard> memberCards);
    Set<Group> findByMemberCards_Account(Account account);
}
