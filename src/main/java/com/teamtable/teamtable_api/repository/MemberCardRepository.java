package com.teamtable.teamtable_api.repository;

import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.model.MemberCard;
import com.teamtable.teamtable_api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCardRepository extends JpaRepository<MemberCard, Long> {
    MemberCard findByAccountAndGroup(Account account, Group group);
}
