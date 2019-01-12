import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Match2019

{
	// public String matchType;
	public int teamNumber;
	public int matchNumber;
	public boolean platform2;
	public boolean platform1;
	public int autoCargoInRocket;
	public int autoCargoInShip;
	public int autoHatchInRocket;
	public int autoHatchInShip;
	public String autoComments;
	public int cargoInRocket;
	public int cargoInShip;
	public int hatchInRocket;
	public int hatchInShip;
	public int scoringHeight;
	public String cargoCollection;
	public int endgamePlatform;
	public boolean robotsHelped;
	public String teleopComments;
	public String result;
	public String generalComments;

	// these values need to be changed. this is for robot score
	public static double STARTING_PLATFORM_POINTS = 1;
	public static double PLATFORM2_POINTS = 1;
	public static double PLATFORM3_POINTS = 1;
	public static double ROCKET_HATCH_POINTS = 1;
	public static double ROCKET_CARGO_POINTS = 1;
	public static double SHIP_HATCH_POINTS = 1;
	public static double SHIP_CARGO_POINTS = 1;
	public static double HELP_ROBOT_POINTS = 1;

	public Match2019(String[] lineInput) {
		String s;
		teamNumber = Integer.parseInt((lineInput[2]));
		matchNumber = Integer.parseInt((lineInput[3]));
		
		
		
	}

	

}
