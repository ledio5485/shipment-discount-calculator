package org.example.logic;

import org.example.domain.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolePrintServiceTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final ConsolePrintService sut = new ConsolePrintService();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void shouldPrintTheOutputCorrectly() {
        var transactions = List.of(
                new Transaction("2015-02-01 S MR"),
                new Transaction("2015-02-02 M LP")
        );

        sut.print(transactions);

        var expected =
                "2015-02-01 S MR 0.00 -\n" +
                "2015-02-02 M LP 0.00 -";
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }
}