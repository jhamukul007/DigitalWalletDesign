package com.dwp.services;

import com.dwp.exceptions.ResourceAlreadyExistException;
import com.dwp.exceptions.ResourceNotFoundException;
import com.dwp.models.Logging;
import com.dwp.models.User;
import com.dwp.utils.AppUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class UserService {
    private final Set<User> users;
    private final Logging logger;

    public UserService(Logging logger) {
        this.logger = logger;
        this.users = new HashSet<>();

    }

    public void registerUser(User user) {
        getUserByEmailId(user.getEmailId()).ifPresent(u -> {
            throw new ResourceAlreadyExistException(AppUtil.RESOURCE_ALREADY_FOUND.apply("User", "email"));
        });
        users.add(user);
        logger.log(String.format("User %s has been created ", user.getEmailId()));
    }

    User getUserByEmailOrThrowException(String email) {
        return users.stream().filter(user -> Objects.deepEquals(email, user.getEmailId())).findAny().orElseThrow(() ->
                new ResourceNotFoundException(AppUtil.RESOURCE_NOT_FOUND.apply("User", "email")));
    }

    Optional<User> getUserByEmailId(String email) {
        return users.stream().filter(user -> Objects.deepEquals(email, user.getEmailId())).findAny();
    }
    public User getUserByIdOrThrowException(Long id) {
        return users.stream().filter(user -> Objects.deepEquals(id, user.getId())).findAny().orElseThrow(() ->
                new ResourceNotFoundException(AppUtil.RESOURCE_NOT_FOUND.apply("User", "id")));
    }
}
