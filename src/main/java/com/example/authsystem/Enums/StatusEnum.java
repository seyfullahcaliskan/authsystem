package com.example.authsystem.Enums;

public enum StatusEnum {
    KAPALI(1),
    PASIF(2),
    AKTIF(3),
    IPTAL(4);

    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusEnum fromValue(int value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Bilinmeyen durum degeri: " + value);
    }
}
