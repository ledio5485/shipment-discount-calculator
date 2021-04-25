package org.example.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileInputParserTest {

    @Test
    void shouldThrowExceptionWhenCannotParseAFile() {
        Exception exception = assertThrows(RuntimeException.class, () -> FileInputParser.parseShippingPrice("no_file.txt"));

        String expectedMessage = "Cannot parse the file no_file.txt";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}