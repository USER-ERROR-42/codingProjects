/* This is a program I wrote for the database course at Cal Poly Pomona
 * Prof Carlton gave us several badly made excel sheets
 * He wanted us to create SQL insert statements from them
 * I decided that was too easy so I wrote this program
 * It can take arbitrary csv sheets and properly configured output specifications
 * And spit out insert statements
 * It took a week to make, when manually it would have taken an hour max
 * 
 * Here's to over-complicated solutions,
 * Kyle Tejada
 */
 

import java.io.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

public class DataApp
{
	String[][] text;
	
	// Table definition arrays
	String[]	Customer;
	String[]	Order;
	String[]	OrderLine;
	String[]	Product;
	String[]	Fabricated;
	String[]	ProductSupplier;
	String[]	Supplier;
	String[]	Employee;
	String[]	EmployeeCourse;
	String[]	Course;
	String[]	SalesRep;
	
	String[]	PriceHistory;
	
	// Data Tables
	String[][]	dCustomer;
	String[][]	dOrder;
	String[][]	dOrderLine;
	String[][]	dProduct;
	String[][]	dFabricated;
	String[][]	dProductSupplier;
	String[][]	dSupplier;
	String[][]	dEmployee;
	String[][]	dEmployeeCourse;
	String[][]	dCourse;
	String[][]	dSalesRep;
	
	String[][]  dPriceHistory;
	
	String[]	tableNames;
	String[][][] dataTables;
	
	HashMap<String, String> states = new HashMap<String, String>();

	// define price history dates
	String[][] priceHistoryDates = {
				{"1/1/2016","3/31/2016"},
				{"4/1/2016","5/20/2016"},
				{"5/21/2016","5/29/2016"},
				{"5/30/2016","7/07/2016"},
				{"7/08/2016","11/25/2016"},
				{"11/26/2016","12/31/2017"}
									};
	// Define price increases each period (as decimals ie 5% = 0.05)
	double[] priceIncreases = {0.0, 0.04, 0.05, -0.10, 0.10, 0.04};
	
	public static void main(String[] args)
	{
		// This App takes text files in a CVS format (separated by semicolons)
		// It outputs SQL INSERT INTO Statements to a new text file
		
		new DataApp();
		
	}
	
	public DataApp()
	{
		
		String whichFile = JOptionPane.showInputDialog(null, "Enter \nE for employee table\nS for supplier table\nO for order table\nP for price history table");
		loadTableDefinitionArrays();
		
		if (whichFile != null)
		{
			whichFile = whichFile.toUpperCase().trim();
		}
		
		// ENTER THE PATH HERE
		
		String sysPath = "C:\\Users\\kart5\\Desktop\\Project 3 Database\\";
		String filePath = "";
		
		switch (whichFile)
		{
			case "E":
				filePath = "Employee.csv";
				break;
			case "O":
				filePath = "Order.csv";
				break;
			case "S":
				filePath = "Supplier.csv";
				break;
			case "P":
				filePath = "Supplier.csv";
				break;
			default:
				filePath = "Employee.csv";
				break;
		}
		
		String path = sysPath + filePath;
		// JOptionPane.showMessageDialog(null, path);
		
		File file = new File(path);

		
		FileReader fr = null;
		BufferedReader br = null;
		
		try
		{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// Make Array of fields (separated by semicolons ;)
		// Inside Array of rows
		
		text = new String[50][];
		
		assignRowsToArray(br, text);
		
		populateStateMap(states);
		
		//printRows(text);
		
		switch (whichFile)
		{
			case "E":
				// Makes the Employee, EmployeeCourse, Course, and SalesRep Tables
				dEmployee = extractData(Employee);
				dEmployeeCourse = extractData(EmployeeCourse);
				dCourse = extractData(Course);
				dSalesRep = extractData(SalesRep);
				dSalesRep = ConvertPercents(dSalesRep);
				String[][][] dataE = { dEmployee, dEmployeeCourse, dCourse, dSalesRep };
				dataTables = dataE;
				String [] e = {"EMPLOYEE_t","EMPLOYEE_COURSE_t","COURSE_t","SALES_REPRESENTATIVE_t"};
				tableNames = e;
				break;
			case "O":
				// Makes the Customer, Order, OrderLine, and Product Tables
				dCustomer = extractData(Customer);
				dOrder = extractData(Order);
				dOrderLine = extractData(OrderLine);
				dProduct = extractData(Product);
				String[][][] dataO = { dCustomer, dOrder, dOrderLine, dProduct };
				dataTables = dataO;;
				String [] o = {"CUSTOMER_t","ORDER_t","ORDER_LINE_t","PRODUCT_t"};
				tableNames = o;
				break;
			case "S":
				// Makes the Supplier and ProductSupplier Tables
				dSupplier = extractData(Supplier);
				dProductSupplier = extractData(ProductSupplier);
				String[][][] dataS = { dSupplier, dProductSupplier };
				dataTables = dataS;
				String [] s = {"SUPPLIER_t","PRODUCT_SUPPLIER_t"};
				tableNames = s;
				break;
			case "P":
				// Price History Table
				dPriceHistory = extractData(PriceHistory);
				dPriceHistory = priceHistory(dPriceHistory);
				String[][][] dataP = { dPriceHistory };
				dataTables = dataP;
				String [] p = {"PRICE_HISTORY_t"};
				tableNames = p;
				break;
			default:
				// Defaults to employee tables
				dEmployee = extractData(Employee);
				dEmployeeCourse = extractData(EmployeeCourse);
				dCourse = extractData(Course);
				dSalesRep = extractData(SalesRep);
				String[][][] dataE2 = { dEmployee, dEmployeeCourse, dCourse, dSalesRep };
				dataTables = dataE2;
				String [] e2 = {"EMPLOYEE_t","EMPLOYEE_COURSE_t","COURSE_t","SALES_REPRESENTATIVE_t"};
				tableNames = e2;
				break;
		}
		
		// Clean-up the data
		for (int i = 0; i < dataTables.length; i ++)
		{
			// Split Composite Attributes
			// Denoted by a # at the beginning of the template array's attribute name
			
			String [][] dataArray = dataTables[i];
			splitCompositeAttributes(dataArray);
			trimValues(dataArray);
			if (dSupplier != null)
				stateNamesToStateCodes(dSupplier);
			
			// Split 2 rows in same record
			// ie (commonID, ID1, desc1, ID2, desc2, ID3, desc3)
			// becomes 	(commonID, ID1, desc1)
			//			(commonID, ID2, desc2)
			//			(commonID, ID3, desc3)
			dataArray = splitMultiRecordRows(dataArray);
			
			// Now remove duplicate rows
			dataArray = removeDuplicateRows(dataArray);
			
			// Change dates from mm/dd/yyyy to ddMONyyyy format
			changeDates(dataArray);
			
			// Add single quotes for strings and removes commas for non-strings
			addSingleQuotes(dataArray);
			
			if (tableNames[i].equals("PRODUCT_t"))
				addRandomNumberColumn("StockQuantity", dataArray);
			
			// Check data is good
			printRows(dataArray);
			System.out.println("\nNext Table\n\n");

			dataTables[i] = dataArray;

		}

		for (int i1 = 0; i1 < dataTables.length; i1 ++)
		{
			File outputFile = null;
			FileWriter fw = null;
			BufferedWriter bw = null;
			PrintWriter pw = null;
			try 
			{
				outputFile = new File(sysPath + tableNames[i1] + ".txt");
				if (!outputFile.exists())
					outputFile.createNewFile();
				fw = new FileWriter(outputFile);
				bw = new BufferedWriter(fw);
				pw = new PrintWriter(bw);
				
				
			}
			catch (Exception e)
			{}

			String output = "";
			output = createTheBloodySQL(dataTables[i1], tableNames[i1]);
			System.out.println(output);
		
			
			// Finally
			try
			{
				pw.write(output);
				pw.close();
				bw.close();
				fw.close();
			}
			catch (Exception e)
			{
				
			}
		
		}
		
	}
	


	private String[][] priceHistory(String[][] inputArray)
	{
		// takes the input array and changes the 2nd and third columns to be the dates specified at top
		// TODO finish
		String [][] output = {};
		ArrayList<String[]> outputList = new ArrayList<String[]>();
		String startDate = "";
		String endDate = "";
		double priceMultiplier = 0;
		
		for (int i1 = 0; i1 < priceHistoryDates.length; i1 ++)
		{
			startDate = priceHistoryDates[i1][0];
			endDate = priceHistoryDates[i1][1];
			priceMultiplier = 1 + priceIncreases[i1];
			
			String [] row = {};
			for (int i2 = 0; i2 < inputArray.length; i2 ++)
			{
				row = inputArray[i2];
				if (i2 == 0) // the definition row is only at the top
				{
					if (i1 == 0)
						outputList.add(row);
					continue;
				}
				double oldPrice = Double.parseDouble(row[3]);
				double newPrice = oldPrice * priceMultiplier;
				String[] newRow = {row[0], startDate, endDate, String.valueOf(newPrice)};
				outputList.add(newRow);
				
			}
		}
		output = outputList.toArray(output);
		return output;
	}

	private void addRandomNumberColumn(String name, String[][] table)
	{
		// adds a new column with the specified name
		// the values are random numbers between 1 and 666666
		
		String[] row = null;
		Random rand = new Random();
		for (int i = 0; i < table.length; i ++)
		{
			row = Arrays.copyOf(table[i], table[i].length + 1);
			if (i == 0)
				row [row.length - 1] = name;
			else if (i == 1)
				row [row.length - 1] = "666666";
			else 
			{
				row [row.length - 1] = String.valueOf(rand.nextInt(6667));
			}	
			
			table[i] = row;
		}
		
	}

	private String[][] ConvertPercents(String[][] table)
	{
		// Converts percentages to decimals
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(table[0]);
		
		String[] row = null;
		String value = null;
		for (int i = 1; i < table.length; i ++)
		{
			row = table[i];
			// HARD CODED COLUMN VALUE
			value = row[1];
			if (value.equals(""))
				continue;
			
			value = value.substring(0, value.length() - 1);
			float n = Float.parseFloat(value);
			n /= 100;
			value = String.valueOf(n);
			row[1] = value;
			
			list.add(row);
		}
		String [][] output = {};
		output = list.toArray(output);
		return output;
	}

	private void addSingleQuotes(String[][] dataArray)
	{
		// adds single quotes to attribute names that end with '
		// also adds another single quote in front of already existing ones
		// so SQL plays nice with it
		ArrayList<Integer> indexesOfStrings = new ArrayList<Integer>();
		ArrayList<Integer> indexesOfNonStrings = new ArrayList<Integer>();
		String name = null;
		for (int i = 0; i < dataArray[0].length; i++)
		{
			name = dataArray[0][i];
			if (name.endsWith("'"))
			{
				indexesOfStrings.add(i);
				dataArray[0][i] = name.substring(0, name.length() - 1);
			}
			else
			{
				indexesOfNonStrings.add(i);
			}
		}
		
		String[] row = null;
		for (int i = 1; i < dataArray.length; i ++)
		{
			row = dataArray[i];
			// Adds single quotes for the strings
			for(int index : indexesOfStrings)
			{
				if (dataArray[i][index].equals(""))
					continue;
				else
				{
					// double up on single quotes
					String word = dataArray[i][index];
					if (word.contains("'"))
						word = doubleChar(word, "'".charAt(0));
					
					
					// add single quotes around it
					dataArray[i][index] = "'" + word +"'";
				}
			}
			
			// remove commas for the non-strings
			String value = null;
			String [] noCommasArray = null;
			for(int index : indexesOfNonStrings)
			{
				value = dataArray[i][index];
				noCommasArray = value.split(",");
				value = ArrayToString(noCommasArray);
				dataArray[i][index] = value;
			}
		}
	}

	private String replaceChar(String word, char char1, char char2)
	{
		// replaces any occurrences in word of char1 with char 2
		String output = "";
		char letter;
		for (int i = 0; i < word.length(); i ++)
		{
			letter = word.charAt(i);
			if (letter == char1)
				letter = char2;
			output += letter;
		}
		
		return output;
	}

	private String doubleChar(String word, char letter)
	{
		// goes through the string and adds another char immediately
		// after any occurrences of the specified character
		String output = "";
		for (int i = 0; i < word.length(); i ++)
		{
			output += word.substring(i, i + 1);
			if (word.charAt(i) == letter)
				output += word.substring(i, i + 1);
		}
		
		return output;
	}


	

	private String ArrayToString(String[] array)
	{
		// takes a string array and returns it combined as a string
		// no whitespace is added
		String output = "";
		String word = "";
		for (int i = 0; i < array.length; i ++)
		{

			
			word = array[i];
			output += word;
		}
		return output;
	}

	private void changeDates(String[][] dataArray)
	{
		// changes all attributes that have "date" in their attribute name
		
		ArrayList<Integer> indexesToChange = new ArrayList<Integer>();
		
		String name = "";
		for (int i = 0; i < dataArray[0].length; i ++)
		{
			name = dataArray[0][i].toLowerCase();
			if (name.contains("date"))
				indexesToChange.add(i);
		}
		
		for (int i = 1; i < dataArray.length; i ++)
		{
			String [] row = dataArray[i];
			String value = null;
			for (int index : indexesToChange)
			{
				value = row[index];
				if (value == null || value.equals(""))
				{
					value = "";
				}
				else
				{
					String [] date = value.split("/");
					int month = Integer.parseInt(date[0]);
					int day = Integer.parseInt(date[1]);
					int year = Integer.parseInt(date[2]);
					
					String sYear = null;
					if (year < 100)
						sYear = "20" + String.valueOf(year) ;
					else
						sYear = String.valueOf(year);
					
					String sDay = null;
					if (day < 10)
						sDay = "0" + String.valueOf(day);
					else 
						sDay = String.valueOf(day);
					
					String sMonth = null;
					switch (month)
					{
						case 1:
							sMonth = "JAN";
							break;
						case 2:
							sMonth = "FEB";
							break;
						case 3:
							sMonth = "MAR";
							break;
						case 4:
							sMonth = "APR";
							break;
						case 5:
							sMonth = "MAY";
							break;
						case 6:
							sMonth = "JUN";
							break;
						case 7:
							sMonth = "JUL";
							break;
						case 8:
							sMonth = "AUG";
							break;
						case 9:
							sMonth = "SEP";
							break;
						case 10:
							sMonth = "OCT";
							break;
						case 11:
							sMonth = "NOV";
							break;
						case 12:
							sMonth = "DEC";
							break;
					}
					row[index] = sDay + sMonth + sYear;
					
				}
			}
		}
		
	}

	private String createTheBloodySQL(String[][] table, String tableName)
	{
		// Takes a 2D String array and creates SQL INSERT INTO statements
		// uses the attributes names in the first row as the column names
		String output = "";
		
		output += "INSERT INTO " + tableName + "\n";
		output += "(";
		
		// list attribute names
		for (int i = 0; i < table[0].length; i ++)
		{
			if (i != 0)
				output += ", ";
			
			output += table[0][i];
		}
		
		output += ")\n";
		output += "VALUES \n";
		
		// list record values, starting on 2nd row (1st was attribute names)
		for (int i1 = 1; i1 < table.length; i1 ++)
		{
			output += "(";
			
			String value = "";
			for (int i2 = 0; i2 < table[i1].length; i2 ++)
			{
				if (i2 != 0)
				{
					output += ", ";
				}
				value = table[i1][i2];
				if (! value.equals(""))
				{
					output += value;
				}
				else
				{
					output += "null";
				}
			}
			
			output += ")";
			if (i1 != table.length - 1)
				output += ",";
			output += "\n";
		}
		output += "\nSELECT *\n";
		output += "FROM " + tableName + "\n";
		
		return output;
	}

	private String[][] removeDuplicateRows(String[][] input)
	{
		// removes duplicate rows
		String[][] output = {};
		ArrayList<String[]> data = new ArrayList<String[]>();
		for (String[] inputRow : input)
		{
			if (inputRow == null)
				break;
			boolean rowUnique = true;
			for (String[] outputRow : data)
			{
				if (outputRow == null)
					break;
				boolean equal = true;
				for (int i = 0; i < outputRow.length; i++)
				{
					if (!inputRow[i].equals(outputRow[i]))
					{
						equal = false;
						break;
					}
				}
				if (equal)
				{
					rowUnique = false;
					break;
				}
			}
			if (rowUnique)
			{
				data.add(inputRow);
			}
			
		}
		output = data.toArray(output);
		return output;
	}
	
	private void trimValues(String[][] dataArray)
	{
		// trims the values
		for (String[] row : dataArray)
		{
			for (int i = 0; i < row.length; i++)
			{
				try
				{
					row[i] = row[i].trim();
				}
				catch (Exception e)
				{
				}
			}
		}
	}
	
	private void populateStateMap(HashMap<String, String> s)
	{
		// loads the state hashmap with state names as keys and state codes as values
		
		s.put("alaska", "AK");
		s.put("alabama", "AL");
		s.put("arkansas", "AR");
		s.put("arizona", "AZ");
		s.put("california", "CA");
		s.put("colorado", "CO");
		s.put("connecticut", "CT");
		s.put("delaware", "DE");
		s.put("florida", "FL");
		s.put("georgia", "GA");
		s.put("hawaii", "HI");
		s.put("iowa", "IA");
		s.put("idaho", "ID");
		s.put("illinois", "IL");
		s.put("indiana", "IN");
		s.put("kansas", "KS");
		s.put("kentucky", "KY");
		s.put("louisiana", "LA");
		s.put("massachusetts", "MA");
		s.put("maryland", "MD");
		s.put("maine", "ME");
		s.put("michigan", "MI");
		s.put("minnesota", "MN");
		s.put("missouri", "MO");
		s.put("mississippi", "MS");
		s.put("montana", "MT");
		s.put("nebraska", "NE");
		s.put("north carolina", "NC");
		s.put("north dakota", "ND");
		s.put("new hampshire", "NH");
		s.put("new jersey", "NJ");
		s.put("new mexico", "NM");
		s.put("new york", "NY");
		s.put("nevada", "NV");
		s.put("ohio", "OH");
		s.put("oklahoma", "OK");
		s.put("oregon", "OR");
		s.put("pennsylvania", "PA");
		s.put("puerto rico", "PR");
		s.put("rhode island", "RI");
		s.put("south carolina", "SC");
		s.put("south dakota", "SD");
		s.put("tennessee", "TN");
		s.put("texas", "TX");
		s.put("utah", "UT");
		s.put("virginia", "VA");
		s.put("virgin islands", "VI");
		s.put("vermont", "VT");
		s.put("washington", "WA");
		s.put("wisconsin", "WI");
		s.put("west virginia", "WV");
		s.put("wyoming", "WY");
	}
	
	private void stateNamesToStateCodes(String[][] data)
	{
		// Checks the first row for an attribute containing "state" (not case sensitive)
		// If it finds one it looks through that column, replacing state names with their 
		// 2 letter codes. ie California -> CA, Georgia -> GA
		
		int index = -1;
		String attribute = "";
		// loop through first row looking for a name that contains "state"
		for (int i = 0; i < data[0].length; i++)
		{
			if (data[0][i] == null)
				continue;
			
			attribute = data[0][i].trim().toLowerCase();
			if (attribute.contains("state"))
			{
				index = i;
				break;
			}
		}
		// If found, loop through that column changing state names to state codes
		if (index >= 0)
		{
			String name = "";
			for (String[] row : data)
			{
				name = row[index];
				name = isStateName(name);
				row[index] = name;
			}
		}
	}
	
	private String isStateName(String input)
	{
		// takes a string and if it's a state name returns its corresponding state code
		// otherwise returns the input string
		String name = input.toLowerCase();
		
		if (states.containsKey(name))
		{
			name = states.get(name);
			return name;
		}
		
		return input;
	}
	
	private String[][] splitMultiRecordRows(String[][] data)
	{
		// any attributes that begin with a number denote repeat attributes
		// that belong in a separate record
		// all other attributes (that don't start with a number) are considered common
		// and given to each new record
		// ie (commonID, 1ID, 1desc, 2ID, 2desc, 3ID, 3desc)
		// becomes 	 1	(commonID, ID, desc)
		//			 2	(commonID, ID, desc)
		//			 3	(commonID, ID, desc)
		// Note supports max 100 (0-9) new records in each row
		// Note all records must have the same row names (same # too)
		
		ArrayList<Integer> commonIndexes = new ArrayList<Integer>();
		HashMap<String, ArrayList<Integer>> diffIndexes = new HashMap<String, ArrayList<Integer>>();
		// The arrayLists contain all of the indexes for a single record
		
		String[][] finalOutput = data;
		String name = "";
		char recordNumber;
		String keyString = "";
		for (int i = 0; i < data[0].length; i++)
		// loop through first row, which contains the names
		{
			name = data[0][i];
			recordNumber = name.charAt(0);
			if (Character.isDigit(recordNumber))
			// if starts with a digit
			{
				keyString = Character.toString(recordNumber);
				if (diffIndexes.containsKey(keyString))
				// if we already have that key add the index to the list
				{
					diffIndexes.get(keyString).add(i);
				}
				else // create new key
				{
					diffIndexes.put(keyString, new ArrayList<Integer>());
					diffIndexes.get(keyString).add(i);
				}
				data[0][i] = name.substring(1); // remove the starting number
			}
			else // no number so it's common
			{
				commonIndexes.add(i);
			}
		}
		
		// Now we have all the common and unique indexes
		
		if (diffIndexes.size() > 0)
		// If there are multiple records in this row
		
		// get all common values
		// now get first record's uniques and create new row (in a new table)
		// If all uniques are empty or null then don't create row
		// get next records and create new rows
		// then move to next row 
		{
			int numUniqueFields = diffIndexes.get(keyString).size();
			int numCommonFields = commonIndexes.size();
			int totalNewFields = numCommonFields + numUniqueFields;
			ArrayList<String[]> output = new ArrayList<String[]>();
			String[] row = null;
			for (int i = 0; i < data.length; i++)
			// Loop through the input array
			{
				row = data[i];
				
				HashMap<Integer, String> commonValues = getValuesMap(commonIndexes, row);
				HashMap<Integer, String> uniqueValues = null;
				
				Collection<ArrayList<Integer>> diffIndexesCollection = diffIndexes.values();
				Iterator<ArrayList<Integer>> iterator = diffIndexesCollection.iterator();
				ArrayList<Integer> currentDiffIndexes = null;
				HashMap<Integer, String> newMap = new HashMap<Integer, String>();
				String[] newRow = null;
				int count = 0;
				while (iterator.hasNext())
				// Once for each new record to be created
				{
					currentDiffIndexes = iterator.next();
					if (checkValuesNotEmptyOrNull(row, currentDiffIndexes))
					{
						if (i == 0 && count > 0)
							// The first row of attribute names is only needed once
							break;
						
						uniqueValues = getValuesMap(currentDiffIndexes, row);
						newMap.putAll(commonValues);
						newMap.putAll(uniqueValues);
						newRow = getOrderedValues(newMap);
						newMap.clear();
						if (newRow != null)
							output.add(newRow);
					}
					count++;
				}
			}
			if (output != null)
			{
				finalOutput = output.toArray(finalOutput);
			}
		}
		return finalOutput;
	}
	
	private String[] getOrderedValues(HashMap<Integer, String> map)
	{
		// takes a hashmap and orders its list of keys
		// then adds the corresponding values (in order) to an array, returning the array
		Set<Integer> keySet = map.keySet();
		Integer[] keys = {};
		keys = keySet.toArray(keys);
		Arrays.sort(keys);
		String[] output = new String[keys.length];
		for (int i = 0; i < keys.length; i++)
		{
			output[i] = map.get(keys[i]);
		}
		return output;
	}
	
	private String[] appendArray(String[] arr1, String[] arr2)
	{
		// adds two string arrays together returns the resulting array
		String[] output = new String[arr1.length + arr2.length];
		int outputIndex = 0;
		for (String element : arr1)
		{
			output[outputIndex] = element;
			outputIndex++;
		}
		for (String element : arr1)
		{
			output[outputIndex] = element;
			outputIndex++;
		}
		return output;
	}
	
	private HashMap<Integer, String> getValuesMap(ArrayList<Integer> commonIndexes, String[] row)
	{
		// takes an ArrayList of indexes and returns a hashmap linking indexes to their corresponding values
		// as taken from row
		HashMap<Integer, String> output = new HashMap<Integer, String>();
		
		for (int i = 0; i < commonIndexes.size(); i++)
		{
			output.put(commonIndexes.get(i), row[commonIndexes.get(i)]);
		}
		
		return output;
	}
	
	private boolean checkValuesNotEmptyOrNull(String[] row, ArrayList<Integer> indexes)
	{
		// loops through the indexes in the 2nd parameter 
		// if all the corresponding values in row are null or empty Strings returns false
		// if at least one is non null and non empty String returns true
		for (int i : indexes)
		{
			if (row[i] != null && !row[i].equals(""))
				return true;
		}
		
		return false;
	}
	
	private void splitCompositeAttributes(String[][] dataArray)
	{
		// looks through the first row of the passed 2D Array for one beginning with a #
		// If it finds a # it checks the next character, which will be used as the delimiter
		// It removes both from the attribute name
		// It splits each value in that column using the delimiter
		// It then adds extra fields to the end of the array
		// Note only does one such attribute, if an array contains multiples call this method again
		
		String[] templateArray = dataArray[0];
		int indexToSplit = -1;
		int numberColumnsToAdd = -1;
		String word = "";
		String delimiter = "";
		for (int i = 0; i < templateArray.length; i++)
		{
			word = templateArray[i];
			if (word.charAt(0) == '#')
			{
				delimiter = Character.toString(word.charAt(1));
				indexToSplit = i;
				templateArray[i] = word.substring(2);
				break;
			}
		}
		if (indexToSplit >= 0)
		{
			String[] test = templateArray[indexToSplit].split(delimiter, -1);
			numberColumnsToAdd = test.length - 1;
		}
		if (numberColumnsToAdd >= 1)
		{
			// Loop through the rows, adding columns 
			String[] row;
			String[] split;
			int originalLength;
			for (int i = 0; i < dataArray.length; i++)
			{
				originalLength = dataArray[i].length;
				row = Arrays.copyOf(dataArray[i], dataArray[i].length + numberColumnsToAdd);
				split = row[indexToSplit].split(delimiter, -1);
				row[indexToSplit] = split[0];
				
				// for each value in split, add to the end of row
				// i3 is row's index from highest to lowest
				int i3 = row.length - 1;
				for (int i2 = 1; i2 < split.length; i2++)
				{
					row[i3] = split[i2];
					i3--;
				}
				dataArray[i] = row;
			}
		}
	}
	
	private String[][] extractData(String[] template)
	{
		// goes through each row in the table, extracting fields given by the parameter array
		
		ArrayList<String[]> rows = new ArrayList<String[]>();
		String[] line;
		
		// These loop through the 2D text array, removing unneeded values
		
		for (int i1 = 1; i1 < text.length; i1++)
		// Go through each line, skipping the first line
		{
			if ((text[i1]) == null)
				break;
			
			line = text[i1].clone();
			
			if (line.equals(""))
				break;
			
			// otherwise
			for (int i2 = 0; i2 < line.length; i2++)
			{
				if (i2 != 0 && isNull(line[i2]))
					break;
				
				//otherwise
				if (isNull(template[i2]))
				// If the template says we DO NOT need this column's value
				{
					line[i2] = null;
					// Get rid of it
				}
			}
			
			// Now line contains only the necessary values and a bunch of nulls
			// Also trim() the values
			line = removeNulls(line);
			rows.add(line);
		}
		
		// Now rows has arrays with the correct values and no nulls
		// Add a similarly no-null template array to the start of rows
		rows.add(0, removeNulls(template));
		
		String[][] outputArray = {};
		return (String[][]) rows.toArray(outputArray);
	}
	
	private String[] removeNulls(String[] line)
	{
		// takes an array and compresses it, removing all null values
		// also trims the non null values
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (String element : line)
		{
			if (notNull(element))
			{
				output.add(element.trim());
			}
		}
		String[] outputArray = {};
		return (String[]) output.toArray(outputArray);
	}
	
	private boolean notNull(String input)
	{
		if (input != null)
			return true;
		
		return false;
	}
	
	private boolean isNull(String input)
	{
		if (input == null)
			return true;
		
		return false;
	}
	
	private void loadTableDefinitionArrays()
	{
		// adds the attributes and attribute names needed in each table
		// Note attributes ending in a ' indicate to add '' to the SQL statements
		
		// A number at the beginning of the name denotes multiple rows in the same spreadsheet row that must be split up
		// ex: 1firstName, 1lastName, 1dob, 2firstName, 2lastName, 2dob, 3firstName, 3lastName, 3dob
		// becomes 	1firstName, 1lastName, 1dob		(all  rows to be inputed separately)
		//			2firstName, 2lastName, 2dob
		//			3firstName, 3lastName, 3dob
		
		// A pound (#) at the beginning indicates multiple columns in the same row
		// ex: 1234 Main Street Los Angeles, CA 12345
		// is broken up into 
		// streetAddress: 	1234 Main Street
		// city: 			Los Angeles
		// state: 			California
		// zip: 			12345
		
		
		Customer = new String[30];
		Customer[0] = "CustomerID";
		Customer[1] = "CustomerName'";
		Customer[2] = "CustomerStreet'";
		Customer[3] = "CustomerCity'";
		Customer[4] = "CustomerState'";
		Customer[5] = "CustomerZip'";
		Customer[6] = "CreditLimit";
		Customer[7] = "SalesRepID";
		
		Order = new String[30];
		Order[8] = "OrderID";
		Order[0] = "CustomerID";
		Order[9] = "CustomerPONumber'";
		Order[10] = "OrderDate'";
		Order[11] = "DueDate'";
		Order[12] = "ShipDate'";
		
		OrderLine = new String[30];
		OrderLine[8] = "OrderID";
		OrderLine[13] = "1ProductID";
		OrderLine[15] = "1OrderQuantity";
		OrderLine[17] = "2ProductID";
		OrderLine[19] = "2OrderQuantity";
		OrderLine[21] = "3ProductID";
		OrderLine[23] = "3OrderQuantity";
		
		Product = new String[30];
		Product[13] = "1ProductID";
		Product[14] = "1ProductDescription'";
		Product[16] = "1UnitPrice";
		Product[17] = "2ProductID";
		Product[18] = "2ProductDescription'";
		Product[20] = "2UnitPrice";
		Product[21] = "3ProductID";
		Product[22] = "3ProductDescription'";
		Product[24] = "3UnitPrice";
		
		// Add another table template to pull products from the supplier table
		
		Fabricated = new String[30];
		
		ProductSupplier = new String[30];
		ProductSupplier[3] = "ProductID";
		ProductSupplier[0] = "SupplierID";
		ProductSupplier[4] = "VendorPartID'";
		ProductSupplier[6] = "ProductCost";
		ProductSupplier[7] = "PurchasedQuantity";
		
		// THE NEW PRICE HISTORY TABLE
		PriceHistory = new String[30];
		PriceHistory[3] = "ProductID";
		PriceHistory[6] = "StartDate'";
		PriceHistory[7] = "EndDate'";
		PriceHistory[8] = "UnitPrice";
		
		Supplier = new String[30];
		Supplier[0] = "SupplierID";
		Supplier[1] = "SupplierName'";
		Supplier[2] = "#,SupplierStreet', SupplierCity', SupplierState', SupplierZip'";
		
		Employee = new String[30];
		Employee[1] = "EmployeeID";
		Employee[2] = "# EmployeeFirstName' EmployeeLastName'";
		Employee[3] = "EmployeeJobTitle'";
		Employee[4] = "EmployeeHireDate'";
		Employee[5] = "EmployeeStreet'";
		Employee[6] = "EmployeeCity'";
		Employee[7] = "EmployeeState'";
		Employee[8] = "EmployeeZip'";
		Employee[18] = "ManagerID";
		
		EmployeeCourse = new String[30];
		EmployeeCourse[1] = "EmployeeID";
		EmployeeCourse[9] = "1CourseID";
		EmployeeCourse[11] = "1CompletionDate'";
		EmployeeCourse[12] = "2CourseID";
		EmployeeCourse[14] = "2CompletionDate'";
		EmployeeCourse[15] = "3CourseID";
		EmployeeCourse[17] = "3CompletionDate'";
		
		Course = new String[30];
		Course[9] = "1CourseID";
		Course[10] = "1CourseDescription'";
		Course[12] = "2CourseID";
		Course[13] = "2CourseDescription'";
		Course[15] = "3CourseID";
		Course[16] = "3CourseDescription'";
		
		SalesRep = new String[30];
		SalesRep[1] = "EmployeeID";
		SalesRep[19] = "CommissionRate";
		
	}
	
	private void printRows(String[][] text)
	{
		for (int i1 = 0; i1 < text.length; i1++)
		{
			if (text[i1] == null)
				break;
			String[] row = text[i1];
			
			if (row == null)
				break;
			
			for (String word : row)
			{
				if (word == null)
					word = "null";
				if (word.equals(""))
					word = " ";
				System.out.print(word + ";");
			}
			System.out.println();
		}
	}
	
	private void assignRowsToArray(BufferedReader br, String[][] text)
	{
		// adds the file's rows to the text array
		String row = "";
		
		for (int i1 = 0; i1 < text.length; i1++)
		{
			try
			{
				row = br.readLine();
				if (row == null)
				{
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			text[i1] = row.split(";", -1);
		}
	}
	
}
