package com.epam.training.ticketservice;

import com.epam.training.ticketservice.core.model.User;
import com.epam.training.ticketservice.core.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminInit {

    private final UserRepo userRepo;

    @PostConstruct
    public void init() {
        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepo.save(admin);
    }
}