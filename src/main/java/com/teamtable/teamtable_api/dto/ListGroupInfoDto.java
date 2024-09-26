package com.teamtable.teamtable_api.dto;

import com.teamtable.teamtable_api.model.Group;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListGroupInfoDto {
    private Set<GroupInfo> groups;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupInfo {
        private Long id;
        private String name;
        private String picture;
    }

    public static ListGroupInfoDto convertListGroupToListGroupInfoDto(Set<Group> listGroup) {
        Set<GroupInfo> groupInfos = listGroup.stream()
                .map(group -> GroupInfo.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .picture(group.getPicture())
                        .build())
                .collect(Collectors.toSet());

        return ListGroupInfoDto.builder()
                .groups(groupInfos)
                .build();
    }
}