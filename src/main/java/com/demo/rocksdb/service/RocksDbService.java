package com.demo.rocksdb.service;

import com.demo.rocksdb.model.User;
import org.rocksdb.RocksIterator;

import java.util.List;

public interface RocksDbService {
    User get(Long id);
    void put(User user);
    void delete(Long id);
    List<User> list();
}
