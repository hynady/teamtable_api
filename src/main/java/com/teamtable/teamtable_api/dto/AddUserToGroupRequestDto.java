package com.teamtable.teamtable_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserToGroupRequestDto {
    private Long groupId;
    private Long userId;
}