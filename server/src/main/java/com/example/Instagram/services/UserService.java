package com.example.Instagram.services;


import com.example.Instagram.dto.UserDTO;
import com.example.Instagram.entity.User;
import com.example.Instagram.entity.enums.ERole;
import com.example.Instagram.exception.UserAlreadyExistException;
import com.example.Instagram.payload.request.SignupRequest;
import com.example.Instagram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getName());
        user.setSurname(userIn.getSurname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserAlreadyExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }


    public User updateUser(UserDTO userDTO, Principal principal){
        User user= getUserByPrincipal(principal);
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }

    public  User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }

    private  User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));

    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }
}
