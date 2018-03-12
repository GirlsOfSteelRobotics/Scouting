import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Team {
	public int teamNumber;
	public ArrayList<Match> matches;
	
	public Team(int teamNum)
	{
		teamNumber = teamNum;
		matches = new ArrayList<Match>();
	}
	
	public double getWinPercentage()
	{
		double wins = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			if (matches.get(i).result.equals("Win")) wins+=1;
			else if (matches.get(i).result.equals("Tie")) wins+=0.5;
		}
		return wins/matches.size();
	}
	
	public double getAverageScoreDiff()
	{
		int total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).alliancePoints;
			total -= matches.get(i).opponentPoints;
		}
		return ((double)total)/matches.size();
	}
	
	public int[][] getAutoStats()
	{
		double[][] autoFunctions = new double[5][6];
		//Rows = element on their side (0 = neither, 1 = switch, 2 = scale, 3 = both, 4 = middle)
		//Cols = what the robot did (0 = nothing, 1 = baseline, 2 = switch, 3 = scale, 4 = both, 5 = total)
		
		for (int i = 0; i < matches.size(); i++)
		{
			int r = matches.get(i).getElementSide();
			int c = matches.get(i).getAutoFunction();
			
			autoFunctions[r][c]++;
			autoFunctions[r][5]++;
		}
		
		int ret[][] = new int[5][5];
		
		for (int r = 0; r < ret.length; r++)
		{
			for (int c = 0; c < ret[0].length; c++)
			{
				ret[r][c] = (int)((autoFunctions[r][c]/autoFunctions[r][5])*100);
			}
		}
		
		return ret;
	}
	
	public double getAverageRobotScore()
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).getMatchScore(true);
		}
		return (total)/matches.size();
	}
	
	public double getAvgCubesInScale(boolean includeAuto)
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).getCubesScale(includeAuto);
		}
		return (total)/matches.size();
	}
	
	public double getMaxCubesInScale(boolean includeAuto)
	{
		double max = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).getCubesScale(includeAuto) > max) max = matches.get(i).getCubesScale(includeAuto);
		}
		return max;
	}
	
	public double getMaxCubesInSwitch(boolean includeAuto)
	{
		double max = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).getCubesSwitch(includeAuto) > max) max = matches.get(i).getCubesSwitch(includeAuto);
		}
		return max;
	}
	
	public double getMaxCubesInEZ(boolean includeAuto)
	{
		double max = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).getCubesEZ(includeAuto) > max) max = matches.get(i).getCubesEZ(includeAuto);
		}
		return max;
	}
	
	public double getMaxCubes(boolean includeEZ, boolean includeAuto)
	{
		double max = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			double total = 0;
			total += matches.get(i).getCubesSwitch(includeAuto);
			total += matches.get(i).getCubesScale(includeAuto);
			if (includeEZ) total += matches.get(i).getCubesEZ(includeAuto);
			
			if(total > max ) max = total;
		}
		return max;
	}
	
	public double getAverageAutoRating()
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).autoRating;
		}
		return (total)/matches.size();
	}
	
	public double getAverageCubesPlaced(boolean includeEZ, boolean includeAuto)
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).getCubesSwitch(includeAuto);
			total += matches.get(i).getCubesScale(includeAuto);
			total += matches.get(i).getCubesEZ(includeAuto);
		}
		return (total)/matches.size();
	}
	
	public String getClimbingAbility()
	{
		double climbs = 0;
		double lifts = 0;
		double climbAndLift = 0;
		
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).endgameFunction.contains("+")) climbAndLift++;
			else if(matches.get(i).endgameFunction.contains("Lifted")) lifts++;
			else if(matches.get(i).endgameFunction.contains("Successful")) climbs++;
		}
		
		if (climbAndLift/matches.size() > 0.1) return "Climb + Lift";
		else if (climbs/matches.size() > 0.1) return "Climb";
		else if (lifts/matches.size() > 0.1) return "Lift Others";
		else return "None";
	}
	
	public double getClimbPercentage()
	{
		double num = 0;
		
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).endgameFunction.contains("+")) num++;
			else if(matches.get(i).endgameFunction.contains("Lifted")) num++;
			else if(matches.get(i).endgameFunction.contains("Successful")) num++;
		}
		
		return 100*num/matches.size();
	}
	
	public String getRobotType() 
	{
		int total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			if (matches.get(i).robotGoal.contains("Offense")) total += 1;
			else if (matches.get(i).robotGoal.contains("Defense")) total -= 1;
		}
		if (total > 0) return "Offense";
		else if (total < 0) return "Defense";
		else return "Both";
	}
	
	public void addMatch(Match match)
	{
		matches.add(match);
	}
	
	
	public void writeStatFile(String filename, int ranking) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		fout.write("Team #" + teamNumber + " - " + matches.size() + " matches played");
		fout.newLine();
		fout.write("GoS Strategy Ranking: " + ranking);
		fout.newLine();
		fout.newLine();
		writeGeneralStats(fout);
		fout.newLine();
		writeAutoStats(fout);
		fout.newLine();
		writeTeleopStats(fout);
		fout.newLine();
		writeGeneralComments(fout);
		fout.newLine();
		writeMatches(fout);
		fout.close();
	}
	
	public void writeGeneralStats(BufferedWriter fout) throws IOException
	{
		fout.write("--------------- General Stats ---------------");
		fout.newLine();
		fout.write("Win Percentage = " + (getWinPercentage()*100) + "%");
		fout.newLine();
		fout.write("Average Score Difference = " + getAverageScoreDiff());
		fout.newLine();
		fout.write("Robot Type = " + getRobotType());
		fout.newLine();
		fout.write("Average Auto Rating = " + getAverageAutoRating() + " out of 5");
		fout.newLine();
		fout.write("Average Robot Score = " + getAverageRobotScore());
		fout.newLine();
	}
	
	public void writeAutoStats(BufferedWriter fout) throws IOException
	{
		fout.write("--------------- Autonomous Stats ---------------");
		fout.newLine();
		
		int[][] auto = getAutoStats();
		int index;
		//Rows = element on their side (0 = neither, 1 = switch, 2 = scale, 3 = both. 4 = middle)
		//Cols = what the robot did (0 = nothing, 1 = baseline, 2 = switch, 3 = scale, 4 = both)
		
		index = 0;
		int Nothing = auto[0][index] + auto[1][index] + auto[2][index] + auto[3][index] + auto[4][index];
		index = 1;
		int Baseline = auto[0][index] + auto[1][index] + auto[2][index] + auto[3][index] + auto[4][index];
		index = 2;
		int Switch = auto[0][index] + auto[1][index] + auto[2][index] + auto[3][index] + auto[4][index];
		index = 3;
		int Scale = auto[0][index] + auto[1][index] + auto[2][index] + auto[3][index] + auto[4][index];
		index = 4;
		int Both = auto[0][index] + auto[1][index] + auto[2][index] + auto[3][index] + auto[4][index];
		
		int outsideLeft = 0; int insideLeft = 0; int middle = 0; int insideRight = 0; int outsideRight = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			String pos = matches.get(i).autoPosition;
			
			if (pos.equals("Outside Left")) outsideLeft++;
			else if (pos.equals("Inside Left")) insideLeft++;
			else if (pos.equals("Inside Right")) insideRight++;
			else if (pos.equals("Outside Right")) outsideRight++;
			else if (pos.equals("Middle")) middle++;
		}
		
		insideLeft = (int)(((double)insideLeft / matches.size()) * 100);
		outsideLeft = (int)(((double)outsideLeft / matches.size()) * 100);
		middle = (int)(((double)middle / matches.size()) * 100);
		insideRight = (int)(((double)insideRight / matches.size()) * 100);
		outsideRight = (int)(((double)outsideRight / matches.size()) * 100);
		
		fout.write("Overall Auto Stats:");
		fout.newLine();
		fout.write("\t% matches crossed baseline = " + Baseline + "%");
		fout.newLine();
		fout.write("\t% matches put cube in switch = " + Switch + "%");
		fout.newLine();
		fout.write("\t% matches put cube in scale = " + Scale + "%");
		fout.newLine();
		fout.write("\t% matches put cube in switch and scale = " + Both + "%");
		fout.newLine();
		fout.write("\t% matches did not move = " + Nothing + "%");
		fout.newLine();
		fout.write("Starting Position: " + outsideLeft + "% outside left, " + insideLeft + "% inside left, " + middle + "% middle, " + insideRight + "% inside right, " + outsideRight + "% outside right");
		fout.newLine();
		
		fout.write("Stats based on switch/scale colors:");
		fout.newLine();
		
		//Rows = element on their side (0 = neither, 1 = switch, 2 = scale, 3 = both)
		//Cols = what the robot did (0 = nothing, 1 = baseline, 2 = switch, 3 = scale, 4 = both)
		fout.write("\t Switch+Scale are on robot's side: ");
		index = 3;
		fout.write("\t" + auto[index][0] + "% nothing, " + auto[index][1] + "% baseline, " + auto[index][2] + "% switch, " + auto[index][3] + "% scale, " + auto[index][4] + "% both");
		fout.newLine();
		
		fout.write("\t Switch is on robot's side: ");
		index = 1;
		fout.write("\t" + auto[index][0] + "% nothing, " + auto[index][1] + "% baseline, " + auto[index][2] + "% switch, " + auto[index][3] + "% scale, " + auto[index][4] + "% both");
		fout.newLine();
		
		fout.write("\t Scale on robot's side: ");
		index = 2;
		fout.write("\t" + auto[index][0] + "% nothing, " + auto[index][1] + "% baseline, " + auto[index][2] + "% switch, " + auto[index][3] + "% scale, " + auto[index][4] + "% both");
		fout.newLine();
		
		fout.write("\t Neither on robot's side: ");
		index = 0;
		fout.write("\t" + auto[index][0] + "% nothing, " + auto[index][1] + "% baseline, " + auto[index][2] + "% switch, " + auto[index][3] + "% scale, " + auto[index][4] + "% both");
		fout.newLine();
		
		fout.write("\t Robot is in the middle: ");
		index = 0;
		fout.write("\t" + auto[index][0] + "% nothing, " + auto[index][1] + "% baseline, " + auto[index][2] + "% switch, " + auto[index][3] + "% scale, " + auto[index][4] + "% both");
		fout.newLine();
		
		
		fout.write("All auto comments:");
		fout.newLine();
		
		for (int i = 0; i < matches.size(); i++)
		{
			if (!matches.get(i).autoComments.equals(""))
			{
				fout.write("\t" + matches.get(i).matchType + " Match #" + matches.get(i).matchNumber + ": " + matches.get(i).autoComments);
				fout.newLine();
			}
		}
		
	}
	
	public boolean canRobotClimb()
	{
		for(int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).endgameFunction.contains("Successful")) return true;
		}
		return false;
	}
	
	public void writeTeleopStats(BufferedWriter fout) throws IOException
	{
		fout.write("--------------- Teleop Stats ---------------");
		fout.newLine();
		double EZ = 0; double Switch = 0; double Scale = 0;
		double pickCube = 0; double portal = 0;
		double cycleRating = 0; double drivingRating = 0;
		double climb = 0; double platform = 0; double attempt = 0; double neither = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			EZ += matches.get(i).cubesEZ;
			Switch += matches.get(i).cubesSwitch;
			Scale += matches.get(i).cubesScale;
			
			if (matches.get(i).robotFunctions.contains("Pick")) pickCube++;
			if (matches.get(i).robotFunctions.contains("portal")) portal++;
			
			cycleRating += matches.get(i).cycleRating;
			drivingRating += matches.get(i).drivingRating;
			
			if (matches.get(i).endgameFunction.contains("Successful")) climb++;
			else if (matches.get(i).endgameFunction.contains("Attempted")) attempt++;
			else if (matches.get(i).endgameFunction.contains("Platform")) platform++;
			else neither++;
		}
		
		double totalCubes = EZ + Switch + Scale;
		EZ = (EZ/totalCubes) * 100;
		Switch = (Switch/totalCubes) * 100;
		Scale = (Scale/totalCubes) * 100;
		
		pickCube = pickCube / matches.size();
		portal = portal / matches.size();
		
		cycleRating = cycleRating / matches.size();
		drivingRating = drivingRating / matches.size();
		
		climb = climb/matches.size();
		attempt = attempt/matches.size();
		platform = platform/matches.size();
		neither = neither/matches.size();
		
		fout.write("Average # cubes placed: " + (totalCubes/matches.size()));
		fout.newLine();
		fout.write("\t% cubes in EZ = " + (int)EZ + "%");
		fout.newLine();
		fout.write("\t% cubes in Switch = " + (int)Switch + "%");
		fout.newLine();
		fout.write("\t% cubes in Scale = " + (int)Scale + "%");
		fout.newLine();
		
		fout.write("Can get cube from floor?: " + ((pickCube > 0.1) ? "Yes" : "No"));
		fout.newLine();
		fout.write("Can get cube from portal?: " + ((portal > 0.1) ? "Yes" : "No"));
		fout.newLine();
		
		fout.write("Average cycle time rating = " + cycleRating + " out of 3");
		fout.newLine();
		fout.write("Average driver rating = " + drivingRating + " out of 3");
		fout.newLine();
		
		fout.write("Can robot climb?: " + ((climb > 0.1) ? "Yes" : "No"));
		fout.newLine();
		fout.write("\t% climb in matches = " + (int)(climb*100) + "%");
		fout.newLine();
		fout.write("\t% attempted climb in matches = " + (int)(attempt*100) + "%");
		fout.newLine();
		fout.write("\t% platform in matches = " + (int)(platform*100) + "%");
		fout.newLine();
		fout.write("\t% did nothing in endgame = " + (int)(neither*100) + "%");
		fout.newLine();

	}
	
	public void writeMatches(BufferedWriter fout) throws IOException
	{
		fout.write("--------------- Match Data ---------------");
		fout.newLine();
		for (int i = 0; i < matches.size(); i++)
		{
			matches.get(i).writeMatch(fout);
		}
	}
	
	public void writeGeneralComments(BufferedWriter fout) throws IOException
	{
		fout.write("--------------- General Comments ---------------");
		fout.newLine();
		for (int i = 0; i < matches.size(); i++)
		{
			if(matches.get(i).generalComments.length() != 0)
			{
				fout.write(matches.get(i).matchType + " Match #" + matches.get(i).matchNumber + ": " + matches.get(i).generalComments);
				fout.newLine();
			}
		}
	}


}
