package com.demo.rocksdb.service;

import com.demo.rocksdb.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
public class RocksDbServiceImpl implements RocksDbService{
    static {
        // Load the RocksDB native library
        RocksDB.loadLibrary();
    }

    ObjectMapper objectMapper = new ObjectMapper();

    private RocksDB rocksDB;

    private final String dbPath = "/tmp/rocksdb"; //Path to store data

    @PostConstruct
    public void init() {
        try (Options options = new Options().setCreateIfMissing(true)) {
            rocksDB = RocksDB.open(options, dbPath);
        } catch (RocksDBException e) {
            throw new RuntimeException("Error initializing RocksDB", e);
        }
    }

    @Override
    public User get(Long id) {
        User user = null;
        try {
            byte[] bytes = rocksDB.get(longToBytes(id));
            user = deserializeUser(bytes);
        } catch (RocksDBException e) {
            System.out.println("Error while getting rocks db data e::" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while deserializing data e:::" + e.getMessage());
        } finally {
            return user;
        }
    }

    @Override
    public void put(User user) {
        try {
            rocksDB.put(longToBytes(user.getId()), serializeUser(user));
        } catch (RocksDBException e) {
            System.out.println("Error while putting data to rocks db e::" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while serializing data e:::" + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            rocksDB.delete(longToBytes(id));
        } catch (RocksDBException e) {
            System.out.println("Error while deleting data from rocks db e::" + e.getMessage());
        }
    }

    @Override
    public List<User> list() {
        var users = new ArrayList<User>();
        try {
            var iterator = rocksDB.newIterator();
            iterator.seekToFirst();
            while (iterator.isValid()) {
                users.add(deserializeUser(iterator.value()));
            }
        } catch (Exception e) {
            System.out.println("Error while listing users e::" + e.getMessage());
        }
        return users;
    }

    @PreDestroy
    public void close() {
        if (rocksDB != null) {
            rocksDB.close();
        }
    }

    public byte[] longToBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        return buffer.array();
    }

    public User deserializeUser(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, User.class);
    }

    public byte[] serializeUser(User user) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(user);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

}
