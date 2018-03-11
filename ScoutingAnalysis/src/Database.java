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
	
	public Database(String filename, boolean includePracticeMatches) throws NumberFormatException, IOException
	{
		data = new ArrayList<Team>();
		FileReader fileInput = new FileReader(filename);
		BufferedReader file = new BufferedReader(fileInput);
		String line = file.readLine(); //Get rid of header line
		
		//For each match entry
		while ((line = file.readLine()) != null) {
			String[] lineInput = line.split(",");
			int teamNumber = Match.getInt(lineInput[1]);
			
			//Create new match object
			Match match = new Match(lineInput);
			int index = getTeamIndex(teamNumber);
			
			//Find or create team
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
			
			if(includePracticeMatches || !match.matchType.contains("Practice"))
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
	
	public void sortByScaleScore(boolean includeAuto)
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxCubesInScale(includeAuto) > max.getMaxCubesInScale(includeAuto))
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public void sortBySwitchScore(boolean includeAuto)
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxSwitchScore(includeAuto) > max.getMaxSwitchScore(includeAuto))
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public int getTeamIndex(int teamNumber)
	{
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).teamNumber == teamNumber)
				return i;
		}
		return -1; //team not in database
	}
	
	public void writeDataSheets(String foldername) throws IOException
	{
		sortByRobotScore();
		
		for (int i = 0; i < data.size(); i++)
		{
			String filename = foldername + "/" + Integer.toString(data.get(i).teamNumber) + ".txt";
			System.out.println("Writing " + filename);
			data.get(i).writeStatFile(filename, i+1);
		}
		
	}
	
	public void writeRankedList(String filename, boolean includeAuto) throws IOException
	{
		sortByRobotScore();
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double cubes = ((int)(data.get(i).getAverageCubesPlaced(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": ");
			fout.write("Avg Cubes Placed = " + cubes);
			//fout.write(", Max Cubes Placed = " + data.get(i).getMaxCubes());
			fout.write(", Can climb?: " + (data.get(i).canRobotClimb() ? "Yes" : "No"));
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeMaxCubesList(String filename, boolean includeEZ, boolean includeAuto) throws IOException
	{
		sortByMaxCubes(includeEZ, includeAuto);
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": ");
			//fout.write("Avg Cubes Placed = " + cubes);
			fout.write("Max Cubes Placed = " + data.get(i).getMaxCubes(includeEZ, includeAuto));
			fout.write(", Can climb?: " + (data.get(i).canRobotClimb() ? "Yes" : "No"));
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeScaleRankedList(String filename, boolean includeAuto) throws IOException
	{
		sortByScaleScore(includeAuto);
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double score = ((int)(data.get(i).getAverageRobotScore()*100))/100.0;
			double scale = ((int)(data.get(i).getMaxCubesInScale(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Scale Score = " + scale);
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeSwitchRankedList(String filename, boolean includeAuto) throws IOException
	{
		sortBySwitchScore(includeAuto);
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double score = ((int)(data.get(i).getAverageRobotScore()*100))/100.0;
			double Switch = ((int)(data.get(i).getMaxSwitchScore(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Switch Score = " + Switch);
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeGoodScaleRobots(String filename, int threshold, boolean includeAuto) throws IOException
	{
		ArrayList<Team> goodScaleRobots = new ArrayList<Team>();
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).getMaxCubesInScale(includeAuto) >= threshold)
				goodScaleRobots.add(data.get(i));
		}
		
		goodScaleRobots = sortScaleRobots(goodScaleRobots);
		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < goodScaleRobots.size(); i++)
		{
			double score = ((int)(goodScaleRobots.get(i).getAverageRobotScore()*100))/100.0;
			double scale = ((int)(goodScaleRobots.get(i).getMaxCubesInScale(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + goodScaleRobots.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Scale Score = " + scale);
			fout.write(", Can climb?: " + (goodScaleRobots.get(i).canRobotClimb() ? "Yes" : "No"));
			fout.newLine();
		}
		
		fout.close();
		
	}
	
	public void sortByMaxCubes(boolean includeEZ, boolean includeAuto)
	{
		
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxCubes(includeEZ, includeAuto) > max.getMaxCubes(includeEZ, includeAuto))
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
		
	}
	
	public void writeGoodSwitchRobots(String filename, int threshold, boolean includeAuto) throws IOException
	{
		ArrayList<Team> goodSwitchRobots = new ArrayList<Team>();
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).getMaxSwitchScore(includeAuto) >= threshold)
				goodSwitchRobots.add(data.get(i));
		}
		
		goodSwitchRobots = sortSwitchRobots(goodSwitchRobots);
		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < goodSwitchRobots.size(); i++)
		{
			double score = ((int)(goodSwitchRobots.get(i).getAverageRobotScore()*100))/100.0;
			double Switch = ((int)(goodSwitchRobots.get(i).getMaxSwitchScore(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + goodSwitchRobots.get(i).teamNumber + ": Avg robot score = " + score);
			fout.write(", Max Switch Score = " + Switch);
			fout.write(", Can climb?: " + (goodSwitchRobots.get(i).canRobotClimb() ? "Yes" : "No"));
			fout.newLine();
		}
		
		fout.close();
		
	}
	
	public ArrayList<Team> sortScaleRobots(ArrayList<Team> unsortedRobots)
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(unsortedRobots.size() != 0)
		{
			Team max = unsortedRobots.get(0);
			for(int i = 1; i < unsortedRobots.size(); i++)
			{
				if(unsortedRobots.get(i).getAverageRobotScore() > max.getAverageRobotScore())
					max = unsortedRobots.get(i);
			}
			sortedTeams.add(max);
			unsortedRobots.remove(max);
		}
		
		return sortedTeams;
	}
	
	public ArrayList<Team> sortSwitchRobots(ArrayList<Team> unsortedRobots)
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(unsortedRobots.size() != 0)
		{
			Team max = unsortedRobots.get(0);
			for(int i = 1; i < unsortedRobots.size(); i++)
			{
				if(unsortedRobots.get(i).getAverageRobotScore() > max.getAverageRobotScore())
					max = unsortedRobots.get(i);
			}
			sortedTeams.add(max);
			unsortedRobots.remove(max);
		}
		
		return sortedTeams;
	}

}
