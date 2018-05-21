import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;


public class Match {
	public String matchType;
	public int matchNumber;
	public FieldSide startingPosition;
	public FieldSide switchPosition;
	public FieldSide scalePosition;
	public boolean autoBaseline;
	public int autoCubesSwitch;
	public int autoCubesScale;
	public String autoComments;
	public int cubesEZ;
	public int cubesSwitch;
	public int cubesScale;
	public int cubesFailed;
	public String cubeCollection;
	public String endgameFunction;
	public int robotsLifted;
	public FieldSide climbPosition;
	public String generalComments;
	public String result;
	public int partnerRating;
	
	public enum FieldSide {
		left, right, middle, none
	}
	
	
	public static double EZ_POINTS = 0.64;
	public static double SWITCH_POINTS = 1.0;
	public static double SCALE_POINTS = 1.3;
	public static double CLIMB_POINTS = 1.5;
	public static double LIFT_POINTS = 1.0;
	
	public Match(String[] lineInput)
	{
		String s;
		
		matchType = getString(lineInput[2]);
		matchNumber = getInt(lineInput[3]);
		
		//CORRECT MATCH NUMBERS
		
		s = getString(lineInput[4]);
		if (s.contains("Left")) startingPosition = FieldSide.left;
		else if (s.contains("Right")) startingPosition = FieldSide.right;
		else if (s.contains("Middle")) startingPosition = FieldSide.middle;
		else startingPosition = FieldSide.none;
		
		s = getString(lineInput[5]);
		if (s.contains("Left")) switchPosition = FieldSide.left;
		else if (s.contains("Right")) switchPosition = FieldSide.right;
		else switchPosition = FieldSide.none;
		
		s = getString(lineInput[6]);
		if (s.contains("Left")) scalePosition = FieldSide.left;
		else if (s.contains("Right")) scalePosition = FieldSide.right;
		else scalePosition = FieldSide.none;
		
		s =  getString(lineInput[7]);
		if (s.contains("Yes")) autoBaseline = true;
		else autoBaseline = false;
		
		autoCubesSwitch = getInt(lineInput[8]);
		autoCubesScale = getInt(lineInput[9]);
		autoComments = getString(lineInput[10]);
		
		cubesEZ = getInt(lineInput[11]);
		cubesSwitch = getInt(lineInput[12]);
		cubesScale = getInt(lineInput[13]);
		cubesFailed = getInt(lineInput[14]);
		
		cubeCollection = getString(lineInput[15]);
		endgameFunction = getString(lineInput[16]);
		robotsLifted = getInt(lineInput[17]);
		
		s = getString(lineInput[18]);
		if (s.contains("Left")) climbPosition = FieldSide.left;
		else if (s.contains("Right")) climbPosition = FieldSide.right;
		else if (s.contains("middle")) climbPosition = FieldSide.middle;
		else climbPosition = FieldSide.none;
		
		generalComments = getString(lineInput[19]);
		result = getString(lineInput[20]);
		partnerRating = getInt(lineInput[21]);
		
		//Corrections
		if (matchType.equals("Qualification") && matchNumber == 1)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 3)
		{
			switchPosition = FieldSide.left;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 5)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 7)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 8)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.left;
		}
		else if (matchType.equals("Qualification") && matchNumber == 10)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 11)
		{
			switchPosition = FieldSide.left;
			scalePosition = FieldSide.right;
		}
		else if (matchType.equals("Qualification") && matchNumber == 15)
		{
			switchPosition = FieldSide.right;
			scalePosition = FieldSide.left;
		}
		else if (matchType.equals("Qualification") && matchNumber == 22)
		{
			switchPosition = FieldSide.left;
			scalePosition = FieldSide.right;
		}
		
	}
	
	public double getMatchScore(boolean includeAuto)
	{
		double score = 0;
		
		//Auto
		if (includeAuto)
		{
			score += autoCubesSwitch * (2 * SWITCH_POINTS);
			score += autoCubesScale * (2 * SCALE_POINTS);
		}
		
		//Teleop
		score += getCubesEZ(false) * EZ_POINTS;
		score += getCubesSwitch(false) * SWITCH_POINTS;
		score += getCubesScale(false) * SCALE_POINTS;

		//Endgame
		if(endgameFunction.equals("Climb")) score += CLIMB_POINTS;
		score += robotsLifted * LIFT_POINTS;
		
		return score;
		
	}
	
	public double getMatchScoreNoClimb(boolean includeAuto)
	{
		double score = 0;

		//Auto
		if (includeAuto)
		{
			score += autoCubesSwitch * (1.5 * SWITCH_POINTS);
			score += autoCubesScale * (1.5 * SCALE_POINTS);
		}
		
		//Teleop
		score += getCubesEZ(false) * EZ_POINTS;
		score += getCubesSwitch(false) * SWITCH_POINTS;
		score += getCubesScale(false) * SCALE_POINTS;
		
		return score;
		
	}
	
	public int getElementSide()
	{
		boolean switchSide = false;
		boolean scaleSide = false;
		
		if (startingPosition == FieldSide.middle) return 4;
		
		if (startingPosition == switchPosition) switchSide = true;
		if (startingPosition == scalePosition) scaleSide = true;

		if (!switchSide && !scaleSide) return 0;
		else if (switchSide && !scaleSide) return 1;
		else if (!switchSide && scaleSide) return 2;
		else if (switchSide && scaleSide) return 3;
		else return -1;
	}
	
	public int getAutoFunction()
	{
		boolean switchAuto = false;
		boolean scaleAuto = false;
		
		if (autoCubesSwitch > 0) switchAuto = true;
		if (autoCubesScale > 0) scaleAuto = true;
		
		if (switchAuto && scaleAuto) return 4;
		else if (switchAuto) return 2;
		else if (scaleAuto) return 3;
		else if (autoBaseline) return 1;
		else return 0;
	}
	
	public int getCubesSwitch(boolean includeAuto)
	{
		int switchCubes = 0;
		
		switchCubes += cubesSwitch;
		if(includeAuto) switchCubes += autoCubesSwitch;
		
		return switchCubes;
	}
	
	public int getCubesScale(boolean includeAuto)
	{
		int scaleCubes = 0;
		
		scaleCubes += cubesScale;
		if(includeAuto) scaleCubes += autoCubesScale;
		
		return scaleCubes;
	}
	
	public int getCubesEZ(boolean includeAuto)
	{
		return cubesEZ;
	}
	
	public void writeMatch(BufferedWriter fout) throws IOException
	{
		fout.write(matchType + " #" + matchNumber + ": ");
		fout.write(getCubesEZ(false) + " EZ, " + getCubesSwitch(false) + " Switch, " + getCubesScale(false) + " Scale, " + cubesFailed + " Failed");
		if (endgameFunction.equals("Climb")) fout.write(", climbed");
		else if (endgameFunction.contains("Attempted")) fout.write(", attempted climb");
		else if (endgameFunction.contains("Platform")) fout.write(", platform");
		else if (endgameFunction.contains("Neither")) fout.write(", no endgame");
		
		if (robotsLifted > 0) fout.write(", lifted " + robotsLifted + "others");
		fout.newLine();
		
		fout.write("\tAuto: Start Pos = " + getStartingPosition() + ", Baseline?: " + (autoBaseline ? "Yes" : "No"));
		fout.write(", " + getSwitchSide() + " Switch(x" + autoCubesSwitch + ")" + ", " + getScaleSide() + " Scale(x" + autoCubesScale + ")");
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
	
	public String getStartingPosition()
	{
		if (startingPosition == FieldSide.left) return "Left";
		else if (startingPosition == FieldSide.right) return "Right";
		else if (startingPosition == FieldSide.middle) return "Middle";
		else return "ERROR";
	}
	
	public String getSwitchSide()
	{
		if (switchPosition == FieldSide.left) return "Left";
		else if (switchPosition == FieldSide.right) return "Right";
		else return "ERROR";
	}
	
	public String getScaleSide()
	{
		if (scalePosition == FieldSide.left) return "Left";
		else if (scalePosition == FieldSide.right) return "Right";
		else return "ERROR";
	}
	

}
