package com.mrakshith21.programming_judge.enums;

import lombok.Getter;

@Getter
public enum Language {

    JAVA_21("JAVA_21"),
    CPLUSPLUS_17("CPLUSPLUS_17"),
    PYTHON_3_13("PYTHON_3_13");

    private final String value;

    Language(String value) {
        this.value = value;
    }
}
