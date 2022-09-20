package com.app.restsecurity.service.impl;

import com.app.restsecurity.entity.User;
import com.app.restsecurity.exception.UserException;
import com.app.restsecurity.modal.JWT;
import com.app.restsecurity.repository.UserRepository;
import com.app.restsecurity.security.JWTTokenUtil;
import com.app.restsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTTokenUtil jwtTokenUtil;

    private final List<String> ADMIN_PHONES = List.of("1234567890", "1515154747");

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    @Transactional
    public void save(User user) throws UserException {
        if (findByPhone(user.getPhone()) != null) {
            throw new UserException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public JWT login(User user) throws UserException {
        var u = findByPhone(user.getPhone());
        if (u == null) {
            throw new UserException("User not found");
        }
        if (!passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            throw new UserException("Wrong password");
        }
        var roles = ADMIN_PHONES.contains(u.getPhone()) ?
                List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")) :
                List.of(new SimpleGrantedAuthority("USER"));
        String accessToken = jwtTokenUtil.generateAccessToken(u.getPhone(), roles);
        String refreshToken = jwtTokenUtil.generateRefreshToken(u.getPhone(), roles);
        return new JWT(accessToken, refreshToken);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    private User findByPhone(String phone) throws UserException {
        return userRepository.findByPhone(phone).orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        try {
            return findByPhone(phone);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Bean
    public void createDummyUser() {
        userRepository.save(new User(null, "1234567890", passwordEncoder.encode("12345")));
        userRepository.save(new User(null, "1515154747", passwordEncoder.encode("abcde")));
    }
}
