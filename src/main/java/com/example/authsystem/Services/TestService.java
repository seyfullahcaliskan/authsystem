package com.example.authsystem.Services;
import com.example.authsystem.DTO.PagedResponseDTO;
import com.example.authsystem.Entities.TestEntity;
import com.example.authsystem.Enums.StatusEnum;
import com.example.authsystem.Repositories.TestRepository;
import jakarta.persistence.criteria.Predicate;
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
        Specification<TestEntity> spec = createSpecification(pagingRequest.getFilter());
        Page<TestEntity> pagedTests = testRepository.findAll(spec, pageable);

        // Convert Page<TestEntity> to PagedResponseDTO<TestEntity>
        List<TestEntity> content = pagedTests.getContent();
        PagedResponseDTO<TestEntity> response = new PagedResponseDTO<>(
                content,
                pagedTests.getNumber(),
                pagedTests.getSize(),
                pagedTests.getTotalElements(),
                pagedTests.getTotalPages(),
                pagedTests.isLast(),
                pagingRequest.getFilter()  // Include the filter from the request
        );

        return response;
    }

    /**
     * createSpecification: Filtre parametrelerini alır ve Specification oluşturur
     */
    private Specification<TestEntity> createSpecification(Map<String, Object> filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null || filter.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, Object> entry : filter.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();

                try {
                    // Field tipini root (entity) üzerinden alıyoruz
                    Class<?> fieldType = root.get(fieldName).getJavaType();
                    // Filtre değerini field tipine dönüştürüyoruz
                    Object convertedValue = convertValueToFieldType(fieldValue, fieldType);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(fieldName), convertedValue));
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // Eğer alan veya dönüştürme hatası oluşursa bu filtre koşulunu atla
                    // Hataları loglayabilir veya handle edebilirsiniz
                }
            }

            return predicate;
        };
    }

    /**
     * convertValueToFieldType: Filtre değerini ilgili alanın tipine dönüştürür
     */
    private Object convertValueToFieldType(Object value, Class<?> fieldType) {
        if (value == null) {
            return null;
        }

        // Eğer değer zaten doğru türdeyse direkt döner
        if (fieldType.isAssignableFrom(value.getClass())) {
            return value;
        }

        // Enum alanlar için özel dönüştürme
        if (fieldType.isEnum()) {
            return convertToEnum(value, fieldType);
        }

        // Sayısal türler için dönüştürme
        if (fieldType == short.class || fieldType == Short.class) {
            if (value instanceof Number) {
                return ((Number) value).shortValue();
            } else if (value instanceof String) {
                return Short.parseShort((String) value);
            }
        } else if (fieldType == int.class || fieldType == Integer.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
        } else if (fieldType == long.class || fieldType == Long.class) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else if (value instanceof String) {
                return Long.parseLong((String) value);
            }
        } else if (fieldType == double.class || fieldType == Double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            }
        } else if (fieldType == float.class || fieldType == Float.class) {
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else if (value instanceof String) {
                return Float.parseFloat((String) value);
            }
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            if (value instanceof Boolean) {
                return value;
            } else if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            }
        } else if (fieldType == char.class || fieldType == Character.class) {
            if (value instanceof Character) {
                return value;
            } else if (value instanceof String) {
                String str = (String) value;
                if (str.length() == 1) {
                    return str.charAt(0);
                }
            }
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            if (value instanceof Number) {
                return ((Number) value).byteValue();
            } else if (value instanceof String) {
                return Byte.parseByte((String) value);
            }
        } else if (fieldType == UUID.class && value instanceof String) {
            return UUID.fromString((String) value);
        }

        throw new IllegalArgumentException("Value type not convertible to field type for field type: " + fieldType.getName());
    }

    private Object convertToEnum(Object value, Class<?> fieldType) {
        if (fieldType.equals(StatusEnum.class)) {
            if (value instanceof Number) {
                int intValue = ((Number) value).intValue();
                return StatusEnum.fromValue(intValue);
            } else if (value instanceof String) {
                String stringValue = (String) value;
                try {
                    int intValue = Integer.parseInt(stringValue);
                    return StatusEnum.fromValue(intValue);
                } catch (NumberFormatException e) {
                    // Eğer string, sayıya dönüştürülemezse, enum değeri olarak kabul edilebilir
                    return StatusEnum.valueOf(stringValue.toUpperCase());
                }
            }
        }

        throw new IllegalArgumentException("Enum value not convertible for enum type: " + fieldType.getName());
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
}
