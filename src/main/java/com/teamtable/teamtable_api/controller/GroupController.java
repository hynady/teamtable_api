package com.teamtable.teamtable_api.controller;

import com.teamtable.teamtable_api.dto.GroupRequestDto;
import com.teamtable.teamtable_api.dto.AddUserToGroupRequestDto;
import com.teamtable.teamtable_api.model.Account;
import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.service.AccountService;
import com.teamtable.teamtable_api.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

import com.teamtable.teamtable_api.dto.GroupIdRequestDto;
import com.teamtable.teamtable_api.dto.ListAccountInfoDto;

@RestController
@RequestMapping("/v1/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/new")
    public Group createNewGroup(@RequestBody GroupRequestDto groupRequestDto, Principal principal) {
        Account account = accountService.getAccount(Long.valueOf(principal.getName()));
        return groupService.createGroupWithOwner(groupRequestDto.getGroupName(), account);
    }

    @PutMapping("/addUser")
    public ResponseEntity<String> addUserToGroup(@RequestBody AddUserToGroupRequestDto requestDto) {
        boolean success = groupService.addUserToGroup(requestDto.getGroupId(), requestDto.getUserId());
        if (success) {
            return ResponseEntity.ok("User added to group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user to group");
        }
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<String> removeUserFromGroup(@RequestBody AddUserToGroupRequestDto requestDto) {
        boolean success = groupService.removeUserFromGroup(requestDto.getGroupId(), requestDto.getUserId());
        if (success) {
            return ResponseEntity.ok("User removed from group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove user from group");
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<ListAccountInfoDto> getAccountsInGroup(@RequestBody GroupIdRequestDto requestDto) {
        Set<Account> accounts = groupService.getAccountsInGroup(requestDto.getGroupId());
        if (accounts != null) {
            ListAccountInfoDto accountInfoDto = ListAccountInfoDto.convertListAccountToListAccountInfoDto(accounts);
            return ResponseEntity.ok(accountInfoDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
