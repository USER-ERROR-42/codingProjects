
public class Inventory
{
	// stores up to 3 artifacts
	// 
	// Note: I should have update the room objects to use the inventory class
	// to store its objects, but I didn't want to bother with
	
	Artifact[]	inventory;
	int			numArtifacts;
	
	Inventory()
	{
		this.inventory = new Artifact[3];
		numArtifacts = 0;
	}
	
	public Artifact selectArtifact(String name)
	{
		// checks if an artifact with a given name is in inventory
		// If so returns the artifact, else returns null
		Artifact art;
		for (int index = 0; index <= inventory.length; index++)
		{
			art = inventory[index];
			
			if (art != null)
				if (art.name.equals(name) || art.altName.equals(name))
					return art;
		}
		
		return null;
	}
	
	public boolean addArtifact(Artifact art)
	// puts an artifact in inventory
	// If there's an empty slot returns true, else false
	{
		if (numArtifacts >= 3)
		{
			return false;
		}
		else
		{
			for (int i = 0; i <= inventory.length; i++)
			{
				if (inventory[i] == null)
				{
					inventory[i] = art;
					numArtifacts++;
					return true;
				}
			}
		}
		return false;
	}
	
	public Artifact removeArtifact(String name)
	// removes an artifact from inventory
	// returns the artifact that it removed
	{
		Artifact art;
		for (int index = 0; index <= inventory.length; index++)
		{
			art = inventory[index];
			
			if (art != null)
				if (art.name.equals(name) || art.altName.equals(name))
				{
					inventory[index] = null;
					return art;
				}
		}
		
		return null;
	}
	
	public void listInventory()
	// lists the names of the items in inventory
	{
		String[] names = new String[3];
		int numNames = 0;
		Artifact art;
		for (int index = 0; index < inventory.length; index++)
		{
			art = inventory[index];
			if (art != null)
			{
				names[numNames] = art.displayName;
				numNames ++;
			}
		}
		String text;
		if (numNames == 0)
		{
			Functions.printLongText("Your inventory is as empty as your head.");
		}
		else
		{
			text = "In your inventory you have: \n";
			
			for (int i = 0; i < numNames; i++)
			{
				text += ("\t" + names[i] + "\n");
			}
			System.out.print(text);
		}
		
	}
}
