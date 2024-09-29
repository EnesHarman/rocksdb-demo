package com.demo.rocksdb.service;

import com.demo.rocksdb.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    User getUserFromRocksDb(Long id);

    User getUser(Long id);

    List<User> listUsers();
}
