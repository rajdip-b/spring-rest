package com.app.restsecurity.service;

import com.app.restsecurity.entity.User;
import com.app.restsecurity.exception.UserException;
import com.app.restsecurity.modal.JWT;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void save(User user) throws UserException;

    JWT login(User user) throws UserException;

    boolean existsByPhone(String phone);

}
