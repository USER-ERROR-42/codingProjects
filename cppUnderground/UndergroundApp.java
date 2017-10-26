import java.util.*;
import java.io.*;

public class UndergroundApp
{
	// Yeah, I know I should't use global variables, but the instructions didn't
	// say anything about globals
	static boolean	gameOn		= true;
	static boolean	hasMoved	= true;
	
	static int	col		= 0;
	static int	row		= 0;
	static int	floor	= 1;// This is America where our floors start at 1
							// That being said, floor 0 is the basement
	
	static int			artifactCount		= 0;
	static Artifact[]	artifactMasterList	= new Artifact[100];
	
	static Map			map			= new Map();
	static Scanner		scan		= new Scanner(System.in);
	static Inventory	inventory	= new Inventory();
	
	// 100 total artifact limit
	
	// Flavor text variables
	static int touchCount = 0;
	
	public static void main(String[] args)
	{
		
		System.out.println("Welcome to the Great Cal Poly Underground!");
		
		// Main game loop
		while (gameOn)
		{
			// Print Room name and description
			if (hasMoved)
			{
				System.out.println();
				map.rooms[row][col][floor].printYouAreHere();
				// Print room's items
				map.rooms[row][col][floor].printGlowingItems();
				
				hasMoved = false;
			}
			
			// take user input and call appropriate command function
			commandCaller(givePrompt());
		}
		
	}
	
	static String givePrompt()
	// Prints the prompt and returns the input
	{
		System.out.print("\n> ");
		String input = scan.nextLine().trim().toLowerCase();
		System.out.println("");
		return input;
	}
	
	private static int commandCaller(String input)
	// takes the input, splits it into an array of strings
	// uses the first word to call the relevant game command
	// passes the array along to be used as parameters as needed
	{
		String[] inputArray = input.split(" ");
		if (inputArray[0].equals(""))
		{
			Functions.printLongText("I think you forgot to enter an input.");
			return 0; // how I decided to terminate function here
		}
		int numWords = inputArray.length;
		inputArray = removeArticles(inputArray);
		boolean has2 = false;
		boolean has3 = false;
		if (inputArray.length >= 2)
		{
			has2 = true;
		}
		if (inputArray.length >= 3)
			has3 = true;
		
		// contains all possible command words
		if (inputArray[0] == null)
		{
			badCommand(input, numWords);
			return 0;
		}
		switch (inputArray[0])
		{
			// quit commands
			case "quit":
				askToQuit();
				break;
			case "exit":
				askToQuit();
				break;
			
			// move commands
			case "move":
				move(inputArray);
				break;
			case "go":
				move(inputArray);
				break;
			case "walk":
				move(inputArray);
				break;
			case "head":
				move(inputArray);
				break;
			
			// North commands
			case "north":
				moveDirection("north");
				break;
			case "n":
				moveDirection("north");
				break;
			
			// East commands
			case "east":
				moveDirection("east");
				break;
			case "e":
				moveDirection("east");
				break;
			
			// South commands
			case "south":
				moveDirection("south");
				break;
			case "s":
				moveDirection("south");
				break;
			
			// West commands
			case "west":
				moveDirection("west");
				break;
			case "w":
				moveDirection("west");
				break;
			
			// Up commands
			case "up":
				moveDirection("up");
				break;
			case "upstairs":
				moveDirection("up");
				break;
			case "u":
				moveDirection("up");
				break;
			// Down commands
			case "down":
				moveDirection("down");
				break;
			case "downstairs":
				moveDirection("down");
				break;
			case "d":
				moveDirection("down");
				break;
			
			// Northeast commands
			case "northeast":
				moveDirection("northeast");
				break;
			case "ne":
				moveDirection("northeast");
				break;
			
			// Southeast commands
			case "southeast":
				moveDirection("southeast");
				break;
			case "se":
				moveDirection("southeast");
				break;
			
			// Southwest commands
			case "southwest":
				moveDirection("southwest");
				break;
			case "sw":
				moveDirection("southwest");
				break;
			
			// Northwest commands
			case "northwest":
				moveDirection("northwest");
				break;
			case "nw":
				moveDirection("northwest");
				break;
			
			// describe room commands
			case "look":
				look();
				break;
			case "observe":
				look();
				break;
			
			// Examine commands
			case "examine":
				examine(inputArray, has2, "examine");
				break;
			
			case "inspect":
				examine(inputArray, has2, "inspect");
				break;
			
			// Take artifact commands
			case "take":
				take(inputArray, has2, "take");
				break;
			
			case "get":
				take(inputArray, has2, "get");
				break;
			
			case "pick":
				take(inputArray, has2, "pick");
				break;
			
			case "pickup":
				take(inputArray, has2, "pickup");
				break;
			
			// Place artifact commands
			case "place":
				place(inputArray, has2, "place");
				break;
			
			case "put":
				place(inputArray, has2, "put");
				break;
			
			case "drop":
				place(inputArray, has2, "drop");
				break;
			
			// Check inventory commands
			case "check":
				check(inputArray, has2, "check");
				break;
			
			case "inventory":
				check(inputArray, has2, "inventory");
				break;
			
			case "backpack":
				check(inputArray, has2, "backpack");
				break;
			
			// TODO Inventory touch/ examine?
			
			// Where am I commands
			case "where":
				whereAmI();
				break;
			
			case "whereami":
				whereAmI();
				break;
			
			// Commit commands
			case "commit":
				commit(inputArray);
				break;
			
			// Touch commands
			case "touch":
				touch(inputArray, has2);
				break;
			
			case "feel":
				touch(inputArray, has2);
				break;
			
			case "use":
				touch(inputArray, has2);
				break;
			
			case "interact":
				touch(inputArray, has2);
				break;
			
			case "save":
				save(inputArray, has2, has3);
				break;
			
			case "load":
				load(inputArray, has2, has3);
				break;
			
			case "restore":
				load(inputArray, has2, has3);
				break;
				
			case "help":
				help();
				break;
			
			case "helpme":
				help();
				break;
			
			default:
				badCommand(input, numWords);
				break;
			
		}
		return 0; // I want to be able to return in the middle of the function
	}
	
	private static void help()
	{
		// Prints all of the possible commands with short descriptions
		// There are several commands uniquely mine
		// TODO Auto-generated method stub
		
	}

	private static void save(String[] inputArray, boolean has2, boolean has3)
	{
		// saves the game state in a user specified file
		if (has2)
		{
			if (!has3)
			{
				// Gets
				try
				{
					String path = "C:/Users/kart5/Desktop/CPP_Underground_V2/saves/";
					path += inputArray[1];
					File file = new File(path);
					boolean overwrite = true;
					
					if (file.exists())
					{
						Functions.printLongText(
								"There's already a file with that name. Would you like me to overwrite it? I'm sure it doesn't contain anything important. (Y/N) )");
						String input = scan.nextLine().trim().toLowerCase();
						
						if (input.equals("y") || input.equals("yes") || input.equals("overwrite"))
						{
							overwrite = true;
							Functions.printLongText(
									"Ok, it's gone. Congratulations! You just deleted the only copy of your highest scoring game! You really ought to be more careful with your things.");
							System.out.println();
						}
						else if (input.equals("n") || input.equals("no"))
						{
							Functions.printLongText(
									"Full of indecision and a desire to hoard every little thing, you refuse to get rid of the useless file. A wise choice, I'm certain that it will be important one day.");
							overwrite = false;
						}
						else
						{
							Functions.printLongText(
									"Ok, look it's not that hard. I was looking for a simple yes or no answer. Heck, I'll even take y or n if you can't be bothered to type the whole thing. Now, do you want to overwrite the file or not? (Y/N) ");
							input = scan.nextLine().trim().toLowerCase();
							
							if (input.equals("y") || input.equals("yes") || input.equals("overwrite"))
							{
								overwrite = true;
								Functions.printLongText(
										"Ok, it's gone. Congratulations! You just deleted the only copy of your highest scoring game! You really ought to be more careful with your things.");
								System.out.println();
							}
							else if (input.equals("n") || input.equals("no"))
							{
								Functions.printLongText(
										"Full of indecision and a desire to hoard every little thing, you refuse to get rid of the useless file. A wise choice, I'm certain that it will be important one day.");
								overwrite = false;
							}
							else
							{
								Functions.printLongText(
										"Alright, listen if you can't figure out how to enter yes or no, then you are certainly not qualified to decide whether to overwrite the file. In fact, I'm not certain you're qualified to play this game. But whatever continue playing.");
								overwrite = false;
								whereAmI();
							}
						}
					}
					
					if (overwrite)
					{
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter printer = new PrintWriter(bw);
						
						// Save files have this format
						// ID, row, column, floor, state
						// ID of artifact in UndergroundApp.artifactMasterList
						// state is 0 for off (or not special), 1 for on
						
						// Saves current location
						int curRow = row;
						int curCol = col;
						int curFloor = floor;
						int state = 0;
						
						// For saving purposes the player is artifact -1
						printer.println("-1," + row + "," + col + "," + floor + "," + state);
						
						// Loops over entire map
						for (row = 0; row < Constants.MAX_ROW; row++)
						{
							for (col = 0; col < Constants.MAX_COLUMN; col++)
							{
								for (floor = 0; floor < Constants.MAX_FLOOR; floor++)
								{
									if (room() != null)
									{
										for (Artifact art : room().artifactList)
										{
											if (art != null)
											{
												state = 0; // default state is
															// off (or not
															// special)
												if (art.isSpecial())
												{
													SpecialArtifact sArt = (SpecialArtifact) art;
													// I promise that it's special now; this is why I like scripting languages better
													if (sArt.on)
													{
														state = 1;
													}
												}
												printer.println(
														art.ID + "," + row + "," + col + "," + floor + "," + state);
											}
										}
									}
								}
							}
						}
						// Save things in inventory
						// inventory is position -1,-1,-1 for saving purposes
						for (Artifact art : inventory.inventory)
						{
							if (art != null)
							{
								state = 0;
								if (art.isSpecial())
								{
									SpecialArtifact sArt = (SpecialArtifact) art;
									// I promise that it's special now; this is why I like scripting languages better
									if (sArt.on)
									{
										state = 1;
									}
								}
								
								printer.println(art.ID + ",-1,-1,-1," + state);
							}
						}
						
						printer.close();
						bw.close();
						fw.close();
						Functions.printLongText(
								"There, your game has been saved. You're all ready to begin save-scumming.");
						
						// restore current location
						row = curRow;
						col = curCol;
						floor = curFloor;
					}
				}
				catch (IOException e)
				{
					Functions.printLongText(
							"I'm sorry, but that is not a valid file path. Don't you know how computers work? Do you even know what a file path is?");
				}
				catch (Exception e)
				{
					Functions.printLongText(
							"A USER__ERROR has occurred please replace the user and press any key to continue.");
				}
				
			}
			else
			{
				Functions.printLongText("Enter only one filename, please. The things I have to put up with.");
			}
		}
		else
		{
			Functions.printLongText(
					"You think you'd better save your game before you ruin your current run, but where should you save it to?");
		}
		
	}
	
	static void load(String[] inputArray, boolean has2, boolean has3)
	// loads the game state
	// 2 steps - First removes all artifacts from the rooms, putting them in an
	// array
	// Then reads the file placing them in the correct rooms
	{
		
	}
	
	private static void check(String[] inputArray, boolean has2, String verb)
	{
		// Lists the artifacts currently in the player's inventory
		inventory.listInventory();
	}
	
	private static void place(String[] inputArray, boolean has2, String verb)
	{
		// places an artifact in the player's inventory in the current room
		// If successful returns true, else false
		
		if (has2)
		{
			Artifact art = inventory.selectArtifact(inputArray[1]);
			boolean placed = false;
			if (art != null)
			{
				
				if (room().numArtifacts < 3)
				{
					for (int i = 0; i <= room().artifactList.length; i++)
					{
						if (room().artifactList[i] == null)
						{
							room().artifactList[i] = art;
							room().numArtifacts++;
							placed = true;
							break;
						}
					}
				}
				else
				{
					Functions.printLongText(
							"The arbitrary limit for objects in a room has been reached. There is thus abitrarily no more room for your "
									+ art.displayName + " in here.");
				}
			}
			else
			{
				Functions.printLongText("You go to get the '" + Functions.addInputArray(inputArray, 1)
						+ "' out of your inventory and " + verb
						+ " it in the room, but you can't find it. You could have sworn you had it a moment ago. It seems you're still suffering from amensia.");
			}
			if (placed)
			{
				inventory.numArtifacts--;
				Functions.printLongText(
						"You take the " + art.displayName + " from your inventory and " + verb + " it in the room.");
			}
		}
		else
		{
			Functions.printLongText("And what exactly are you trying to dump in this room?");
		}
		
	}
	
	private static void take(String[] inputArray, boolean has2, String verb)
	{
		// Takes an artifact from the room and places it in the player's
		// inventory
		String output = "";
		if (!has2)
		{
			Functions.printLongText("And what exactly did you want to " + verb + "?");
		}
		else if (inventory.numArtifacts >= 3)
		{
			Functions.printLongText(
					"I can see that you're struggling to carry three items, so I'm not going to let you pick up another one. You'll have to put something down first.");
		}
		else
		{
			Artifact art;
			boolean[] carryable = { false }; // I need another thing returned
											// and
											// I'm too lazy to do it the
											// right
											// way
			art = room().removeArtifact(inputArray[1], carryable);
			if (art == null && carryable[0]) // artifact not in room, so berate
											// the player
			{
				output += "You must be suffering from memory loss or hallucinations. There is no '";
				output += Functions.addInputArray(inputArray, 1);
				output += "' in this room.";
				Functions.printLongText(output);
			}
			else if (carryable[0]) // Artifact returned put in backpack
			{
				inventory.addArtifact(art);
				// TODO uncomment Functions.printLongText(art.pickUpText);
				Functions.printLongText("You " + verb + " the " + art.displayName + ".");
			}
			else // artifact in room, but not carryable
			{
				Functions.printLongText(
						"You strain to pick up the " + art.displayName + ", but you are too weak. You require STR "
								+ String.valueOf((int) (10.0 * Math.random() + 10)) + "to attempt this feat.");
			}
			
		}
		
	}
	
	private static void touch(String[] inputArray, boolean has2)
	{
		// you touch a special artifact to turn on or off
		// normal artifacts just print text
		if (!has2)
		{
			Functions.printLongText("And what exactly are you trying to put your grubby hands all over?");
		}
		else
		{
			if (inputArray[1].equals("yourself") || inputArray[1].equals("myself") || inputArray[1].equals("self"))
			{
				
				switch (touchCount)
				{
					case 0:
						touchCount++;
						Functions.printLongText(
								"Oh ha ha. Very funny. You found a moderately inappropriate way to interpret one of the game commands.");
						break;
					case 1:
						touchCount++;
						Functions.printLongText("Ok, it was funny the first time. Now you're just being juvenile.");
						break;
					case 2:
						touchCount++;
						Functions.printLongText(
								"Look it's still not funny. In fact is wasn't funny the first time, I was just trying not to hurt your feelings.");
						break;
					case 3:
						touchCount++;
						Functions.printLongText(
								"It's nice to know that your sense of humor is so well developed. This joke must never get old. I bet you tell it to your friends all the time. And they smile and nod, much like I'm doing now.");
						break;
					case 4:
						touchCount++;
						Functions.printLongText(
								"Right, at this point I'm going to have to assume that you're actually trying to do something inappropriate in my game. Well, I'll have none of that.");
						break;
					default:
						Functions.printLongText(
								"You continue trying to be both inappropriate and humorous, but only succeed in making yourself more pitiful.");
						break;
				}
			}
			else
			{
				
				Artifact item = room().selectArtifact(inputArray[1]);
				
				if (item != null)
				{
					item.touch();
				}
				else
					Functions.printLongText(
							"You go to touch it, but then realise that there is no '" + inputArray[1] + "' here.");
			}
		}
	}
	
	private static void examine(String[] inputArray, boolean has2, String command)
	{
		// prints the item description
		if (!has2)
		{
			Functions.printLongText("I'm not a mind reader. What exactly are you trying to " + command + "?");
		}
		else
		{
			Artifact item = room().selectArtifact(inputArray[1]);
			
			if (item != null)
			{
				item.printDescription();
			}
			else
				Functions.printLongText("I'd love to tell you all about this '" + inputArray[1]
						+ "', but unfortunately there is no '" + inputArray[1] + "' to speak of.");
		}
	}
	
	private static String[] removeArticles(String[] inputArray)
	{
		// takes the input array and removes little words, like articles and
		// prepositions
		String[] newArray = new String[10];
		int index = 0;
		int c = 0;
		String word;
		while (c <= 9 && c < inputArray.length)
		{
			word = inputArray[c];
			
			switch (word)
			{
				case "the":
					break;
				
				case "a":
					break;
				
				case "an":
					break;
				
				case "in":
					break;
				
				case "at":
					break;
				
				case "above":
					break;
				
				case "about":
					break;
				
				case "around":
					break;
				
				case "that":
					break;
				
				case "this":
					break;
				
				case "these":
					break;
				
				case "those":
					break;
				
				case "into":
					break;
				
				case "to":
					break;
				
				case "over":
					break;
				
				case "onto":
					break;
				
				case "on":
					break;
				
				default:
					newArray[index] = word;
					index++;
					break;
			}
			
			c++;
		}
		String[] returnArray = new String[c];
		for (int i = 0; i < c; i++)
		{
			returnArray[i] = newArray[i];
		}
		
		return returnArray;
	}
	
	private static void commit(String[] inputArray)
	{
		// prints some flavor text
		if (inputArray.length >= 2)
		{
			switch (inputArray[1])
			{
				case "suicide":
					Functions.printLongText(
							"Woah, that seems a little extreme! Your grades can't be that bad. You know, there are resources that can help. Maybe you should visit the counciling center.");
					break;
				
				case "fraud":
					crime(inputArray[1]);
					break;
				
				case "forgery":
					crime(inputArray[1]);
					break;
				
				default:
					Functions.printLongText("Sorry, committing " + inputArray[1]
							+ "is not supported in version 1.32. If you would like to see it supported in future updates, you can request it on our forums.");
					break;
			}
		}
		else
		{
			Functions.printLongText("Commit what?");
		}
		
	}
	
	private static void crime(String crime)
	{
		// more flavors
		Functions.printLongText("Umm, yeah. That's a crime. The university servely punishes crimes of " + crime
				+ ", so I'm just gonna pretend you didn't suggest that.");
		
	}
	
	private static void whereAmI()
	{
		// prints the room name and any items here
		room().printYouAreHere();
		
	}
	
	private static void look()
	{
		// prints the room description,
		room().printYouAreHere();
		room().printDescription();
		room().printItems();
		room().printExits();
		
	}
	
	private static void moveDirection(String dir)
	// calls itself with a default string parameter
	{
		moveDirection(dir, "head");
	}
	
	private static void moveDirection(String dir, String head)
	{
		// attempts to move in a given direction
		
		switch (dir)
		{
			// + column
			case "north":
				if (room().exitNorth)
				{
					col++;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			// - column
			case "south":
				if (room().exitSouth)
				{
					col--;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			
			// + row
			case "east":
				if (room().exitEast)
				{
					row++;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			// - row
			case "west":
				if (room().exitWest)
				{
					row--;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			
			// + floor
			case "up":
				if (room().exitUp)
				{
					floor++;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			// - floor
			case "down":
				if (room().exitDown)
				{
					floor--;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			
			// + column & + row
			case "northeast":
				if (room().exitNortheast)
				{
					col++;
					row++;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			// - column & + row
			case "southeast":
				if (room().exitSoutheast)
				{
					col--;
					row++;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			
			// - column & - row
			case "southwest":
				if (room().exitSouthwest)
				{
					col--;
					row--;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			// + column & - row
			case "northwest":
				if (room().exitNorthwest)
				{
					col++;
					row--;
					hasMoved = true;
				}
				else
					printBadMoveText(dir, head);
				break;
			
			// bad direction
			default:
				hasMoved = false;
				Functions.printLongText(
						"You start to take a step, but then remember that '" + dir + "' isn't a valid direction.");
				break;
		}
		if (hasMoved)
		{
			printMoveText(dir, head);
		}
	}
	
	private static void printMoveText(String dir, String head)
	{
		// prints the flavor text for moving
		// takes the direction and the movement verb
		Functions.printLongText("You " + head + " " + dir);
	}
	
	private static void printBadMoveText(String dir, String head)
	{
		// prints the flavor text for moving
		// takes the direction and the movement verb
		Functions.printLongText("You start to " + head + " " + dir + ", but there is no exit in that direction.");
	}
	
	private static void move(String[] inputArray)
	{
		// moves in a given direction
		if (inputArray.length < 2)
		{
			Functions.printLongText("You decide to " + inputArray[0] + " somewhere, but which way should you head?");
		}
		else
		{
			String dir;
			
			switch (inputArray[1])
			{
				case "n":
					dir = "north";
					break;
				case "e":
					dir = "east";
					break;
				case "s":
					dir = "south";
					break;
				case "w":
					dir = "west";
					break;
				case "ne":
					dir = "northeast";
					break;
				case "se":
					dir = "southeast";
					break;
				case "sw":
					dir = "southwest";
					break;
				case "nw":
					dir = "northwest";
					break;
				case "u":
					dir = "up";
					break;
				case "d":
					dir = "down";
					break;
				default:
					dir = inputArray[1];
					break;
			}
			
			moveDirection(dir, inputArray[0]);
		}
		
	}
	
	private static void badCommand(String input, int numWords)
	{
		// informs the user that I can't anticipate every command
		String wordPhrase;
		if (numWords == 1)
			wordPhrase = "word";
		else
			wordPhrase = "phrase";
		
		Functions.printLongText("You type in the random " + wordPhrase + ", \"" + input
				+ "\", wondering what interesting flavor text you can get. But you are disappointed.");
		
	}
	
	static void askToQuit()
	{
		// asks to quit
		Functions.printLongTextNoNewline("You decide to quit the game, but then wonder if you're really sure (Y/N) > ");
		String input = scan.nextLine().trim().toLowerCase();
		System.out.println();
		if (input.equals("y") || input.equals("yes") || input.equals("quit") || input.equals("exit")
				|| input.equals("yeah"))
		{
			Functions.printLongText(
					"You exit the game, thinking that this dumb indie game wasn't worth the 0 cents you paid for it.");
			Functions.printLongText("\nBut thanks for playing the Great Cal Poly Underground, anyways.");
			gameOn = false;
		}
		else if (input.equals("n") || input.equals("no") || input.equals("stay"))
		{
			Functions.printLongText("What were you thinking? Of course you want to keep playing.");
		}
		else
		{
			Functions.printLongText("I'm going to assume that means you want to keep playing.");
		}
	}
	
	static Room room()
	// just returns the current room object for brevity (and convenience)
	{
		return map.rooms[row][col][floor];
	}
	
}
