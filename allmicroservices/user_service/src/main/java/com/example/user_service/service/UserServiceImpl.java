package com.example.user_service.service;

import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> getUserById(Integer user_id) {
        return userRepository.findById(user_id);
    }

    @Override
    public void deleteUserById(Integer user_id) {
        userRepository.deleteById(user_id);
    }

    @Override
    public UserEntity updateUser(Integer user_id, UserEntity userEntity) {
        UserEntity userDB = userRepository.getById(user_id);

        if(Objects.nonNull(userEntity.getUser_name()) && !"".equalsIgnoreCase(userEntity.getUser_name())) {
            userDB.setUser_name(userEntity.getUser_name());
        }
        if(Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
            userDB.setEmail(userEntity.getEmail());
        }

        return userRepository.save(userDB);
    }

    @Override
    public UserEntity getUserByName(String user_name) {
        return userRepository.findByNameIgnoreCase(user_name);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
}
