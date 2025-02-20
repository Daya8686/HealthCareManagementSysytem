package com.hcms.project.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hcms.project.service.JWTService;
import com.hcms.project.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
   private ApplicationContext context;
    
    public JwtFilter() {
    	System.out.println("This is in filter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

//        System.out.println("Authorization header: '" + authHeader + "'");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7).strip(); // strip() handles all whitespace
//            System.out.println("Extracted token: '" + token + "'"); // Print the token without "Bearer "
//        }
//        // Extract token and validate "Bearer" scheme
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	 
            token = authHeader.substring(7).trim();
            String mytoken=authHeader.substring(7).strip();
            
            System.out.println("This is from here "+token);
            System.out.println("This is from here Another "+mytoken);
            username = jwtService.extractUserName(token);
           
        }

        // Validate token and set the authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
            	System.out.println(jwtService.validateToken(token, userDetails));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
