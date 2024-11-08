package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.mockito.Mockito.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {
    private TicketDAO ticketDAO;
    @Mock
    private DataBaseConfig mockDataBaseConfig;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private Ticket ticket;

    @BeforeEach
    public void setUp() throws Exception {
        ticketDAO = new TicketDAO();
        ticket = new Ticket();
    }

    @Test
    public void testGetTicket_Success() {
        ticketDAO.getTicket("NEVEROUT");
        assertNotNull(ticketDAO);
    }

    @Test
    public void testGetTicket_NoTicketFound() {
        Ticket result = ticketDAO.getTicket("REGNUMBERTHATNOTEXIST");
        assertNull(result, "The reg number doesn't exist so the method getTicket return null");
    }

    @Test
    public void testGetTicket_error() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenThrow(new SQLException("Database connection error"));
        Ticket ticket = ticketDAO.getTicketToOut("SOME_REG_NUMBER");
        assertNull(ticket, "Ticket should be null if there is an exception during the database connection.");
    }

    // DIFFERENCE BETWEEN GetTicket and GetTicketToOut is the sql request in it, in the ToOut there is "AND out_time IS NULL" in the WHERE clause.

    @Test
    public void testGetTicketToOut_Success() {
        ticketDAO.getTicketToOut("NEVEROUT");
        assertNotNull(ticketDAO);
    }

    @Test
    public void testGetTicketToOut_NoTicketFound() {
        Ticket result = ticketDAO.getTicket("REGNUMBERTHATNOTEXIST");
        assertNull(result, "The reg number doesn't exist so the method getTicket return null");
    }

    @Test
    public void testGetTicketToOut_error() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenThrow(new SQLException("Database connection error"));
        Ticket ticket = ticketDAO.getTicketToOut("SOME_REG_NUMBER");
        assertNull(ticket, "Ticket should be null if there is an exception during the database connection.");
    }

    @Test
    public void testIsRegAlreadyParked_false(){
        boolean isIn = ticketDAO.isRegAlreadyParked("REGNUMBERQUINEXISTEPAS");
        assertFalse(isIn);
    }

    @Test
    public void testIsRegAlreadyParked_true() throws Exception{
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(DBConstants.CHECK_REG_NUMBER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        boolean isIn = ticketDAO.isRegAlreadyParked("NEVEROUT");
        assertTrue(isIn);
    }

    @Test
    public void updateTicket_isUpdated(){
        Date inTime = new Date();
        ticket.setVehicleRegNumber("REGNUMBER");
        ticket.setPrice(100.0);
        ticket.setInTime(inTime);
        ticket.setOutTime(inTime);
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    public void updateTicket_isNotUpdated() {
        Date inTime = new Date();
        ticket.setVehicleRegNumber(null);
        ticket.setInTime(null);
        ticket.setOutTime(null);
        assertFalse(ticketDAO.updateTicket(ticket));
    }

    @Test
    public void testGetVisitCount_NoVisits() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(DBConstants.GET_OCCURRENCE_REG_NUMBER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No records found

        int visitCount = ticketDAO.getVisitCount("NON_EXISTENT_REG");

        assertEquals(0, visitCount, "Expected visit count to be 0 when no visits found.");

        verify(mockPreparedStatement, times(1)).setString(1, "NON_EXISTENT_REG");
        verify(mockResultSet, times(1)).next();
    }

    // Test Case 2: One or more visits (expect 1 or greater)
    @Test
    public void testGetVisitCount_OneOrMoreVisits() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(DBConstants.GET_OCCURRENCE_REG_NUMBER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(3); // Simulate 3 visits

        int visitCount = ticketDAO.getVisitCount("EXISTING_REG");

        assertEquals(3, visitCount, "Expected visit count to match the simulated database return.");

        verify(mockPreparedStatement, times(1)).setString(1, "EXISTING_REG");
        verify(mockResultSet, times(1)).getInt(1);
    }

    // Test Case 3: Database connection error (expect 0 as fallback)
    @Test
    public void testGetVisitCount_DatabaseConnectionError() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenThrow(new SQLException("Database connection error"));

        int visitCount = ticketDAO.getVisitCount("ANY_REG");

        assertEquals(0, visitCount, "Expected visit count to be 0 on database connection error.");

        verify(mockDataBaseConfig, times(1)).getConnection();
    }

    @Test
    public void testSaveTicket_Success() throws Exception {
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        // Arrange
        when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(mockPreparedStatement);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("TEST123");
        ticket.setPrice(10.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);

        // Act
        ticketDAO.saveTicket(ticket);

        // Assert
        verify(mockPreparedStatement, times(1)).setInt(1, parkingSpot.getId());
        verify(mockPreparedStatement, times(1)).setString(2, ticket.getVehicleRegNumber());
        verify(mockPreparedStatement, times(1)).setDouble(3, ticket.getPrice());
        verify(mockPreparedStatement, times(1)).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        verify(mockPreparedStatement, times(1)).setTimestamp(5, null);  // outTime is null in this test
        verify(mockPreparedStatement, times(1)).execute();
    }

    @Test
    public void testSaveTicket_Exception() throws Exception {
        // Arrange
        ticketDAO.dataBaseConfig = mockDataBaseConfig;
        when(mockDataBaseConfig.getConnection()).thenThrow(new SQLException("Database connection error"));

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("TEST123");
        ticket.setPrice(10.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);

        // Act & Assert
        assertDoesNotThrow(() -> ticketDAO.saveTicket(ticket), "Exception should be caught and not thrown");

        verify(mockDataBaseConfig, times(1)).getConnection();
    }
}
