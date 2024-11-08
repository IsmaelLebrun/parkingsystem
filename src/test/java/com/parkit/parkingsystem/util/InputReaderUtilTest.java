package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputReaderUtilTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    public void tearDown() {
        System.setIn(originalSystemIn);
    }


    @Test
    void readSelection_goodInput() {
        // can be 1, 2 or 3 -----|
        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        int result = inputReaderUtil.readSelection();
        assertEquals(1, result);
    }

    @Test
    void readSelection_badInput(){
        // can be everything except 1, 2 or 3
        String simulatedInput = "a\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        int result = inputReaderUtil.readSelection();
        assertEquals(-1, result);
    }

    @Test
    void readVehicleRegistrationNumber_work() throws Exception {
        String simulatedInput = "ABCDEF\n";
        String expectedInput = "ABCDEF";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String result = inputReaderUtil.readVehicleRegistrationNumber();
        assertEquals(expectedInput, result);
    }

    @Test
    void readVehicleRegistrationNumber_exceptionIllegalArgument() throws Exception{
        System.setIn(new ByteArrayInputStream("\n".getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        Exception exception = assertThrows(IllegalArgumentException.class, inputReaderUtil::readVehicleRegistrationNumber);
        assertEquals("Invalid input provided", exception.getMessage());
        String expectedConsoleMessage = "Error reading input. Please enter a valid string for vehicle registration number";
        assertTrue(output.toString().contains(expectedConsoleMessage),"Expected console output message: " + expectedConsoleMessage);
    }
}