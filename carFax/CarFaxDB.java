package kptProject3;

import java.util.*;
import java.sql.*;

public class CarFaxDB
// Handles all DB connections 
{
	
	private static Connection	con;
	private static final String	URL			= "jdbc:derby://localhost:1527/kptProject3";
	private static final String	username	= "password";
	private static final String	password	= "username";
	// Yes, the username is password and vice versa
	
	public static String errors = "";
	
	public static void add(Car car)
	{
		// adds a car to the database
		
		clearErrors();
		try
		{
			// establish connection
			con = DriverManager.getConnection(URL, username, password);
			
			// create a statement object
			Statement st = con.createStatement();
			
			// SQL command
			String sql = "INSERT INTO CAR (VIN, MAKE, MODEL, MODELYEAR)"
						+ "VALUES ('" + car.getVin() + "','" + car.getMake() + "','" + car.getModel() + "'," + car.getYear() + ")";
			
			st.execute(sql);
			
			// close connections
			st.close();
			con.close();
			
		}
		catch (SQLException e)
		{
			if (e.getErrorCode() == 30000)
			{
				errors += "Error adding car " + car.getVin() + "\nA car with that VIN already exists\n";
			}
			else
			{
			errors += "Error adding car " + car.getVin() + "\n" + e.getMessage() + "\n";
			}
		}
	}
	
	public static boolean delete(String vin)
	{
		// deletes a car from the database
		boolean output = false;

		clearErrors();
		try
		{
			// establish connection
			con = DriverManager.getConnection(URL, username, password);
			
			// create a statement object
			Statement st = con.createStatement();
			
			// SQL command
			String sql = "DELETE FROM CAR WHERE VIN = '" + vin + "'";
			
			st.execute(sql);
			
			if (st.getUpdateCount() < 1)
			{
				errors += "No car with vin: " + vin + " in database\n";
			}
			else 
				output = true;
			
			// close connections
			st.close();
			con.close();
			
		}
		catch (Exception e)
		{
			errors += "Error deleting car with vin: " + vin + "\n" + e.getMessage() + "\n";
		}
		
		return output;
		
	}
	
	public static boolean update(Car car)
	{
		// updates a car in the database
		// outputs true if a record was updated 
		
		boolean output = false;

		clearErrors();
		try
		{
			// establish connection
			con = DriverManager.getConnection(URL, username, password);
			
			// create a statement object
			Statement st = con.createStatement();
			
			// SQL command
			String sql = "UPDATE CAR SET "
						+ "MAKE = '" + car.getMake() + "', "
						+ "MODEL = '" + car.getModel() + "', "
						+ "MODELYEAR = " + car.getYear() + " "
						+ "WHERE VIN = '" + car.getVin() + "'";
			
			st.execute(sql);
			
			if (st.getUpdateCount() < 1)
			{
				errors += "No car with vin: " + car.getVin() + " to update\n";
			}
			else 
				output = true;
			
			// close connections
			st.close();
			con.close();
			
		}
		catch (Exception e)
		{
			errors += "Error adding car " + car.getVin() + "\n" + e.getMessage() + "\n";
		}
		
		return output;
		
	}
	
	public static Car select(String aVin)
	{
		// selects a car from the DB by vin, if found returns a car object
		// otherwise returns null
		
		clearErrors();
		Car output = null;
		try
		{
			// establish connection
			con = DriverManager.getConnection(URL, username, password);
			
			// create a statement object
			Statement st = con.createStatement();
			
			// SQL command
			String sql = "SELECT * FROM CAR "
						+ "WHERE VIN = '" + aVin + "' ";
			
			ResultSet result = st.executeQuery(sql);
			
			if (result.next())
			{
				output = new Car(aVin);
				output.setMake(result.getString("MAKE"));
				output.setModel(result.getString("MODEL"));
				output.setYear(result.getInt("MODELYEAR"));
			}
			else
			{
				output = null;
				errors += "No car with vin: " + aVin + " found\n";
			}
			
			// close connections
			st.close();
			con.close();
			
		}
		catch (Exception e)
		{
			errors += "Error selecting car " + aVin + "\n" + e.getMessage() + "\n";
		}
		
		return output;
	}
	
	
	public static String getErrors()
	{
		return errors;
	}
	
	public static void setErrors(String errors)
	{
		CarFaxDB.errors = errors;
	}
	
	public static void clearErrors()
	{
		CarFaxDB.errors = "";
	}
	
}
