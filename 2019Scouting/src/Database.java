import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database 
{
	public ArrayList<Team> data;
	
	public Database(String filename, boolean includePracticeMatches) throws NumberFormatException, IOException
	{
		data = new ArrayList<Team>();
		FileReader fileInput = new FileReader(filename);
		BufferedReader file = new BufferedReader(fileInput);
		String line = file.readLine(); //get rid of header line
		
		while((line = file.readLine()) != null)
		{
			String[] lineInput = line.split(",");
			int teamNumber = Match.getInt(lineInput[1]);
			Match match = new Match(lineInput);
			int index = getTeamIndex(teamNumber);
			
			//find or create team
			Team team;
			if(index == -1)
			{
				System.out.println("team does not exist");
				team = new Team(teamNumber);
				data.add(team);
			}
			else
			{
				team = data.get(index);
			}
			
			team.addMatch(match);
					
		}
		
		for (int i = 0; i < data.size(); i++)
		{
			if(data.get(i).matches.size() == 0)
			{
				data.remove(i);
				i--;
			}
		}
		
		file.close();
	}
	
	public int getTeamIndex(int teamNumber) 
	{
		for(int i = 0; i < data.size(); i++)
		{
			if(data.get(i).teamNumber == teamNumber)
			{
				return i;
			}
		}
		return -1; //team not in database
	}
	
	public void writeDataSheets(String foldername) throws IOException
	{

		
		for(int i = 0; i < data.size(); i++)
		{
			String filename = foldername + "/" + Integer.toString(data.get(i).teamNumber) + ".txt";
			System.out.println("Writing " + filename);
			data.get(i).writeStatFile(filename, i+1);
		}
	}
	
}
