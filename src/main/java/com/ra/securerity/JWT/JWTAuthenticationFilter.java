package com.ra.securerity.JWT;

import com.ra.securerity.principals.CustomUserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFormRequest(request);
        try {
            if (token != null && !token.isEmpty()) {
                //xac thuc chuoi token
                boolean valid = jwtProvider.validateToken(token);
                if (valid) {
                    //phai tach username tu token
                    String username = jwtProvider.getUsernameFromToken(token);
                    //lay thong tin tai khoan trong database (lien quan CustomDetailService)
                    UserDetails userDetail = userDetailsService.loadUserByUsername(username);

                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                    //set vao SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFormRequest(HttpServletRequest request) {
        String header_au = request.getHeader("Authorization");
        if (header_au != null && header_au.startsWith("Bearer ")) {
            return header_au.substring("Bearer ".length());
        }
        return null;
    }
}
