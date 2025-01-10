package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private String roleId;
    private String name;
    @JsonIgnore
    private List<UserDto> users;
}
