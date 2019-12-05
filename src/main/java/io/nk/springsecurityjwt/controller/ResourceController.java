package io.nk.springsecurityjwt.controller;

import io.nk.springsecurityjwt.jwtUtility.JwtUtil;
import io.nk.springsecurityjwt.models.AuthenticaionRequest;
import io.nk.springsecurityjwt.models.AuthenticationResponse;
import io.nk.springsecurityjwt.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResourceController {

   @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/hello")
    public String getMessage(){
        return "Hello world";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticaionRequest authenticaionRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticaionRequest.getUserName(), authenticaionRequest.getPassword())
        );
        }catch (BadCredentialsException ex){
            throw new Exception("Incorect username or password ", ex);
        }

        final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticaionRequest.getUserName());

        final String jwt=jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

}
