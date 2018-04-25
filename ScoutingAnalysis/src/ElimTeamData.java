import java.util.ArrayList;


public class ElimTeamData {
	
	public int teamNumber;
	public String matchType;
	public int matchNumber;
	public int autoCubesSwitch;
	public int autoCubesScale;
	public int cubesEZ;
	public int cubesSwitch;
	public int cubesScale;
	public String cubeCollection;
	public String endgameFunction;
	public int robotsLifted;
	public String strategyComments;
	public String result;
	
	public ElimTeamData(String[] lineInput)
	{
		teamNumber = getInt(lineInput[1]);
		matchType = getString(lineInput[3]);
		matchNumber = getInt(lineInput[4]);
		
		autoCubesSwitch = getInt(lineInput[5]);
		autoCubesScale = getInt(lineInput[6]);

		cubesEZ = getInt(lineInput[7]);
		cubesSwitch = getInt(lineInput[8]);
		cubesScale = getInt(lineInput[9]);
		cubeCollection = getString(lineInput[10]);
		
		endgameFunction = getString(lineInput[11]);
		robotsLifted = getInt(lineInput[12]);
		
		strategyComments = getString(lineInput[13]);
		result = getString(lineInput[14]);
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
