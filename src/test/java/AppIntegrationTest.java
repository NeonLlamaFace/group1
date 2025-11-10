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
        app.connect("127.0.0.1:33060", 5000);
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
}