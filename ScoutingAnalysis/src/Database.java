import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;


public class Database {
	private ArrayList<Team> data;
	
	public Database(String filename) throws NumberFormatException, IOException
	{
		data = new ArrayList<Team>();
		FileReader fileInput = new FileReader(filename);
		BufferedReader file = new BufferedReader(fileInput);
		String line = file.readLine();
		
		while ((line = file.readLine()) != null) {
			String[] lineInput = line.split(",");
			int teamNumber = Match.getInt(lineInput[1]);
			if (teamNumber == 3505) teamNumber = 3504;
			if (teamNumber == 6396) teamNumber = 6936;
			if (teamNumber == 667) teamNumber = 677;
			if (teamNumber == 114) teamNumber = 144;
			if (teamNumber == 3202) teamNumber = 3201;
			//Create match
			Match match = new Match(lineInput);
			
			
			int index = getTeamIndex(teamNumber);
			Team team;
			if (index == -1) 
			{
				team = new Team(teamNumber);
				data.add(team);
			}
			else
			{
				team = data.get(index);
			}
			
			if(!match.matchType.contains("Practice"))
				team.addMatch(match);
		}
		
		file.close();
	}
	
	public void sortByRobotScore()
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getAverageRobotScore() > max.getAverageRobotScore())
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public void sortByScaleScore()
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxScaleScore() > max.getMaxScaleScore())
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public void sortBySwitchScore()
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxSwitchScore() > max.getMaxSwitchScore())
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public void printStats(int teamNumber, PrintStream output)
	{
		output.println("Team Stats for team #" + teamNumber + ":");
		Team team = data.get(getTeamIndex(teamNumber));
		team.printStats(output);
	}
	
	public void printMatches(int teamNumber, PrintStream output)
	{
		output.println("Matches for team #" + teamNumber + ":");
		Team team = data.get(getTeamIndex(teamNumber));
		team.printMatches(output);

	}
	
	public boolean isTeamInDatabase(int teamNumber)
	{
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).teamNumber == teamNumber)
				return true;
		}
		return false;
	}
	
	public int getTeamIndex(int teamNumber)
	{
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).teamNumber == teamNumber)
				return i;
		}
		return -1;
	}
	
	public void writeDataSheets(String foldername) throws IOException
	{
		for (int i = 0; i < data.size(); i++)
		{
			String filename = foldername + "/" + Integer.toString(data.get(i).teamNumber) + ".txt";
			System.out.println("Writing " + filename);
			data.get(i).writeStatFile(filename, i+1);
		}
		
	}
	
	public void writeRankedList(String filename) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double score = ((int)(data.get(i).getAverageRobotScore()*100))/100.0;
			double cubes = ((int)(data.get(i).getAverageCubesPlaced()*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Avg Cubes Placed = " + cubes);
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeScaleRankedList(String filename) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double score = ((int)(data.get(i).getAverageRobotScore()*100))/100.0;
			double scale = ((int)(data.get(i).getMaxScaleScore()*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Scale Score = " + scale);
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeSwitchRankedList(String filename) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double score = ((int)(data.get(i).getAverageRobotScore()*100))/100.0;
			double Switch = ((int)(data.get(i).getMaxSwitchScore()*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Switch Score = " + Switch);
			fout.newLine();
		}
		
		fout.close();
	}

}
