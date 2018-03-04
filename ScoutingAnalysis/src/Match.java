import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Match {
	public String matchType;
	public int matchNumber;
	public String autoFunction;
	public String autoPosition;
	public String autoComments;
	public double autoRating;
	public int cubesEZ;
	public int cubesSwitch;
	public int cubesScale;
	String robotFunctions;
	public double cycleRating;
	public double drivingRating;
	public String endgameFunction;
	public String robotGoal;
	public String result;
	public int alliancePoints;
	public int opponentPoints;
	public String generalComments;
	
	public Match(String[] lineInput)
	{
		matchType = getString(lineInput[2]);
		matchNumber = getInt(lineInput[3]);
		if (matchNumber == 4611) matchNumber = 37;
		autoFunction = getString(lineInput[4]);
		autoPosition = getString(lineInput[5]);
		autoComments = getString(lineInput[6]);
		autoRating = getInt(lineInput[7]);
		
		cubesEZ = getInt(lineInput[8]);
		cubesSwitch = getInt(lineInput[9]);
		cubesScale = getInt(lineInput[10]);
		
		robotFunctions = getString(lineInput[11]);
		cycleRating = getInt(lineInput[12]);
		drivingRating = getInt(lineInput[13]);
		
		endgameFunction = getString(lineInput[14]);
		robotGoal = getString(lineInput[15]);
		result = getString(lineInput[16]);

		alliancePoints = getInt(lineInput[17]);
		opponentPoints = getInt(lineInput[18]);
		generalComments = getString(lineInput[19]);
	}
	
	public void printMatch(PrintStream output)
	{
		output.println("Match #" + matchNumber + ": " + result + ", " + alliancePoints + " - " + opponentPoints);
		output.println("\tComments: " + generalComments);
	}
	
	public void writeMatch(BufferedWriter fout) throws IOException
	{
		fout.write(matchType + " Match #" + matchNumber + ": " + result + ", " + alliancePoints + " - " + opponentPoints + ", ");
		fout.write(cubesEZ + " cubes in EZ, " + cubesSwitch + " cubes in Switch, " + cubesScale + " cubes in Scale");
		if (endgameFunction.contains("Successful")) fout.write(", successfully climbed");
		else if (endgameFunction.contains("Attempted")) fout.write(", attempted climbed");
		else if (endgameFunction.contains("Platform")) fout.write(", platform");
		else if (endgameFunction.contains("Neither")) fout.write(", no endgame");
		fout.newLine();
		//fout.write("\tComments: " + generalComments);
		//fout.newLine();
	}
	
	private static String getString(String input)
	{
		if (input.length() == 0) return "";
		if (input.charAt(0) == '"') return input.substring(1, input.length()-1);
		else return input;
	}
	
	public static int getInt(String input)
	{
		if (input.charAt(0) == '"')
		{
			input = input.substring(1, input.length()-1);
		}
		if (input.charAt(0) == ' ')
		{
			input = input.substring(1, input.length());
		}
		if (input.charAt(input.length()-1) == ' ')
		{
			input = input.substring(0, input.length()-1);
		}
		return Integer.parseInt(input);
	}
	

}
