package com.teamtable.teamtable_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdTokenRequestDto {

    private String id_token;
    private String refresh_token;
    private String access_token;
}
