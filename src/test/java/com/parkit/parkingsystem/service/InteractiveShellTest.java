package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InteractiveShellTest {
    private ByteArrayOutputStream outputStream;

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private TicketDAO ticketDAO;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ticketDAO = new TicketDAO();
        parkingSpotDAO = new ParkingSpotDAO();
        inputReaderUtil = new InputReaderUtil();
    }

    //load interface working

    @Test
    public void testLoadInterface_withValidOptionToLeave() {
        String simulatedInput = "3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        InteractiveShell.loadInterface();
        assertTrue(outputStream.toString().contains("Exiting from the system!"));
        System.setIn(System.in);
    }

//    @Test
//    public void testLoadInterface_withInvalidOption() {
//        String simulatedInput = "99\n3\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        InteractiveShell.loadInterface();
//
//        assertTrue(outputStream.toString().contains("Unsupported option. Please enter a number corresponding to the provided menu"));
//        assertTrue(outputStream.toString().contains("Exiting from the system!"));
//    }
//
//    @Test
//    public void testLoadInterface_withInvalidOption2() {
//        String simulatedInput = "1\n3\n3\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        InteractiveShell.loadInterface();
//
//        assertTrue(outputStream.toString().contains("Incorrect input provided"));
//        assertTrue(outputStream.toString().contains("Exiting from the system!"));
//    }
}