package com.ecommerce.ecommerce.auth;

import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login (AuthRequest request){
        try {

            //1. Autentica el usuario con su username y password

            Authentication authentication =
                    authenticationManager.authenticate(
                            new  UsernamePasswordAuthenticationToken(request.getUsername(),
                                    request.getPassword())
                    );

            //2. Si pasa,recupera el usuario desde la base de datos.

            Users user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()-> new RuntimeException("User not found"));

            //3. Genera un token JWT para ese usuario.
            String jwtToken = jwtService.generateToken((UserDetails) user);


            //4. Devuelve el token como respuesta.
            return new AuthResponse(jwtToken);
        } catch (AuthenticationException e){
            throw new RuntimeException("Invalid username or password");
        }
    }
}
