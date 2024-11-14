package com.example.authsystem.Controllers;

import com.example.authsystem.DTO.PageRequestDTO;
import com.example.authsystem.Entities.TestEntity;
import com.example.authsystem.Services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/paged")
    public ResponseEntity<Page<TestEntity>> getAllTestsPaged(@RequestBody PageRequestDTO pagingRequest) {
        Page<TestEntity> pagedTests = testService.getAllTestsPaged(pagingRequest);
        return new ResponseEntity<>(pagedTests, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TestEntity>> getAllTests() {
        List<TestEntity> tests = testService.getAllTests();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestEntity> getTestById(@PathVariable UUID id) {
        Optional<TestEntity> testEntity = testService.getTestById(id);
        return testEntity.map(test -> new ResponseEntity<>(test, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TestEntity> createTest(@RequestBody TestEntity testEntity) {
        TestEntity createdTest = testService.createTest(testEntity);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{etag}")
    public ResponseEntity<TestEntity> updateTest(@PathVariable UUID id, @PathVariable UUID etag, @RequestBody TestEntity testEntity) {
        try {
            TestEntity updatedTest = testService.updateTest(id, etag, testEntity);
            return new ResponseEntity<>(updatedTest, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable UUID id) {
        testService.deleteTest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
