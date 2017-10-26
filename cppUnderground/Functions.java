
public class Functions
// contains some common functions
{
	// Min number of characters to put on one line
	static final int CHARACTER_LIMIT = 50;
	
	static void printLongText(String text)
	// takes a long string of text and prints it in chunks, adding new lines as
	// needed
	
	// I really should have added the descriptions with the new line characters
	// already
	// or at least output them with newlines into another file, but I'm already
	// gonna
	// spent way to long on this piece of nonsense.
	{
		String[] words = text.split(" ");
		int len = words.length;
		int index = 0;
		int characters;
		
		// go through the text array
		while (index < len)
		{
			characters = 0;
			
			// go through word by word until we reach the character limit
			while (characters < CHARACTER_LIMIT && index < len)
			{
				System.out.print(words[index] + " ");
				characters += words[index].length() + 1;
				index++;
			}
			
			System.out.println();
		}
	}
	
	static void printLongTextNoNewline(String text)
	// takes a long string of text and prints it in chunks, adding new lines as
	// needed but no newline at the end
	
	// I really should have added the descriptions with the new line characters
	// already
	// or at least output them with newlines into another file, but I'm already
	// gonna
	// spent way to long on this piece of nonsense.
	{
		String[] words = text.split(" ");
		int len = words.length;
		int index = 0;
		int characters;
		
		// go through the text array
		while (index < len)
		{
			characters = 0;
			
			// go through word by word until we reach the character limit
			while (characters < CHARACTER_LIMIT && index < len)
			{
				System.out.print(words[index] + " ");
				characters += words[index].length() + 1;
				index++;
			}
			
			if (index < len)
			{
				System.out.println();
			}
		}
	}
	
	public static String addInputArray(String [] inputArray, int start)
	// returns the rest of the input Array starting at the specified position
	{
		String output = "";
		String word = "";
		for (int i = start; i < inputArray.length; i++)
		{
			word = inputArray[i];
			if (word != null)
			{
				if (i != start)
				{
				output += " ";
				}
				output += (word);
			}
			else
				break;
		}
		
		return output;
	}
	
	
	@SuppressWarnings("unused")
	private static void codeStorage()
	{
		Artifact art;
		Room rom = new Room(5, 5, 5);
		
		art = new SpecialArtifact(/* Name */ "NAME", /* AltName */ "ALT_NAME", /* YouFoundText */"YOU_FOUND_TEXT",
				/* OffDescript */ "OFF_DESCRIPTION", /* TurningOn */ "TURNING_ON", /* OnDescript */ "ON_DESCRIPTION",
				/* TurningOff */ "TURNING_OFF", /* TurnsOff? */ true);
		rom.addArtifact(art);
		
		art = new Artifact(/* Name */ "NAME", /* Description */ "DECRIPTION", /* YouFoundText */"YOU_FOUND_TEXT",
				/* TouchText */ "TOUCH_TEXT", /* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
	}
	
	
}
