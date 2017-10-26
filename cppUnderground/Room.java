import java.io.*;

public class Room
{
	// Parameters
	
	int	row;
	int	col;
	int	floor;
	
	boolean	exitNorth;
	boolean	exitSouth;
	boolean	exitEast;
	boolean	exitWest;
	boolean	exitNortheast;
	boolean	exitSoutheast;
	boolean	exitSouthwest;
	boolean	exitNorthwest;
	boolean	exitUp;
	boolean	exitDown;
	
	String	name;
	String	description;
	String	youAreHerePhrase;	// ie "you find yourself standing in "
	
	// contains the artifact objects that can be found in this room
	Artifact[]	artifactList;
	// TODO make room's store artifacts in an inventory
	int			numArtifacts;
	
	
	
	// Constructor
	@SuppressWarnings("unused") // These are being used, I promise you Java
	Room(int row, int col, int floor)
	{
		this.row = row;
		this.col = col;
		this.floor = floor;
		
		this.exitNorth = false;
		this.exitSouth = false;
		this.exitEast = false;
		this.exitWest = false;
		this.exitNortheast = false;
		this.exitSoutheast = false;
		this.exitSouthwest = false;
		this.exitNorthwest = false;
		this.exitUp = false;
		this.exitDown = false;
		
		this.artifactList = new Artifact[3];
		this.numArtifacts = 0;
	}
	
	void addArtifact(Artifact item)
	// adds an artifact to the room's list
	// should only be used for initialization
	// placeArtifact is for player item placement
	{
		this.artifactList[numArtifacts] = item;
		numArtifacts++;
	}
	
	boolean placeArtifact (Artifact art)
	{
		// Places an artifact in the room
		// if successful returns true, else false
		
		if (numArtifacts >= 3)
		{
			return false;
		}
		else
		{
			for (int i = 0; i <= artifactList.length; i++)
			{
				if (artifactList[i] == null)
				{
					artifactList[i] = art;
					numArtifacts++;
					return true;
				}
			}
		}
		return false;
	}
	
	Artifact removeArtifact (String name, boolean[] carryable)
	{
		// TODO takes a name string
		// removes and returns that artifact if present
		// else returns null
		Artifact art;
		for (int index = 0; index < artifactList.length; index++)
		{
			art = artifactList[index];
			
			if (art != null)
				if (art.name.equals(name) || art.altName.equals(name))
				{
					if (art.isCarryable())
					{
						artifactList[index] = null;
						carryable[0] = true;
						return art;
					}
					else 
					{
						carryable[0] = false;
						return art;
					}
				}
		}
		
		return null;
	}
	
	void printYouAreHere()
	// calls the printLongText function with this room's description
	// which adds newlines as needed to the text
	{
		Functions.printLongText(youAreHerePhrase);
	}
	
	void printDescription()
	// calls the printLongText function with this room's description
	// which adds newlines as needed to the text
	{
		Functions.printLongText(description);
	}
	
	void printItems()
	// lists the items in this room
	{
		
		if (numArtifacts == 0)
		{
			Functions.printLongText("There doesn't seem to be anything worth examining here.");
		}
		else
		{
			int c = 0;
			for (Artifact a : this.artifactList)
			{
				if (a == null)
				{
					continue;
				}
				if (a.isVisible)
					Functions.printLongText(a.youFoundText);
				else if (a.isSpecial())
					if (a.glowing)
						((SpecialArtifact) a).printGlowingText(); 
				
				c++;
				if (c >= numArtifacts)
				{
					break;
				}
			}
		}
		
	}
	
	int countVisibleArtifacts()
	{
		int num = 0;
		int c = 0;
		for (Artifact a : artifactList)
		{
			if (a==null)
			{
				continue;
			}
			if (a.isVisible)
				num++;
			c++;
			if (c >= numArtifacts)
			{
				break;
			}
		}
		
		return num;
	}
	
	public Artifact selectArtifact(String name)
	{
		// takes a text input and checks whether that artifact is in the room
		// If so it returns that artifact, otherwise returns null
		int c = 0;
		for (Artifact item : artifactList)
		{
			if (item == null)
			{
				continue;
			}
			if (name.equals(item.name) || name.equals(item.altName))
			{
				return item;
			}
			c++;
			if (c >= numArtifacts)
			{
				break;
			}
		}
		
		return null;
	}
	
	public void printExits()
	{
		// Displays all the valid exits for this room
		String[] exits = new String[5];
		String[] exitStairs = new String[2];
		int num = 0;
		int numStairs = 0;
		
		
		// So my implementation of exits is not the best
		// Leads to some very repetitive code
		if (this.exitNorth)
		{
			exits[num] = "north";
			num++;
		}
		if (this.exitSouth)
		{
			exits[num] = "south";
			num++;
		}
		if (this.exitEast)
		{
			exits[num] = "east";
			num++;
		}
		if (this.exitWest)
		{
			exits[num] = "west";
			num++;
		}
		if (this.exitNortheast)
		{
			exits[num] = "northeast";
			num++;
		}
		if (this.exitSoutheast)
		{
			exits[num] = "southeast";
			num++;
		}
		if (this.exitSouthwest)
		{
			exits[num] = "southwest";
			num++;
		}
		if (this.exitNorthwest)
		{
			exits[num] = "northwest";
			num++;
		}
		if (this.exitUp)
		{
			exitStairs[numStairs] = "upwards";
			numStairs++;
		}
		if (this.exitDown)
		{
			exitStairs[numStairs] = "downwards";
			numStairs++;
		}
		
		if (num == 1)
		{
			Functions.printLongText("There is an exit to the " + exits[0] + ".");
		}
		else if (num == 2)
		{
			Functions.printLongText("There are exits to the " + exits[0] + " and " + exits[1] + ".");
		}
		else if (num == 3)
		{
			Functions.printLongText("There are exits to the " + exits[0] + ", " + exits[1] + ", and " + exits[2] + ".");
		}
		else if (num == 4)
		{
			Functions.printLongText("There are exits to the " + exits[0] + ", " + exits[1] + ", " + exits[2] + ", and "
					+ exits[3] + ".");
		}
		else if (num == 5) // limit of 5 exits per room
		{
			Functions.printLongText("There are exits to the " + exits[0] + ", " + exits[1] + ", " + exits[2] + ", "
					+ exits[3] + ", and " + exits[4] + ".");
		}
		
		if (numStairs > 0)
		{
			String stairs = "";
			if (num > 0)
			{
				stairs += "There is also ";
			}
			else
			{
				stairs += "There is ";
			}
			
			if (numStairs == 1)
			{
				stairs += "a flight of stairs leading " + exitStairs[0] + ".";
			}
			else
			{
				stairs += "a flight of stairs leading " + exitStairs[0] + " and another going " + exitStairs[1] + ".";
			}
			
			Functions.printLongText(stairs);
		}
		
		if (num == 0 && numStairs == 0)
		{
			Functions.printLongText(
					"There doesn't seem to be any way out of this room at the moment. But maybe if you looked around you'd found a way out. ");
		}
		
	}
	
	public void printGlowingItems()
	{
		int c = 0;
		for (Artifact a : artifactList)
		{
			if (a != null && a.glowing)
				c++;
			if (c >= numArtifacts)
			{
				break;
			}
		}
	}
	
	
	
}
