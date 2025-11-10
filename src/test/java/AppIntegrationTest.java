package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect();
    }

    @Test
    void connectTest()
    {
        app.connect();
    }

    @Test
    void disconnectTest()
    {
        app.disconnect();
    }

    @Test
    void getCityTest()
    {
        City testCity = new City();
        testCity.id = 1;
        testCity.name = "London";
        testCity.countryCode = "GBR";
        testCity.district = "England";
        testCity.population = 9000000;
    }

    @Test
    void displayCityTest()
    {
        City testCity = new City();
        testCity.id = 1;
        testCity.name = "London";
        testCity.countryCode = "GBR";
        testCity.district = "England";
        testCity.population = 9000000;
    }
}