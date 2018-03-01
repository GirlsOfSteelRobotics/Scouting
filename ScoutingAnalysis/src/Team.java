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
	
	public double getAverageRobotScore()
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).cubesEZ;
			total += matches.get(i).cubesSwitch;
			total += 2 * (matches.get(i).cubesScale);
			if (matches.get(i).endgameFunction.contains("Successful")) total += 5;
		}
		return (total)/matches.size();
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
	
	public void printStats(PrintStream output)
	{
		output.println("Win Percentage = " + (getWinPercentage()*100) + "%");
		output.println("Average Points = " + getAverageScoreDiff());
	}
	
	public void printMatches(PrintStream output)
	{
		for (int i = 0; i < matches.size(); i++)
		{
			matches.get(i).printMatch(output);
		}
	}
	
	public void writeStatFile(String filename) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		fout.write("Team #" + teamNumber + " - " + matches.size() + " matches played");
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
		double Baseline = 0; double Switch = 0; double Scale = 0; double Attempt = 0; double Nothing = 0;
		double left = 0; double middle = 0; double right = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			String auto = matches.get(i).autoFunction;
			if (auto.contains("move")) Nothing++;
			if (auto.contains("attempted")) Attempt++;
			if (auto.contains("Cross")) Baseline++;
			if (auto.contains("Switch")) Switch++;
			if (auto.contains("Scale")) Scale++;
			
			if (matches.get(i).autoPosition.equals("Left")) left++;
			else if (matches.get(i).autoPosition.equals("Right")) right++;
			else if (matches.get(i).autoPosition.equals("Middle")) middle++;
		}
		
		Baseline = (Baseline / matches.size()) * 100;
		Switch = (Switch / matches.size()) * 100;
		Scale = (Scale / matches.size()) * 100;
		Attempt = (Attempt / matches.size()) * 100;
		Nothing = (Nothing / matches.size()) * 100;
		
		left = (left / matches.size()) * 100;
		right = (right / matches.size()) * 100;
		middle = (middle / matches.size()) * 100;
		
		fout.write("% matches crossed baseline = " + (int)Baseline + "%");
		fout.newLine();
		fout.write("% matches put cube in switch = " + (int)Switch + "%");
		fout.newLine();
		fout.write("% matches put cube in scale = " + (int)Scale + "%");
		fout.newLine();
		fout.write("% matches did not move = " + (int)Nothing + "%");
		fout.newLine();
		fout.write("Starting Position: " + (int)left + "% left, " + (int)middle + "% middle, " + (int)right + "% right");
		fout.newLine();
		fout.write("All auto comments:");
		fout.newLine();
		
		for (int i = 0; i < matches.size(); i++)
		{
			if (!matches.get(i).autoComments.equals(""))
			{
				fout.write("\tMatch #" + matches.get(i).matchNumber + ": " + matches.get(i).autoComments);
				fout.newLine();
			}
		}
		
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
				fout.write("Match #" + matches.get(i).matchNumber + ": " + matches.get(i).generalComments);
				fout.newLine();
			}
		}
	}


}
