package com.example.authsystem.Repositories;

import com.example.authsystem.Entities.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID>, JpaSpecificationExecutor<TestEntity> {
    Optional<TestEntity> findByIdAndEtag(UUID id, UUID etag);
}
