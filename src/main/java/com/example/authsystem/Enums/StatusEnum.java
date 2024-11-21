package com.example.authsystem.Enums;

import com.example.authsystem.Utils.IdentifiableEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum implements IdentifiableEnum {
    CANCELLED(4, "İptal"),
    CLOSED(1, "Kapalı"),
    PASSIVE(2, "Pasif"),
    ACTIVE(3, "Aktif");


    private final int id;
    private final String value;

    StatusEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    @JsonValue
    public StatusRepresentation toJson() {
        return new StatusRepresentation(id, value);
    }

    @JsonCreator
    public static StatusEnum fromJson(@JsonProperty("id") int id) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Bilinmeyen durum degeri: " + id);
    }

    public static StatusEnum fromValue(int id) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Bilinmeyen durum degeri: " + id);
    }

    @Override
    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static class StatusRepresentation {
        private final int id;
        private final String value;

        public StatusRepresentation(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }
}
