
public class Map
// contains the rooms
{
	// Instance
	Room rooms[][][] = new Room[Constants.MAX_ROW][Constants.MAX_COLUMN][Constants.MAX_FLOOR];
	
	Map()
	{
		
		Room rom;
		Artifact art;
		SpecialArtifact specArt;
		
		// In hindsight this was a terrible way to enter the info
		
		rom = new Room(0, 0, 1);
		rooms[0][0][1] = rom;
		rom.name = "a Box Canyon";
		rom.description = "This looks like the Voorhis Ecological Reserve. A caverous opening in the canyon wall lies just ahead of you.";
		rom.youAreHerePhrase = "You find yourself in a Canyon.";
		rom.exitNorth = true;
		specArt = new SpecialArtifact(
				/* Name */ "paper",
				/* AltName */ "ALT_!)@(&%$&_NAME",
				/* YouFoundText */"There's a piece of Paper on the ground in front of you.",
				/* OffDescript */ "The paper appears blank.",
				/* TurningOn */ "The paper begins to glow.",
				/* OnDescript */ "The paper reads: Welcome to the Great Cal Poly Underground!",
				/* TurningOff */ "The writing fades.",
				/* TurnsOff? */ true);
		specArt.makeCarryable("THE_PAPER", "PICKED UP PAPER", "DROPPED PAPER");
		rom.addArtifact(specArt);
		
		rom = new Room(0, 1, 1);
		rooms[0][1][1] = rom;
		rom.name = "the Kellogg Mansion";
		rom.description = "This is the former home of William Kellogg";
		rom.youAreHerePhrase = "A large mansion lies in front of you.";
		rom.exitDown = true;
		rom.exitEast = true;
		art = new Artifact(
				/* Name */ "picture",
				/* Description */ "The picture bears an inscription that reads: W.K. Kellogg. He appears to be holding a box of Corn Flakes.",
				/* YouFoundText */"There's a Picture on the wall nearby.",
				/* TouchText */ "W.K. Kellogg looks on disapproingly as you touch his picture.",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		
		rom = new Room(1, 1, 1);
		rooms[1][1][1] = rom;
		rom.name = "Los Olivos";
		rom.description = "The aroma of hamburgers and pizza wafts through the air.";
		rom.youAreHerePhrase = "You enter the food court.";
		rom.exitNorth = true;
		rom.exitWest = true;
		rom.exitEast = true;
		art = new Artifact(
				/* Name */ "lunch",
				/* Description */ "The lunch appears to be a hamburger, french fries, and some kind of soda.",
				/* YouFoundText */"There's an uneaten Lunch on a table.",
				/* TouchText */ "You go to eat the Lunch, but unfortunately it seems that it's for display only.",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		specArt = new SpecialArtifact(
				/* Name */ "freezer",
				/* AltName */ "big",
				/* YouFoundText */"There's a Big Freezer behind the counter.",
				/* OffDescript */ "It’s one of those big walk-in freezers, that you might find in restaurants.",
				/* TurningOn */ "As you open the freezer’s door, a rush of chilling air blows past your face.",
				/* OnDescript */ "The freezer is appears to be empty. Looks like its time to order another shipment of food.",
				/* TurningOff */ "It’s getting too cold so you close the freezer door. It slams shut with a thud.",
				/* TurnsOff? */ true);
		rom.addArtifact(specArt);
		specArt = new SpecialArtifact(
				/* Name */ "radio",
				/* AltName */ "portable",
				/* YouFoundText */ "A small Portable Radio is on the table next to the Lunch.",
				/* OffDescript */ "They said radio is dead, but its seems hale and hearty to you. The sweet sounds of Don’t Stop Believing are playing on 9.31 Jack FM.",
				/* TurningOn */ "Being an uncultured plebian, who doesn’t appreciate hard rock, you shut off Steve Perry just as he was about to get to the chorus. ",
				/* OnDescript */ "Radio is dead. Well, this one is off anyways.",
				/* TurningOff */ "Seeing reason, you put the tunes back on. Steve Perry finishes strong, it brings a tear to your eye.",
				/* TurnsOff? */ true);
		rom.addArtifact(specArt);
		
		rom = new Room(1, 2, 1);
		rooms[1][2][1] = rom;
		rom.name = "the Rose Garden";
		rom.description = "You are standing in the middle of a beautiful rose garden.";
		rom.youAreHerePhrase = "The sweet smell of flowers wafts through the air. You enter a garden.";
		rom.exitSouth = true;
		rom.exitWest = true;
		art = new Artifact(/* Name */ "gazebo",
				/* Description */ "The small plaque on the structure reads: Enjoy the garden!",
				/* YouFoundText */"A Gazebo sits in the middle of the garden.",
				/* TouchText */ "You put your hand against the gazebo and feel something. It's a sharp sensation almost like... Oh, that's a pretty bad splinter you got there.",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		
		rom = new Room(0, 2, 1);
		rooms[0][2][1] = rom;
		rom.name = "a Classroom";
		rom.description = "You see an old table covered with papers near the front of the room.";
		rom.youAreHerePhrase = "You cringe as you enter one of the torture chambers that they call a 'classroom'.";
		rom.exitNorth = true;
		rom.exitNortheast = true;
		rom.exitEast = true;
		art = new Artifact(/* Name */ "exam",
				/* Description */ "'CIS 234 Final Exam...' The rest appears unreadable due to a lack of printer toner.",
				/* YouFoundText */"Wait, is that... an Exam? Just lying there on the desk?",
				/* TouchText */ "You put the paper right up to your face, willing it to give you the answers. I'm sorry, but is this really how you study?",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		
		rom = new Room(0, 3, 1);
		rooms[0][3][1] = rom;
		rom.name = "the Library";
		rom.description = "Sunlight is streaming in though the glass windows.";
		rom.youAreHerePhrase = "Selves upon selves of untouched books stretch into the distance. You must be in the library.";
		rom.exitSouth = true;
		rom.exitEast = true;
		art = new Artifact(/* Name */ "sign",
				/* Description */ "The sign reads: No food or drinks in the library.",
				/* YouFoundText */"A Sign is on the wall a bit further on.",
				/* TouchText */ "You touch the sign, and nothing happens. Frankly I'm not sure what you were expecting.",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		specArt = new SpecialArtifact(/* Name */ "book",
				/* AltName */ "glowing",
				/* YouFoundText */ "There's a Book mysteriously glowing on the table.",
				/* OffDescript */ "This book seems to be glowing.",
				/* TurningOn */ "You open the book and some glowing text appears inside.",
				/* OnDescript */ "It contains the some of the secrets to great Java programming. Your Java level has risen to 2. You’ve unlocked switch statements.",
				/* TurningOff */ "You go to close the book, but become engrossed in the Java documentation and forget.",
				/* TurnsOff? */ false);
		specArt.glowing = true;
		specArt.glowingNoticeText = "A mysterious glow catches your eye, as you enter.";
		rom.addArtifact(specArt);
		
		rom = new Room(2, 1, 1);
		rooms[2][1][1] = rom;
		rom.name = "the Stairwell";
		rom.description = "A set of stairs spirals up and down for several floors. ";
		rom.youAreHerePhrase = "You find yourself in a stairwell.";
		rom.exitEast = true;
		rom.exitWest = true;
		rom.exitUp = true;
		rom.exitDown = true;
		art = new Artifact(/* Name */ "extinguisher",
				/* Description */ "A red canister that you can use to cave in someone’s head ‘in case of emergency,’ or at least that’s what Hollywood would have us believe.",
				/* YouFoundText */"There is a Fire Extinguisher on the wall.",
				/* TouchText */ "You run your hands against its smooth surface. You consider tampering with it, but decide its not worth the $500.",
				/* AltName */ "fire");
		rom.addArtifact(art);
		
		rom = new Room(0, 1, 0);
		rooms[0][1][0] = rom;
		rom.name = "the Mansion Basement";
		rom.description = "A musty, disused room underneath Kellogg’s mansion.";
		rom.youAreHerePhrase = "You enter the basement beneath the mansion..";
		rom.exitUp = true;
		rom.exitEast = true;
		
		rom = new Room(1, 1, 0);
		rooms[1][1][0] = rom;
		rom.name = "the Utility Corridor";
		rom.description = "A narrow corridor with pipes lining both walls. You have to duck in some places where a U-bend sticks out from the ceiling or wall. ";
		rom.youAreHerePhrase = "Under the school lies miles of pipes and wires, you find yourself in a corridor filled with them.";
		rom.exitWest = true;
		rom.exitEast = true;
		rom.exitSouth = true;
		art = new Artifact(/* Name */ "sign",
				/* Description */ "A big red sign shows a man touching some pipes. His hands are red, and he for some reason he's dancing. Odd. Maybe you should try it.",
				/* YouFoundText */"A brightly colored Warning Sign, is on the wall.",
				/* TouchText */ "The warning is probably not for touching the sign itself, but just to make you get your greasy hands all over it. To my disappointment, you are not electrocuted or burned. Perhaps you should try touching something else?",
				/* AltName */ "warning");
		rom.addArtifact(art);
		specArt = new SpecialArtifact(/* Name */ "pipes", /* AltName */ "ALT_!)@(&%$&_NAME",
				/* YouFoundText */"Pipes of all sizes fill the corridor.",
				/* OffDescript */ "These pipes hiss slightly as who knows what flows through them.",
				/* TurningOn */ "You touch the pipes, and burn your hand. Oh, my that must have really hurt. But do you know what hurts more? The knowledge that you saw the warning sign and studiously ignored it. Bravo to you, deciding you want to make your own mistakes. This is truly a life lesson. Now I hope you apply this the next time you see a warning sign.",
				/* OnDescript */ "Those pipes are very hot, you should have noticed that before. Then again, being so unobservant that you need me to literally describe everything to you is your main character trait. So maybe both of us shouldn’t expect so much.",
				/* TurningOff */ "Didn’t you learn your lesson last time? Well, as amusing as it was the first time. I’m not letting you touch it again.",
				/* TurnsOff? */ false);
		rom.addArtifact(specArt);
		
		rom = new Room(1, 0, 0);
		rooms[1][0][0] = rom;
		rom.name = "a Sewer";
		rom.description = "The room is dark and wet. You don’t want to think about the smell, and you really don’t want to think about the sewage that smell is coming from.";
		rom.youAreHerePhrase = "For some reason that neither you nor I can comprehend, you enter the sewers.";
		rom.exitNorth = true;
		art = new Artifact(/* Name */ "sewage",
				/* Description */ "It’s a smelly pile of garbage and worse. Look, it’s disgusting, and I refuse to describe it further.",
				/* YouFoundText */"There is some disgusting Sewage to your left.",
				/* TouchText */ "Look, I know it’s your natural inclination to touch everything you see, but think about what you’re trying to do here. Well, I suppose I can’t physically stop you, so I will literarily. I refuse to describe you touching the sewage, you’ll just have to use your well-developed sense of imagination. Yes, that’s it, imagine touching it. Good, isn’t imagination fun?",
				/* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		
		rom = new Room(2, 1, 0);
		rooms[2][1][0] = rom;
		rom.name = "the bottom of the Stairs";
		rom.description = "The stairs end here on the basement level. It’s very grimy down here. You get the feeling that not many people come down here. ";
		rom.youAreHerePhrase = "A staircase leading upward lies in front of you, perhaps you could ascend to a better place. Physically I mean, mentally and metaphysically, you're still stuck where you are.";
		rom.exitWest = true;
		rom.exitUp = true;
		rom.exitEast = true;
		specArt = new SpecialArtifact(/* Name */ "light", /* AltName */ "flashlight",
				/* YouFoundText */"There's a faint light coming from under the stairs.",
				/* OffDescript */ "There’s a flashlight shining from under the stairs. It’s providing most of the light here.",
				/* TurningOn */ "You turn it off.",
				/* OnDescript */ "You turned your only light off, so it’s very dark in here now.",
				/* TurningOff */ "You grope around and turn the flashlight back on.", /* TurnsOff? */ true);
		rom.addArtifact(specArt);
		
		rom = new Room(3, 1, 0);
		rooms[3][1][0] = rom;
		rom.name = "a Storage Room";
		rom.description = "A dusty, forgotten room, where obsolete equipment now collects duct. They’ve been very busy collecting duct, because a layer coats nearly everything. You sneeze and wonder why you’re still in this miserable room.";
		rom.youAreHerePhrase = "You head into a room and immediately begin choking on the clouds of dust in the air. This storage room must not have been used in years, decades even";
		rom.exitWest = true;
		specArt = new SpecialArtifact(/* Name */ "projector", /* AltName */ "old",
				/* YouFoundText */"An Old Projector lies apart in the corner.",
				/* OffDescript */ "It’s one of those old overhead projectors.",
				/* TurningOn */ "After fumbling about, getting dust all over your clothes, you find the switch and flip it on.",
				/* OnDescript */ "It projects a circle of light against the far wall, lighting up the room and revealing the clouds of swirling dust everywhere.",
				/* TurningOff */ "You turn the old thing back off. Plunging your world back into eternal darkness.",
				/* TurnsOff? */ true);
		rom.addArtifact(specArt);
		
		rom = new Room(2, 3, 1);
		rooms[2][3][1] = rom;
		rom.name = "the Quad";
		rom.description = "A nice little outdoor area, where people can sit, chat, and have a bit to eat between classes. You’d love to sit down and chat with someone, but unfortunately you’re alone. Well, you have me, but I’m just the narrator. I’m not supposed to be an actual character, more of a voice in your head really.";
		rom.youAreHerePhrase = "Some trees provide shade here and some chairs respite. You enter the quad.";
		rom.exitWest = true;
		rom.exitSoutheast = true;
		specArt = new SpecialArtifact(/* Name */ "poster", /* AltName */ "glowing",
				/* YouFoundText */"A Poster is giving off a soft glow.",
				/* OffDescript */ "This poster is glowing, but on further inspection you see that it's actually the poster behind this one.",
				/* TurningOn */ "You rip off the top poster to reveal the actual glowing poster.",
				/* OnDescript */ "It shows your grades for this quarter, brazenly glowing for all to see.",
				/* TurningOff */ "You try to pull the glowing poster down, but for some reason that functionality hasn’t been coded in yet.",
				/* TurnsOff? */ false);
		specArt.glowing = true;
		specArt.glowingNoticeText = "There's an odd glow coming from the wall over there.";
		rom.addArtifact(specArt);
		
		rom = new Room(3, 2, 1);
		rooms[3][2][1] = rom;
		rom.name = "the Parking Structure";
		rom.description = "A large parking structure that is usually full of cars. But at this time of day there are only a few here.";
		rom.youAreHerePhrase = "A car alarm sounds from nearby. You enter the parking structure.";
		rom.exitSouth = true;
		rom.exitNorthwest = true;
		specArt = new SpecialArtifact(/* Name */ "car",
				/* AltName */ "nice",
				/* YouFoundText */"That is one Nice Car over there.",
				/* OffDescript */ "Wow, that’s a nice car. It’s probably expensive, or something. Look I’m really not sure. I don’t know anything about cars.",
				/* TurningOn */ "You lovingly run your hands over the vehicle. It responds by lovingly blaring its car alarm at you.",
				/* OnDescript */ "Now you’ve done it. That nice car has a very nice car alarm. And by nice, I mean nice and loud. ",
				/* TurningOff */ "You kick the car’s tire, and for some crazy reason the car alarm stops. You think it must have been a requirement to have things turn back off.",
				/* TurnsOff? */ true);
		rom.addArtifact(specArt);
		
		rom = new Room(3, 1, 1);
		rooms[3][1][1] = rom;
		rom.name = "the CLA Building";
		rom.description = "Cal Poly’s most notably building. So notable in fact that they’re tearing down next year. There’s some story about the architect not pointing it in the right direction, but you’re pretty lost yourself, so you don’t have grounds to criticize. ";
		rom.youAreHerePhrase = "A tall building with an arrow on top towers over you. This is the CLA building.";
		rom.exitNorth = true;
		rom.exitWest = true;
		art = new Artifact(/* Name */ "door",
				/* Description */ "This door heads South. You get an odd feeling about it for some reason. You feel that if you stepped through it you might end up in a random room.",
				/* YouFoundText */"There's a door to the south.",
				/* TouchText */ "You try the door but its locked. Oh well.", /* AltName */ "ALT_!)@(&%$&_NAME");
		rom.addArtifact(art);
		
		rom = new Room(1, 3, 1);
		rooms[1][3][1] = rom;
		rom.name = "the Lecture Hall";
		rom.description = "A podium stands at the front of a big room, from there someone blathers on for an hour, while several other people pretend to pay attention. ";
		rom.youAreHerePhrase = "Echoes of past lectures ring in your ears as you enter the Lecture Hall.";
		rom.exitEast = true;
		rom.exitSouthwest = true;
		rom.exitWest = true;
		specArt = new SpecialArtifact(/* Name */ "projector",
				/* AltName */ "ALT_!)@(&%$&_NAME",
				/* YouFoundText */"A Projector is set up at the back of the room.",
				/* OffDescript */ "It’s a portable projector on a cart. You’ve seen several like it before.",
				/* TurningOn */ "You hit some buttons and the projector starts up. ",
				/* OnDescript */ "Seems like someone left a presentation on auto play. It’s about something called the Porter Forces. Maybe it’s the worst super hero group ever. ‘Evildoers beware: the Bighearted Bellhop, he’ll glad hold the door for people all day. The Charming Chauffer and her sidekick, Baggage Boy.’ Yes, I’m certain this is what the lecture was about.",
				/* TurningOff */ "TURNING_OFF", /* TurnsOff? */ true);
		rom.addArtifact(specArt);
		
		rom = new Room(2, 1, 2);
		rooms[2][1][2] = rom;
		rom.name = "the Top of the Stairs";
		rom.description = "The stairs upward end here. Well, not really they actually continue upwards, but they end here for you. I’ve blocked them off. Yes, sorry but this is as far you can ascend in this life. Better luck next time.";
		rom.youAreHerePhrase = "Here at the top of the stairs you can see both how far you could rise and how far you can fall.";
		rom.exitDown = true;
		rom.exitWest = true;
		art = new Artifact(/* Name */ "stairs",
				/* Description */ "The stairs have been blocked off by an invisible boundary, you hate those. Games that have those are the worst. But if you were to buy the third floor DLC, you could head up there.",
				/* YouFoundText */"These Stairs are Blocked somehow.",
				/* TouchText */ "You try to push past the boundary, but the words 'leaving map area' appear in front of you.",
				/* AltName */ "blocked");
		rom.addArtifact(art);
		
		rom = new Room(1, 1, 2);
		rooms[1][1][2] = rom;
		rom.name = "the Computer Lab";
		rom.description = "Several rows of computers fill this room. Well, I say computers but all that’s visible is the display. These are, of course, virtual machines, so all the other hardware is stored in the cloud.";
		rom.youAreHerePhrase = "A marvel of modern techology, you enter the Computer Lab.";
		rom.exitEast = true;
		rom.exitWest = true;
		art = new Artifact(/* Name */ "computer",
				/* Description */ "It’s just a screen display. No other hardware is present. By the miracle of cloud computing the hardware is provided remotely.",
				/* YouFoundText */"A virtualized Computer Screen displays a screen saver. Remember screen savers? My favorite was the one with the stars rushing towards you. That's the one that showing on the Screen anyways",
				/* TouchText */ "You do the only thing you can and get your hands all over the screen displays. Luckily these are stainless glass displays, so you don’t ruin them, try as you might.",
				/* AltName */ "screen");
		rom.addArtifact(art);
		
		rom = new Room(0, 1, 2);
		rooms[0][1][2] = rom;
		rom.name = "the Cloud Computing Data Center";
		rom.description = "Here is the ‘cloud’ where all the hardware is stored. The university could of course rent storage space from Microsoft or something, but it was simpler and cheaper to build a data center on site. ";
		rom.youAreHerePhrase = "You step into the cloud. Not that you enter the internet, that's only available in the DLC. No, you enter another room, called the Cloud.";
		rom.exitEast = true;
		art = new Artifact(/* Name */ "computers",
				/* Description */ "Here is the equipment for the computers in the adjacent room. If you had more time you would be inclined to mess with these and see what happens.",
				/* YouFoundText */"A Stack of Computers takes up much of the room.",
				/* TouchText */ "Unable to resist your violent urges, you try to break these machines. Luckily they’re virtual machines so you can’t actually touch them. It’s one of the main advantages of virtual machines that people like you can’t tamper with them. ",
				/* AltName */ "stack");
		rom.addArtifact(art);
		
	}
	
}
