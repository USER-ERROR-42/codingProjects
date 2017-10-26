
public class SpecialArtifact extends Artifact
{
	String	onDescription;
	String	turningOnDescription;
	String	turningOffDescription;
	String	glowingNoticeText;
	
	boolean	touchable;
	boolean	turnsOff;	// ie it stays on once turned on
	boolean	glowing;	// If true prints a description upon entering room. ie
						// You notice it immediately.
	boolean	on;
	
	
	SpecialArtifact(String name)
	{
		this(name, "DEFAULT_DESCRIPTION");
	}
	
	SpecialArtifact(String name, String description)
	{
		super(name, description);
		this.touchable = true;
	}
	
	SpecialArtifact(String name, String altName, String youFoundText, String offDescription,
			String turningOnDescription, String onDescription, String turningOffDescription, boolean turnsOff)
	{
		super(name, offDescription);
		this.altName = altName;
		this.youFoundText = youFoundText;
		this.turningOnDescription = turningOnDescription;
		this.onDescription = onDescription;
		this.turningOffDescription = turningOffDescription;
		this.glowingNoticeText = "";
		this.on = false;
		this.glowing = false;
		this.turnsOff = turnsOff;
		this.touchable = true;
	}
	
	boolean isSpecial()
	{
		return true;
	}
	
	void printDescription()
	// prints the on/off description depending on the state of the "on" bool
	{
		if (this.on)
			Functions.printLongText(this.onDescription);
		else
			Functions.printLongText(this.description);
	}
	
	void printGlowingText()
	{
		Functions.printLongText(glowingNoticeText);
	}
	
	void turnOn()
	// turns on the artifact and describes the transformation
	{
		this.turnOn(true);
	}
	
	void turnOn(boolean printDescription)
	{
		if (!this.on)
		{
			this.on = true;
			if (printDescription)
				Functions.printLongText(turningOnDescription);
		}
	}
	
	void turnOff()
	// turns off
	{
		if (this.on)
		{
			if (this.turnsOff)
				this.on = false;
			Functions.printLongText(turningOffDescription);
		}
	}
	
	void turnOnOff()
	// toggles the on/off state
	{
		if (this.on)
		{
			this.turnOff();
		}
		else
			this.turnOn();
	}
	
	void touch()
	// toggles the artifact's state
	{
		if (this.touchable)
			this.turnOnOff();
		else
			super.touch();
	}
}
