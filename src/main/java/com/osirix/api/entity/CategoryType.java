package com.osirix.api.entity;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CategoryType {
	GAME,
	APP;
	
	@JsonCreator 
    public static CategoryType fromString(String value) {
        if (value == null) {
            return null; 
        }
        for (CategoryType type : CategoryType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values())
        );
    }
}
