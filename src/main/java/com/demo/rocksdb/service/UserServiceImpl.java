package com.demo.rocksdb.service;

import com.demo.rocksdb.model.User;
import com.demo.rocksdb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    private RocksDbService rocksDbService;

    public UserServiceImpl(UserRepository userRepository, RocksDbService rocksDbService) {
        this.userRepository = userRepository;
        this.rocksDbService = rocksDbService;
    }

    @Override
    public void addUser(User user) {
        var savedUser = userRepository.save(user);
        rocksDbService.put(savedUser);
    }

    @Override
    public User getUserFromRocksDb(Long id) {
        return rocksDbService.get(id);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> listUsers() {
        return rocksDbService.list();
    }
}
