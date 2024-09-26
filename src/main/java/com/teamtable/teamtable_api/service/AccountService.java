package com.teamtable.teamtable_api.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.teamtable.teamtable_api.config.JWTUtils;
import com.teamtable.teamtable_api.model.Account;
import com.teamtable.teamtable_api.model.Group;
import com.teamtable.teamtable_api.model.MemberCard;
import com.teamtable.teamtable_api.repository.AccountRepository;
import com.teamtable.teamtable_api.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;

    public AccountService(@Value("${app.googleClientId}") String clientId, AccountRepository accountRepository, GroupRepository groupRepository,
                          JWTUtils jwtUtils) {
        this.accountRepository = accountRepository;
        this.groupRepository = groupRepository;
        this.jwtUtils = jwtUtils;
        // Thêm dependency
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Transactional
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public String loginOAuthGoogle(String id_token, String access_token, String refresh_token){
        // Xác thực ID Token và lấy thông tin tài khoản
        Account account = verifyIDToken(id_token, access_token, refresh_token);

        if (account == null) {
            throw new IllegalArgumentException();
        }

        // Lưu hoặc cập nhật tài khoản
        account = createOrUpdateUser(account);

        // Lưu Access Token vào cơ sở dữ liệu
        accountRepository.save(account);

        // Tạo và trả về JWT
        return jwtUtils.createToken(account, false);
    }


    @Transactional
    public Account createOrUpdateUser(Account account) {
        try {
            // Kiểm tra sự tồn tại của người dùng trước
            Account existingAccount = accountRepository.findByEmail(account.getEmail()).orElse(null);

            if (existingAccount == null) {
                // Nếu người dùng không tồn tại, lưu mới
                account.setRoles("ROLE_USER");
                accountRepository.save(account);
                return account;
            } else {
                // Nếu người dùng đã tồn tại, cập nhật thông tin
                existingAccount.setPictureUrl(account.getPictureUrl());
                accountRepository.save(existingAccount);
                return existingAccount;
            }
        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi trùng lặp nếu có
            // Kiểm tra lại sự tồn tại của người dùng
            Account existingAccount = accountRepository.findByEmail(account.getEmail()).orElse(null);
            if (existingAccount != null) {
                System.out.println("catch Dupli"+ existingAccount.getEmail());
                return existingAccount; // Trả về tài khoản đã tồn tại
            }
            throw new RuntimeException("Unexpected error while creating/updating user", e);
        }
    }

    private Account verifyIDToken(String id_token, String access_token, String refresh_token) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(id_token);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();

            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            return new Account(firstName, lastName, email, pictureUrl, id_token, access_token, refresh_token);

        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }

    @Transactional
    public Set<Group> getListGroups(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return groupRepository.findByMemberCards_Account(account);
        }
        return null;
    }

//    public void sendOtp(String email) throws MessagingException {
//        generatedOtp = String.valueOf(new Random().nextInt(999999));
//        // Gửi OTP qua email (cần implement phương thức gửi email)
//        // sendEmail(email, generatedOtp);
//    }
//
//    public boolean verifyOtp(String otp) {
//        return generatedOtp != null && generatedOtp.equals(otp);
//    }

    public Account updateUserInfo(Long userId, String firstName, String lastName) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account != null) {
            account.setFirstName(firstName);
            account.setLastName(lastName);
            return accountRepository.save(account);
        }
        return null;
    }

}
