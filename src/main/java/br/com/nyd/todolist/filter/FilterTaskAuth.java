package br.com.nyd.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.nyd.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();


        if(servletPath.startsWith("/tasks")){
            var authorization = request.getHeader("Authorization");
            var auth_encoded = authorization.substring("Basic".length()).trim();
            var auth_decoded = Base64.getDecoder().decode(auth_encoded);
            var auth_string = new String(auth_decoded);
            var credentials = auth_string.split(":");
            var username = credentials[0];
            var password = credentials[1];
            var user = repository.findByUsername(username);
            if(user == null){
                response.sendError(401);
            }
            else{
                var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerified.verified){
                    request.setAttribute("userId", user.getId());
                    chain.doFilter(request,response);
                }else{
                    response.sendError(401);
                }
            }

        }else {
            chain.doFilter(request,response);
        }
    }
}
