import java.io.*;

public class Artifact
// Its a thing that goes in a room
// It does nothing but have a name and a description
{
	int		ID; // Note that ID is not manually assigned
	String	name;
	String	altName;
	String	displayName;
	String	youFoundText;	// Lists this text when examining a room
	String	description;	// Prints when examining an object
	String	touchText;		// prints when attempting to touch a normal artifact
	String 	pickUpText;		
	String 	dropText;		
	
	
	boolean	isVisible;
	boolean	glowing;
	boolean pickUp;
	
	Artifact [] list;
	
	Artifact()
	{
		this.ID = UndergroundApp.artifactCount;
		UndergroundApp.artifactMasterList[this.ID] = this;
		UndergroundApp.artifactCount ++;
	}
	
	Artifact(String name)
	{
		this(name, "DEFAULT_DESCRIPTION");
	}
	
	Artifact(String name, String description)
	{
		this();
		this.name = name;
		this.description = description;
		this.isVisible = true;
		this.glowing = false;
		this.pickUp = false;
	}
	
	Artifact(String name, String description, String youFoundText, String touchText, String altName)
	{
		this(name, description);
		this.isVisible = true;
		this.glowing = false;
		this.youFoundText = youFoundText;
		this.touchText = touchText;
		this.altName = altName;
		// TODO make this add the artifact to an artifact list as well
	}
	
	void printDescription()
	{
		Functions.printLongText(this.description);
	}
	
	boolean isSpecial()
	{
		return false;
	}
	
	void touch()
	{
		// on a normal object only prints the touch text
		Functions.printLongText(this.touchText);
	}
	
	
	void turnOn()
	{
	}
	
	void turnOff()
	{
	}
	
	void turnOnOff()
	{
	}
	
	public boolean isCarryable ()
	{
		return pickUp;
	}
	
	public void makeCarryable (String displayName, String pickUpText, String dropText)
	// makes an artifact carryable, but requires a display name, pickup flavor text, and drop text
	{
		pickUp = true;
		this.displayName = displayName;
		this.pickUpText = pickUpText;
		this.dropText = dropText;
	}
	
	
	
}
