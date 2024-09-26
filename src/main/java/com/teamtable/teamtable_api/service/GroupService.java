package com.teamtable.teamtable_api.service;

import com.teamtable.teamtable_api.model.Account;
import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.model.MemberCard;
import com.teamtable.teamtable_api.repository.GroupRepository;
import com.teamtable.teamtable_api.repository.MemberCardRepository;
import com.teamtable.teamtable_api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberCardRepository memberCardRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Group createGroupWithOwner(String groupName, Account account) {
        // Tạo nhóm mới
        Group group = new Group();
        group.setName(groupName);

        // Tạo card member cho account với vai trò OWNER
        MemberCard memberCard = new MemberCard();
        memberCard.setAccount(account);
        memberCard.setGroup(group);
        memberCard.setRole(MemberCard.Role.OWNER);

        // Lưu nhóm và card member
        group.getMemberCards().add(memberCard);
        groupRepository.save(group);
        memberCardRepository.save(memberCard);

        return group;
    }

    @Transactional
    public boolean addUserToGroup(Long groupId, Long userId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Account> accountOptional = accountRepository.findById(userId);

        if (groupOptional.isPresent() && accountOptional.isPresent()) {
            Group group = groupOptional.get();
            Account account = accountOptional.get();

            // Kiểm tra xem MemberCard đã tồn tại chưa
            MemberCard existingMemberCard = memberCardRepository.findByAccountAndGroup(account, group);
            if (existingMemberCard != null) {
                // Người dùng đã tồn tại trong nhóm
                return false;
            }

            MemberCard memberCard = new MemberCard();
            memberCard.setAccount(account);
            memberCard.setGroup(group);
            memberCard.setRole(MemberCard.Role.MEMBER);

            group.getMemberCards().add(memberCard);
            groupRepository.save(group);
            memberCardRepository.save(memberCard);

            return true;
        }

        return false;
    }

    @Transactional
    public boolean removeUserFromGroup(Long groupId, Long userId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Account> accountOptional = accountRepository.findById(userId);

        if (groupOptional.isPresent() && accountOptional.isPresent()) {
            Group group = groupOptional.get();
            Account account = accountOptional.get();

            MemberCard memberCard = memberCardRepository.findByAccountAndGroup(account, group);
            if (memberCard != null && memberCard.getRole() != MemberCard.Role.OWNER) {
                group.getMemberCards().remove(memberCard);
                memberCardRepository.delete(memberCard);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Set<Account> getAccountsInGroup(Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            return group.getMemberCards().stream()
                    .map(MemberCard::getAccount)
                    .collect(Collectors.toSet());
        } else {
            return null;
        }
    }
}
