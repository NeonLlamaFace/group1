package com.napier.sem;

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Main application class to connect to a MySQL database,
 * execute queries from statements.sql, and print results.
 */
public class App
{
    // The database connection object
    private Connection con = null;

    /**
     * Entry point of the application.
     */
    public static void main(String[] args)
    {
        App app = new App();

        // 1. Establish the connection to the database
        app.connect();

        // 2. Run the SQL statements from the file
        if (app.con != null) {
            app.runSqlStatements("/tmp/statements.sql");
        }

        // 3. Disconnect from the database
        app.disconnect();
    }

    /**
     * Connect to the MySQL database with retry logic to handle Docker startup time.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Could not load SQL driver: " + e.getMessage());
            System.exit(-1);
        }

        // Connection to the database
        int retries = 100;
        long waitTimeMs = 1000;

        // Use the 'db' service name as the host, as defined in docker-compose.yml
        // The database name 'world' is created by the world.sql script.
        final String DB_URL = "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true";
        final String DB_USER = "root";
        // The password 'example' is set in your db/Dockerfile.
        final String DB_PASSWORD = "example";

        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database... Attempt " + (i + 1));
            try
            {
                // Wait a bit for db to start and initialize
                Thread.sleep(waitTimeMs);

                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("✅ Successfully connected to the database.");
                return; // Exit method on successful connection
            }
            catch (SQLException sqle)
            {
                // This is expected while the DB is starting up.
                System.err.println("Failed to connect: " + sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                // Restore interrupted status
                Thread.currentThread().interrupt();
                System.err.println("Connection attempt interrupted.");
            }
        }
        System.err.println("❌ Failed to connect to database after " + retries + " attempts. Exiting.");
        System.exit(-1);
    }

    /**
     * Disconnects from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
                System.out.println("🔌 Successfully disconnected from the database.");
            }
            catch (Exception e)
            {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Reads the SQL file, parses statements, and executes them against the database.
     * @param filePath The path to the SQL file (e.g., /tmp/statements.sql).
     */
    public void runSqlStatements(String filePath) {
        if (con == null) {
            System.err.println("Cannot run SQL statements: Database connection is not established.");
            return;
        }

        try {
            // Read the entire file content
            String sqlContent = Files.readString(Paths.get(filePath));

            // Split the content into individual statements using ';' as a delimiter,
            // but carefully handling empty strings and comments.
            List<String> statements = Arrays.stream(sqlContent.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && !s.startsWith("--"))
                    .toList();

            try (Statement stmt = con.createStatement()) {
                System.out.println("\n--- Executing Statements from " + filePath + " ---");
                int queryCount = 0;

                for (String query : statements) {
                    queryCount++;
                    // Print the first line of the query being executed
                    System.out.println("\nExecuting Query " + queryCount + ": " + query.split("\n")[0].trim() + "...");

                    // Execute the query
                    boolean isResultSet = stmt.execute(query);

                    if (isResultSet) {
                        // Print results for SELECT queries
                        try (ResultSet rs = stmt.getResultSet()) {
                            printResultSet(rs);
                        }
                    } else {
                        // This handles DML (INSERT/UPDATE/DELETE) if present.
                        System.out.println("Update Count: " + stmt.getUpdateCount());
                    }
                }
            }
            System.out.println("--- Finished executing all statements ---");
        } catch (IOException e) {
            System.err.println("Error reading SQL file " + filePath + ": " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to print the contents of a ResultSet in a simple formatted table.
     */
    private void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        // Print column headers
        System.out.println("\nQuery Results:");
        for (int i = 1; i <= columnsNumber; i++) {
            System.out.printf("%-20s", rsmd.getColumnLabel(i));
            if (i < columnsNumber) System.out.print(" | ");
        }
        // Separator line calculation: 20 chars per column + 3 chars for " | " separator
        System.out.println("\n" + "-".repeat(Math.max(0, columnsNumber * 23 - 3)));

        // Print rows
        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
            for (int i = 1; i <= columnsNumber; i++) {
                // Ensure output is readable, trimming if necessary
                String value = rs.getString(i);
                if (value != null && value.length() > 20) {
                    value = value.substring(0, 17) + "...";
                } else if (value == null) {
                    value = "NULL";
                }
                System.out.printf("%-20s", value);
                if (i < columnsNumber) System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println("\nTotal rows retrieved: " + rowCount);
    }
}