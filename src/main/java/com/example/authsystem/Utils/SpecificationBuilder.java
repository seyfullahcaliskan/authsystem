package com.example.authsystem.Utils;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.Map;
import java.util.UUID;

public class SpecificationBuilder<T> {
    public Specification<T> createSpecification(Map<String, Object> filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null || filter.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, Object> entry : filter.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();

                try {
                    Class<?> fieldType = root.get(fieldName).getJavaType();
                    Object convertedValue = convertValueToFieldType(fieldValue, fieldType);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(fieldName), convertedValue));
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // Hata durumunda filtreyi atla veya logla
                }
            }

            return predicate;
        };
    }


    private Object convertValueToFieldType(Object value, Class<?> fieldType) {
        if (value == null) {
            return null;
        }

        // If the value is already of the correct type, return it directly
        if (fieldType.isAssignableFrom(value.getClass())) {
            return value;
        }

        // Special conversion for Enums
        if (fieldType.isEnum()) {
            return convertToEnum(value, fieldType);
        }

        // Conversion logic for different types
        if (fieldType == Short.class || fieldType == short.class) {
            return Short.valueOf(value.toString());
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.valueOf(value.toString());
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.valueOf(value.toString());
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.valueOf(value.toString());
        } else if (fieldType == Float.class || fieldType == float.class) {
            return Float.valueOf(value.toString());
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value.toString());
        } else if (fieldType == Character.class || fieldType == char.class) {
            return value.toString().charAt(0);
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            return Byte.valueOf(value.toString());
        } else if (fieldType == UUID.class) {
            return UUID.fromString(value.toString());
        } else if (fieldType == String.class) {
            return value.toString();
        }

        throw new IllegalArgumentException("Cannot convert value to field type: " + fieldType.getName());
    }

    private Object convertToEnum(Object value, Class<?> fieldType) {
        Class<? extends Enum> enumClass = fieldType.asSubclass(Enum.class);
        if (value instanceof String) {
            String stringValue = (String) value;
            for (Enum enumConstant : enumClass.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(stringValue) || enumConstant.toString().equalsIgnoreCase(stringValue)) {
                    return enumConstant;
                }
            }
        } else if (value instanceof Number) {
            int intValue = ((Number) value).intValue();
            Enum[] enumConstants = enumClass.getEnumConstants();
            if (intValue >= 0 && intValue < enumConstants.length) {
                return enumConstants[intValue];
            }
            if (IdentifiableEnum.class.isAssignableFrom(enumClass)) {
                for (Enum enumConstant : enumConstants) {
                    IdentifiableEnum identifiableEnum = (IdentifiableEnum) enumConstant;
                    if (identifiableEnum.getId() == intValue) {
                        return enumConstant;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Değer enum tipine dönüştürülemedi: " + fieldType.getName());
    }

}

