package com.example.userservice.service;

import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.UserDTO;
import com.example.userservice.repository.UserRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        var user = userRepository.findById(id)
                .orElse(null);
        return UserMapper.toDTO(user);
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public UserDTO getUserByPhone(String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);
        return UserMapper.toDTO(user);
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElse(null);
        return UserMapper.toDTO(user);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public UserDTO addUser(UserDTO userDTO) {
        var user = userRepository.save(UserMapper.toEntity(userDTO));
        return UserMapper.toDTO(user);
    }

    @Override
    @Nullable
    @Transactional(propagation = Propagation.NESTED)
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        if (id.equals(userDTO.getId()) && userRepository.existsById(id)) {
            var user = userRepository.save(UserMapper.toEntity(userDTO));
            return UserMapper.toDTO(user);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
