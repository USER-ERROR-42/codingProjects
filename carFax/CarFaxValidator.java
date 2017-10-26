package kptProject3;

public class CarFaxValidator
{
	// TODO make getValidInput functions that append an error message
	// TODO a la like they did in project 2
	
	private static String errors = "";
	
	public static String getValidVin(String vin)
	{
		// takes a string input, if its a valid vin returns the vin
		// otherwise returns an empty string
		String output = "";
		if (isValidVin(vin))
		{
			output = sanitize(vin);
		}
		else
		{
			output = "";
			errors += "Invalid Vin, must be 5 alphanumeric characters\n";
		}
		return output;
	}
	
	public static boolean isValidVin(String input)
	{
		// Checks whether the passed string is a valid vin number
		// For purposes of this program a valid vin is 5 alphanumeric characters 
		if (input.matches("\\w{5}"))
			return true;
		else
			return false;
	}
	
	public static String getValidMake(String make)
	{
		// takes a string input, if its a valid make returns the vin
		// otherwise returns an empty string
		String output = "";
		if (isValidMake(make))
		{
			output = sanitize(make);
		}
		else
		{
			output = "";
			errors += "Invalid Make, must be non-blank\n";
		}
		return output;
	}
	
	public static boolean isValidMake(String input)
	{
		// Checks whether the passed string is a valid make
		// For purposes of this program a valid make is a non-blank string
		if (! input.equals(""))
			return true;
		else
			return false;
	}
	
	public static String getValidModel(String model)
	{
		// takes a string input, if its a valid model returns the vin
		// otherwise returns an empty string
		String output = "";
		if (isValidModel(model))
		{
			output = sanitize(model);
		}
		else
		{
			output = "";
			errors += "Invalid Model, must be non-blank\n";
		}
		return output;
	}
	
	public static boolean isValidModel(String input)
	{
		// Checks whether the passed string is a valid model
		// For purposes of this program a valid model is a non-blank string
		if (! input.equals(""))
			return true;
		else
			return false;
	}
	
	public static boolean isValidYear(int input)
	{
		// Checks for a valid year
		// a valid year is a number between 1 and 9999, inclusive
		if (input >= 1 && input <= 9999)
			return true;
		else
			return false;
	}
	
	public static int getValidYear(String input)
	{
		// takes an input string and tries to parse it to be an int
		// if it parses and the integer is between 1 and 9999 inclusive, returns the int
		// if unsuccessful or the int is not in [1,9999] then returns 0
		
		int num = 0;
		try
		{
			num = Integer.parseInt(input.trim());
		}
		catch (Exception e)
		{}
		
		if (isValidYear(num))
			return num;
		

		errors += "Invalid Year, must be an integer between 1 and 9999\n";
		return 0;
		
	}
	
	private static String sanitize(String input)
	{
		// takes an input string and sanitizes it by doubling any single quotes
		// so that it will be safe to enter into a database
		
		return input.replace("'", "''");
	}
	
	public static void clearErrors()
	{
		errors = "";
	}
	
	public static String getErrors()
	{
		return errors;
	}
}
