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
		//Instantiate ArrayList of Teams
		data = new ArrayList<Team>();
		
		//Create object to read in file
		FileReader fileInput = new FileReader(filename);
		BufferedReader file = new BufferedReader(fileInput);
		String line = file.readLine(); //Get rid of header line
		
		//For each match entry (each row in the imported spreadsheet)
		while ((line = file.readLine()) != null) { //reads in line, makes sure not null
			String[] lineInput = line.split(","); //separates entries by commas (this is why comments can't have commas!)
			int teamNumber = Match.getInt(lineInput[1]); //start reading at index 1 (index 0 is timestamp)
			
			/*CORRECT TEAM NUMBERS HERE! Ex: if (teamNumber == 3501) teamNumber = 3504; */
			
			//Create new match object
			Match match = new Match(lineInput);
			
			//Check if team already exists in database by getting its index (returns -1 if it doesn't exist)
			int index = getTeamIndex(teamNumber);
			
			//Find or create team
			Team team; //reference to the Team that we want to add the match to
			if (index == -1) //if team not already in database
			{
				team = new Team(teamNumber); //create a new team
				data.add(team); //add the team to the database (only does this once for each team)
			}
			else //team already exists in database
			{
				team = data.get(index); //point "team" to the already existing object for that team number
			}
			
			if(includePracticeMatches || !match.matchType.contains("Practice"))
			{
				team.addMatch(match); //add the match data to that team's arraylist of matches
			}
				
		}
		
		//remove any teams that don't have matches
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).matches.size() == 0) 
			{
				data.remove(i);
				i--; //this ensures we don't skip over the next team
			}
		}
		
		file.close(); //!!!
	}
	
	public void sortByRobotScore() //pretty self explanatory
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
	
	public void sortByScaleScore(boolean includeAuto)//pretty self explanatory
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
	
	public void sortBySwitchScore(boolean includeAuto)//pretty self explanatory
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(data.size() != 0)
		{
			Team max = data.get(0);
			for(int i = 1; i < data.size(); i++)
			{
				if(data.get(i).getMaxCubesInSwitch(includeAuto) > max.getMaxCubesInSwitch(includeAuto))
					max = data.get(i);
			}
			sortedTeams.add(max);
			data.remove(max);
		}
		
		data = sortedTeams;
	}
	
	public int getTeamIndex(int teamNumber) //return the index of that team number in the arraylist of teams
	//returns -1 if team doesnt exist in database
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
		sortByRobotScore(); //this is so that they're in ranked order and we can print out ranking by index
		
		for (int i = 0; i < data.size(); i++)
		{
			String filename = foldername + "/" + Integer.toString(data.get(i).teamNumber) + ".txt"; //generate file name
			System.out.println("Writing " + filename);
			data.get(i).writeStatFile(filename, i+1); //i+1 = team ranking (since we sorted)
		}
		
	}
	
	public void writeRankedList(String filename, boolean includeAuto) throws IOException
	{
		sortByRobotScore();
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < data.size(); i++)
		{
			double cubes = ((int)(data.get(i).getAverageCubesPlaced(true, includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + data.get(i).teamNumber + ": ");
			fout.write("Avg Cubes Placed = " + cubes);
			//fout.write(", Max Cubes Placed = " + data.get(i).getMaxCubes());
			fout.write(", Climbing Ability: " + data.get(i).getClimbingAbility());
			fout.newLine();
		}
		
		fout.close();
	}
	
	public void writeCSVfile(String filename) throws IOException
	//same way we write other files, but separate everything with commas to appear in different cells
	//this is basically the opposite of what we're doing when we read in the spreadsheet
	{
		sortByRobotScore(); //so the teams appear in ranked otder
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		//Write Header
		fout.write("Team Ranking" + ",");
		fout.write("Team Number" + ",");
		fout.write("Robot Score" + ",");
		fout.write("Climbing Ability" + ",");
		fout.write("Climb/Lift Percentage" + ",");
		fout.write("Max Cubes in Game" + ",");
		fout.write("Max Scale - Teleop" + ",");
		fout.write("Max Switch - Teleop" + ",");
		fout.write("Max EZ - Teleop" + ",");
		fout.write("Autonomous Functions" + ",");
		fout.write("Multiple Cube Auto?" + ",");
		fout.newLine();
		
		for (int i = 0; i < data.size(); i++)
		{
			Team team = data.get(i);
			fout.write((i+1) + ",");
			fout.write(team.teamNumber + ",");
			fout.write(team.getAverageRobotScore() + ",");
			fout.write(team.getClimbingAbility() + ",");
			fout.write(team.getClimbPercentage() + ",");
			fout.write(team.getMaxCubes(true, true) + ",");
			fout.write(team.getMaxCubesInScale(false) + ",");
			fout.write(team.getMaxCubesInSwitch(false) + ",");
			fout.write(team.getMaxCubesInEZ(false) + ",");
			fout.write(team.getAutoAbility() + ",");
			fout.write((team.hasMultipleCubeAuto() ? "Yes" : "No") + ",");
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
			fout.write(", Climbing Ability: " + data.get(i).getClimbingAbility());
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
			double Switch = ((int)(data.get(i).getMaxCubesInSwitch(includeAuto)*100))/100.0;
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
		
		goodScaleRobots = sortByRobotScoreNoClimb(goodScaleRobots);
		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < goodScaleRobots.size(); i++)
		{
			double score = ((int)(goodScaleRobots.get(i).getAverageRobotScore()*100))/100.0;
			double scale = ((int)(goodScaleRobots.get(i).getMaxCubesInScale(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + goodScaleRobots.get(i).teamNumber + ": Avg robot score* = " + score);
			fout.write(", Max Scale Score = " + scale);
			fout.write(", Climbing Ability: " + goodScaleRobots.get(i).getClimbingAbility());
			fout.newLine();
		}
		
		fout.close();
		
	}
	
	public void writeGoodEZRobots(String filename, int threshold, boolean includeAuto) throws IOException
	{
		ArrayList<Team> goodEZRobots = new ArrayList<Team>();
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i).getMaxCubesInEZ(includeAuto) >= threshold)
				goodEZRobots.add(data.get(i));
		}
		
		goodEZRobots = sortByRobotScoreNoClimb(goodEZRobots);
		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < goodEZRobots.size(); i++)
		{
			double Switch = ((int)(goodEZRobots.get(i).getMaxCubesInSwitch(includeAuto)*100))/100.0;
			double EZ = ((int)(goodEZRobots.get(i).getMaxCubesInEZ(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + goodEZRobots.get(i).teamNumber + ": ");
			fout.write("Max EZ Score = " + EZ);
			fout.write(", Max Switch Score = " + Switch);
			fout.write(", Climbing Ability: " + goodEZRobots.get(i).getClimbingAbility());
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
			if (data.get(i).getMaxCubesInSwitch(includeAuto) >= threshold)
				goodSwitchRobots.add(data.get(i));
		}
		
		goodSwitchRobots = sortByRobotScoreNoClimb(goodSwitchRobots);
		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		for (int i = 0; i < goodSwitchRobots.size(); i++)
		{
			double score = ((int)(goodSwitchRobots.get(i).getAverageRobotScore()*100))/100.0;
			double Switch = ((int)(goodSwitchRobots.get(i).getMaxCubesInSwitch(includeAuto)*100))/100.0;
			fout.write((i+1) + ". Team " + goodSwitchRobots.get(i).teamNumber + ": Avg robot score* = " + score);
			fout.write(", Max Switch Score = " + Switch);
			fout.write(", Climbing Ability: " + goodSwitchRobots.get(i).getClimbingAbility());
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
	
	public ArrayList<Team> sortEZRobots(ArrayList<Team> unsortedRobots)
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
	
	public ArrayList<Team> sortByRobotScoreNoClimb(ArrayList<Team> unsortedRobots)
	{
		ArrayList<Team> sortedTeams = new ArrayList<Team>();
		
		while(unsortedRobots.size() != 0)
		{
			Team max = unsortedRobots.get(0);
			for(int i = 1; i < unsortedRobots.size(); i++)
			{
				if(unsortedRobots.get(i).getAverageRobotScoreNoClimb() > max.getAverageRobotScoreNoClimb())
					max = unsortedRobots.get(i);
			}
			sortedTeams.add(max);
			unsortedRobots.remove(max);
		}
		
		return sortedTeams;
	}

}
