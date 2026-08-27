package com.orangehrm.utils;

import com.orangehrm.models.Employee;
import org.apache.commons.lang3.RandomStringUtils;

public class TestDataGenerator {

    private TestDataGenerator() {}

    public static Employee generateEmployee() {
        String suffix = RandomStringUtils.randomAlphabetic(6).toLowerCase();
        return Employee.builder()
                .firstName("Auto" + RandomStringUtils.randomAlphabetic(5))
                .middleName("M" + RandomStringUtils.randomAlphabetic(3))
                .lastName("Test" + RandomStringUtils.randomAlphabetic(5))
                .email("auto." + suffix + "@test.com")
                .mobilePhone("07" + RandomStringUtils.randomNumeric(9))
                .build();
    }

    public static String generateEmployeeId() {
        return "EMP" + RandomStringUtils.randomNumeric(5);
    }

    public static String randomAlpha(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
}
