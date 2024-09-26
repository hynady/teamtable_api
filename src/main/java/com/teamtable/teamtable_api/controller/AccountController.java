package com.teamtable.teamtable_api.controller;

import com.teamtable.teamtable_api.dto.UpdateUserRequestDto;
import com.teamtable.teamtable_api.model.Account;
import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

import static com.teamtable.teamtable_api.dto.AccountInfoDto.convertAccountToAccountInfoDto;
import static com.teamtable.teamtable_api.dto.ListGroupInfoDto.convertListGroupToListGroupInfoDto;

@RestController
@RequestMapping("/v1/user")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/info")
    public ResponseEntity getUserInfo(Principal principal) {
        Account account = accountService.getAccount(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertAccountToAccountInfoDto(account));
    }

    @GetMapping("/card")
    public ResponseEntity getCard(Principal principal) {
        Set<Group> listGroups = accountService.getListGroups(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertListGroupToListGroupInfoDto(listGroups));
    }

//    @PostMapping("/send-otp")
//    public ResponseEntity<Void> sendOtp(@RequestParam String email) {
//        try {
//            accountService.sendOtp(email);
//            return ResponseEntity.ok().build();
//        } catch (MessagingException e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

//    @PutMapping("/update")
//    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto requestDto, @RequestParam Long userId) {
//        if (accountService.verifyOtp(requestDto.getOtp())) {
//            accountService.updateUserInfo(userId, requestDto.getFirstName(), requestDto.getLastName());
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(400).body(null);
//    }
}
