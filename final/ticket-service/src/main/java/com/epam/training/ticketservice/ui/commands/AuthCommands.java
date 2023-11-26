package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.dto.UserDto;
import com.epam.training.ticketservice.core.model.User;
import com.epam.training.ticketservice.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class AuthCommands {

    private final UserService userService;

    @ShellMethod(key = "sign out", value = "User logout")
    public String logout() {
        return userService.logout()
                .map(userDto -> userDto.username() + " successfully logged out.")
                .orElse("There is no user logged in.");
    }

    @ShellMethod(key = "sign in privileged", value = "User login")
    public String login(String username, String password) {
        return userService.login(username, password)
                .map(userDto -> userDto.username() + " successfully signed in")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String print() {
        Optional<UserDto> userDtoOptional = userService.describeAccount();
        if (userDtoOptional.isPresent()) {
            if (userDtoOptional.get().role().equals(User.Role.ADMIN)) {
                return "Signed in with privileged account '" + userDtoOptional.get().username() + "'";
            }

            return "Signed in with user '" + userDtoOptional.get().username() + "'";
        }

        return "You are not signed in";
    }
}
