package com.napier.sem;

import java.sql.*;

public class App
{
    private Connection con = null;

    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait for DB startup
                Thread.sleep(5000);

                // --- Use 127.0.0.1:33060 for local IntelliJ execution ---
                // If running inside a Docker container, change to "jdbc:mysql://db:3306/world?..."
                con = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Gets city details from the database based on ID.
     */
    public City getCity()
    {
        try
        {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT ID, Name, CountryCode, District, Population "
                            + "FROM city ";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next())
            {
                City city = new City();
                city.id = rset.getInt("ID");
                city.name = rset.getString("Name");
                city.countryCode = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");

                return city;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details.");
            return null;
        }
    }

    /**
     * Displays city information to the console.
     */
    public void displayCity(City city)
    {
        if (city != null)
        {
            System.out.println(
                    "ID: " + city.id + "\n"
                            + "Name: " + city.name + "\n"
                            + "Country Code: " + city.countryCode + "\n"
                            + "District: " + city.district + "\n"
                            + "Population: " + city.population + "\n");
        }
        else {
            System.out.println("City object is null. Cannot display details.");
        }
    }

    /**
     * Main application entry point.
     */
    public static void main(String[] args)
    {
        App a = new App();

        a.connect();

        // Get City ID 1 (Kabul)
        City city = a.getCity();

        // Display results
        a.displayCity(city);

        a.disconnect();
    }
}