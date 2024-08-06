package com.projectzero.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum UserType {
    USER("user"),
    ADMIN("admin");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.getType().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
