package com.neha.TaskManagement.Security.Jwt;

import com.neha.TaskManagement.Security.Jwt.Config.JwtConfig;
import com.neha.TaskManagement.Security.Jwt.Model.JwtUser;
import com.neha.TaskManagement.Security.LocalAuthStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private JwtOperationWrapperSvc jwtOperationWrapperSvc;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader(jwtConfig.getHeader())==null){
            filterChain.doFilter(request,response);
            return;
        }
        String token = request.getHeader(jwtConfig.getHeader()).replace("Bearer","").trim();
        JwtUser jwtUser = jwtOperationWrapperSvc.validateToken(token);
        if (jwtUser==null){
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Token invalid or expired.\"}");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        //adding current token and authenticated user for the current api call.
        LocalAuthStore localAuthStore = new LocalAuthStore(token,jwtUser);
        LocalAuthStore.setLocalAuthStore(localAuthStore);

        List<GrantedAuthority> authorities = jwtUser.getAuthorities()
                .stream().map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser.getPrincipal(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}
