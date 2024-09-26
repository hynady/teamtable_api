package com.teamtable.teamtable_api.controller;

import com.teamtable.teamtable_api.dto.IdTokenRequestDto;
import com.teamtable.teamtable_api.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/oauth")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody, HttpServletResponse response) {
        String authToken = accountService.loginOAuthGoogle(requestBody.getId_token(), requestBody.getAccess_token(), requestBody.getRefresh_token());

        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                .httpOnly(false)// có the truy cap tu frontend
                .maxAge(28 * 24 * 3600)
                .path("/")
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}
