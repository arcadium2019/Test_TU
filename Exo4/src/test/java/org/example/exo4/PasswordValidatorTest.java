package org.example.exo4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void getErrorMessage_null_shouldReturnNullMessage() {
        assertEquals("Password must not be null", validator.getErrorMessage(null));
    }

    @ParameterizedTest
    @CsvSource({
        "Password1!, true",
        "Admin2024@, true",
        "short1!,    false",
        "PASSWORD1!, false",
        "password1!, false",
        "Password!,  false",
        "Password1,  false"
    })
    void isValid_csvSource_shouldMatchExpected(String password, boolean expected) {
        assertEquals(expected, validator.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@", "Secure#99", "Hello$1World"})
    void isValid_validPasswords_shouldReturnTrue(String password) {
        assertTrue(validator.isValid(password));
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> errorMessageCases() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("short1!", "Password must contain at least 8 characters"),
            org.junit.jupiter.params.provider.Arguments.of("PASSWORD1!", "Password must contain at least one lowercase letter"),
            org.junit.jupiter.params.provider.Arguments.of("password1!", "Password must contain at least one uppercase letter"),
            org.junit.jupiter.params.provider.Arguments.of("Password!",  "Password must contain at least one digit"),
            org.junit.jupiter.params.provider.Arguments.of("Password1",  "Password must contain at least one special character"),
            org.junit.jupiter.params.provider.Arguments.of("Password1!", "Password is valid")
        );
    }

    @ParameterizedTest
    @MethodSource("errorMessageCases")
    void getErrorMessage_shouldReturnExpectedMessage(String password, String expectedMessage) {
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_nullAndEmpty_shouldReturnFalse(String password) {
        assertFalse(validator.isValid(password));
    }
}