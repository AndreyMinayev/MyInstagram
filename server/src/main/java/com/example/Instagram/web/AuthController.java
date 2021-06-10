package com.example.Instagram.web;


import com.example.Instagram.payload.request.LoginRequest;
import com.example.Instagram.payload.request.SignupRequest;
import com.example.Instagram.payload.response.JWTTokenSuccessResponse;
import com.example.Instagram.payload.response.MessageResponse;
import com.example.Instagram.security.JWTokenProvider;
import com.example.Instagram.security.SecurityConstants;
import com.example.Instagram.services.UserService;
import com.example.Instagram.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {


    @Autowired
    private JWTokenProvider jwTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;
    @Autowired
    private UserService userService;


    @PostMapping("/signin") // signing in   user fills loginRequest object that we created
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){

        // checking errors  (using our responseErrorValidator)  if there is errors - return errors ResponseEntity<Object>
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        // creating  authentication object (Security API)
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generating token using our jwTokenProvider  adding TOKEN_PREFIX and saving in STRING
        String jwt = SecurityConstants.TOKEN_PREFIX + jwTokenProvider.generateToken(authentication);

        // returning responseEntity with our JWTTokenSuccessResponse object  with  boolean true and String jwt data
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true,jwt));

    }




    @PostMapping("/signup")   // signing up   user fills signupRequest object that we created
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult){
        // checking errors  (using our responseErrorValidator)  if there is errors - return errors ResponseEntity<Object>
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return  errors;

        // creating user using userService that we created.
        //  we are using that method  to log, encode password  and give  adequate response if  user alreay exist
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }




}
