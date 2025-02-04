package com.neha.TaskManagement.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalAuthStore {
    public static final ThreadLocal<LocalAuthStore> authenticationContext = new ThreadLocal<>();
    private String token;
    public static void setLocalAuthStore(LocalAuthStore localAuthStore){
        authenticationContext.set(localAuthStore);
    }
    public static LocalAuthStore getLocalAuthStore(){
        return authenticationContext.get();
    }
    public void remove(){
        authenticationContext.remove();
    }
}
