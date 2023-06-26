package com.example.dentistClinic.dto;


import com.example.dentistClinic.domain.UserAdmin;
import org.springframework.stereotype.Component;

@Component

public class UserAdminDTO extends AbstractDTO {



    public UserAdmin DTOToUser(UserAdminDTO userDTO) {
        UserAdmin user = new UserAdmin();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());

        return user;
    }

    public UserAdminDTO userToDTO(UserAdmin user) {
        UserAdminDTO userDTO = new UserAdminDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }

}
