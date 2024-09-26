package com.teamtable.teamtable_api.dto;

import com.teamtable.teamtable_api.model.Account;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListAccountInfoDto {
    private Set<Long> accountIds;

    public static ListAccountInfoDto convertListAccountToListAccountInfoDto(Set<Account> listAccount) {
        return ListAccountInfoDto.builder()
                .accountIds(listAccount.stream().map(Account::getId).collect(Collectors.toSet()))
                .build();
    }
}