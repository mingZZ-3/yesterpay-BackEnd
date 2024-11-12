package com.yesterpay.swaggerTest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    public void save(TestEntity userInfo) {
    }

    public List<TestEntity> findAll() {
        TestEntity testEntity = new TestEntity();
        return (List<TestEntity>) testEntity;
    }
}
