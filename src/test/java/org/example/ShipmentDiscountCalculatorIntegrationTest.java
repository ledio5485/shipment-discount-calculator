package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentDiscountCalculatorIntegrationTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void e2e() throws IOException {
        String[] args = new String[]{"input.txt", "price.txt"};

        ShipmentDiscountCalculator.main(args);

        var expected = Files.lines(Paths.get("output.txt")).collect(Collectors.joining("\n"));
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }
}