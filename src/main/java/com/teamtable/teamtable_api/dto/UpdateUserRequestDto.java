package com.teamtable.teamtable_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String otp; // OTP để xác thực
}