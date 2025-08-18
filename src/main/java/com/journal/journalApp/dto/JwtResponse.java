package com.journal.journalApp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    @NotEmpty
    private String userName;
    @NotEmpty
    private  String password;

}
