package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.integration.config.DataBaseConfigTest;

import java.sql.Connection;

public class DataBasePrepareService {

    DataBaseConfig dataBaseTestConfig = new DataBaseConfig();

    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
}
