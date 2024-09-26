package com.teamtable.teamtable_api.dto;

import com.teamtable.teamtable_api.model.Account;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {

    private String firstName;
    private String lastName;
    private String email;
    private String picture;

    public static AccountInfoDto convertAccountToAccountInfoDto(Account account) {
        return AccountInfoDto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .picture(account.getPictureUrl())
                .build();
    }
}