package com.neha.TaskManagement.Security;

import com.neha.TaskManagement.Security.Jwt.Model.JwtUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalAuthStore {
    public static final ThreadLocal<LocalAuthStore> authenticationContext = new ThreadLocal<>();
    private String token;
    private JwtUser jwtUser;
    public static void setLocalAuthStore(LocalAuthStore localAuthStore){
        authenticationContext.set(localAuthStore);
    }
    public static LocalAuthStore getLocalAuthStore(){
        return authenticationContext.get();
    }
    public static void remove(){
        authenticationContext.remove();
    }
}

