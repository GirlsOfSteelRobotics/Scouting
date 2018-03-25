import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Match {
	public String matchType;
	public int matchNumber;
	public String autoFunction;
	public String autoPosition;
	public String switchPosition;
	public String scalePosition;
	public String autoComments;
	public double autoRating;
	public int cubesEZ;
	public int cubesSwitch;
	public int cubesScale;
	public int cubesFailed;
	String robotFunctions;
	public double cycleRating;
	public double drivingRating;
	public String endgameFunction;
	public String robotGoal;
	public String result;
	public int alliancePoints;
	public int opponentPoints;
	public String generalComments;
	
	public static double EZ_POINTS = 0.65;
	public static double SWITCH_POINTS = 1.0;
	public static double SCALE_POINTS = 1.75;
	public static double CLIMB_POINTS = 2.0;
	public static double LIFT_POINTS = 1.5;
	
	public Match(String[] lineInput)
	{
		
		matchType = getString(lineInput[2]);
		matchNumber = getInt(lineInput[3]);
		if (matchNumber == 3955) matchNumber = 24;
		if (matchNumber == 7274) matchNumber = 27;
		autoPosition = getString(lineInput[4]);
		switchPosition = getString(lineInput[5]);
		scalePosition = getString(lineInput[6]);
		autoFunction = getString(lineInput[7]);
		
		autoRating = getInt(lineInput[8]);
		autoComments = getString(lineInput[9]);
		
		cubesEZ = getInt(lineInput[10]);
		cubesSwitch = getInt(lineInput[11]);
		cubesScale = getInt(lineInput[12]);
		cubesFailed = getInt(lineInput[13]);
		
		robotFunctions = getString(lineInput[14]);
		cycleRating = getInt(lineInput[15]);
		drivingRating = getInt(lineInput[16]);
		
		endgameFunction = getString(lineInput[17]);
		generalComments = getString(lineInput[18]);
		robotGoal = getString(lineInput[19]);
		result = getString(lineInput[20]);

		alliancePoints = getInt(lineInput[21]);
		opponentPoints = getInt(lineInput[22]);
		
	}
	
	public double getMatchScore(boolean includeAuto)
	{
		double score = 0;
		//Autonomous + Teleop
		score += getCubesEZ(includeAuto) * EZ_POINTS;
		score += getCubesSwitch(includeAuto) * SWITCH_POINTS;
		score += getCubesScale(includeAuto) * SCALE_POINTS;

		//Endgame
		if(endgameFunction.contains("+")) score += (CLIMB_POINTS + LIFT_POINTS);
		else if(endgameFunction.contains("Lifted")) score += LIFT_POINTS;
		else if(endgameFunction.contains("Climb")) score += CLIMB_POINTS;
		
		return score;
		
	}
	
	public int getElementSide()
	{

		boolean switchSide;
		boolean scaleSide;
		
		if (autoPosition.contains("Middle")) return 4;
		
		//switch
		if (autoPosition.contains("Left") && switchPosition.contains("Left")) switchSide = true;
		else if (autoPosition.contains("Right") && switchPosition.contains("Right")) switchSide = true;
		else switchSide = false;
		
		//scale
		if (autoPosition.contains("Left") && scalePosition.contains("Left")) scaleSide = true;
		else if (autoPosition.contains("Right") && scalePosition.contains("Right")) scaleSide = true;
		else scaleSide = false;
		
		if (!switchSide && !scaleSide) return 0;
		else if (switchSide && !scaleSide) return 1;
		else if (!switchSide && scaleSide) return 2;
		else if (switchSide && scaleSide) return 3;
		else return -1;
	}
	
	public int getAutoFunction()
	{
		boolean baseline = false;
		boolean switchAuto = false;
		boolean scaleAuto = false;
		
		if (autoFunction.contains("SWITCH")) switchAuto = true;
		if (autoFunction.contains("SCALE")) scaleAuto = true;
		if (autoFunction.contains("Baseline")) baseline = true;
		
		if (switchAuto && scaleAuto) return 4;
		else if (switchAuto) return 2;
		else if (scaleAuto) return 3;
		else if (baseline) return 1;
		else return 0;
	}
	
	public int getCubesSwitch(boolean includeAuto)
	{
		int switchCubes = 0;
		
		switchCubes += cubesSwitch;
		if(includeAuto && autoFunction.contains("SWITCH")) switchCubes += 1;
		
		return switchCubes;
	}
	
	public int getCubesScale(boolean includeAuto)
	{
		int scaleCubes = 0;
		
		scaleCubes += cubesScale;
		if(includeAuto && autoFunction.contains("SCALE")) scaleCubes += 1;
		
		return scaleCubes;
	}
	
	public int getCubesEZ(boolean includeAuto)
	{
		int EZCubes = 0;
		
		EZCubes += cubesEZ;
		if(includeAuto && autoFunction.contains("EZ")) EZCubes += 1;
		
		return EZCubes;
	}
	
	public void writeMatch(BufferedWriter fout) throws IOException
	{
		fout.write(matchType + " #" + matchNumber + ": " + result + ", " + alliancePoints + " - " + opponentPoints + ", ");
		fout.write(getCubesEZ(false) + " EZ, " + getCubesSwitch(false) + " Switch, " + getCubesScale(false) + " Scale, " + cubesFailed + " Failed");
		if (endgameFunction.contains("Successful")) fout.write(", climbed");
		else if (endgameFunction.contains("Attempted")) fout.write(", attempted climb");
		else if (endgameFunction.contains("Platform")) fout.write(", platform");
		else if (endgameFunction.contains("Neither")) fout.write(", no endgame");
		else if (endgameFunction.contains("+")) fout.write(", climbed+lifted");
		else if (endgameFunction.contains("Lifted")) fout.write(", lifted others");
		fout.newLine();
		if (generalComments.length() != 0)
		{
			fout.write("\tComments: " + generalComments);
			fout.newLine();
		}
		
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
