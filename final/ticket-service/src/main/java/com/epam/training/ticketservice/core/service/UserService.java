package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.dto.UserDto;
import com.epam.training.ticketservice.core.model.User;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> login(String name, String password);

    boolean isValidUserData(User user, String password);

    Optional<UserDto> logout();

    Optional<UserDto> describeAccount();
}
