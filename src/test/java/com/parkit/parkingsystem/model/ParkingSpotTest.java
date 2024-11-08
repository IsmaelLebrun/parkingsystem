package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {
    private ParkingSpot parkingSpot;

    @BeforeEach
    public void setUp(){
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    void setId_work() {
        parkingSpot.setId(2);
        assertEquals(2, parkingSpot.getId());
    }

    @Test
    void setParkingType() {
        parkingSpot.setParkingType(ParkingType.CAR);
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
    }
}