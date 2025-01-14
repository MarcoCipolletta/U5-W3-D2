package it.epicode.u5_w3_d2.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String identifier;
    private String password;
}
