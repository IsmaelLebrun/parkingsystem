package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ParkingSpotDAOTest {
    private ParkingSpotDAO parkingSpotDAO;
    @Mock
    private DataBaseConfig mockDataBaseConfig;
    private DataBaseConfig dataBaseConfig;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    void getNextAvailableSlot_car() {
        int nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(1, nextSlot);
    }

    @Test
    void getNextAvailableSlot_bike() {
        int nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(4, nextSlot);
    }


    @Test
    void updateParking_true() {
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    void updateParking_false() {
        assertFalse(parkingSpotDAO.updateParking(null));
    }
}