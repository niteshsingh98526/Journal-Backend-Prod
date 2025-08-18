package com.journal.journalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty
    //@Schema(name = "The user's username")
    private String userName;
    private String email;
    @NotEmpty
    private String password;
}
