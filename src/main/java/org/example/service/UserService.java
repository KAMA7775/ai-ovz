package org.example.service;

import org.example.entity.User;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(Long telegramId, String name) {
        if (userRepository.findByTelegramId(telegramId) == null) {
            User user = new User();
            user.setTelegramId(telegramId);
            user.setName(name);
            userRepository.save(user);
        }
    }
    public void addUserIfNotExists(Long telegramId, String name) {
        if (userRepository.findByTelegramId(telegramId) == null) {
            User user = new User();
            user.setTelegramId(telegramId);
            user.setName(name);
            userRepository.save(user);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

