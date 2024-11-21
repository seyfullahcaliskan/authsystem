package com.example.authsystem.Services;
import com.example.authsystem.DTO.PagedResponseDTO;
import com.example.authsystem.Entities.TestEntity;
import com.example.authsystem.Repositories.TestRepository;
import com.example.authsystem.Utils.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public PagedResponseDTO<TestEntity> getAllTestsPaged(PagedResponseDTO pagingRequest) {
        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber(), pagingRequest.getPageSize());
        SpecificationBuilder<TestEntity> specBuilder = new SpecificationBuilder<>();
        Specification<TestEntity> spec = specBuilder.createSpecification(pagingRequest.getFilter());
        Page<TestEntity> pagedTests = testRepository.findAll(spec, pageable);

        List<TestEntity> content = pagedTests.getContent();
        PagedResponseDTO<TestEntity> response = new PagedResponseDTO<>(
                content,
                pagedTests.getNumber(),
                pagedTests.getSize(),
                pagedTests.getTotalElements(),
                pagedTests.getTotalPages(),
                pagedTests.isLast(),
                pagingRequest.getFilter()
        );
        return response;
    }

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

    public List<TestEntity> findTests(Map<String, Object> filter) {
        SpecificationBuilder<TestEntity> specBuilder = new SpecificationBuilder<>();
        Specification<TestEntity> spec = specBuilder.createSpecification(filter);
        return testRepository.findAll(spec);
    }

    public Optional<TestEntity> findTest(Map<String, Object> filter) {
        SpecificationBuilder<TestEntity> specBuilder = new SpecificationBuilder<>();
        Specification<TestEntity> spec = specBuilder.createSpecification(filter);
        return testRepository.findOne(spec);
    }
}
