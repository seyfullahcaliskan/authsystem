package com.example.authsystem.Services;

import com.example.authsystem.Entities.TestEntity;
import com.example.authsystem.Repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<TestEntity> getAllTests() {
        return testRepository.findAll();
    }

    public Optional<TestEntity> getTestById(UUID id) {
        return testRepository.findById(id);
    }

    public TestEntity createTest(TestEntity testEntity) {
        return testRepository.save(testEntity);
    }

    public TestEntity updateTest(UUID id, UUID etag, TestEntity testEntity) {
        Optional<TestEntity> existingEntityOpt = testRepository.findByIdAndEtag(id, etag);
        if (existingEntityOpt.isPresent()) {
            TestEntity existingEntity = existingEntityOpt.get();
            existingEntity.setName(testEntity.getName());
            existingEntity.setSurname(testEntity.getSurname());
            return testRepository.save(existingEntity);
        } else {
            throw new IllegalArgumentException("Test entity not found or etag mismatch");
        }
    }

    public void deleteTest(UUID id) {
        testRepository.deleteById(id);
    }
}