package com.neha.TaskManagement.Security.Jwt.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class JwtUser{
    private String principal;
    private String fullName;
//    private Object data;
    private List<String> authorities;
}
