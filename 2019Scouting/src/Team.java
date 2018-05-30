import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Team 
{
	public int teamNumber;
	public ArrayList<Match> matches;
	
	public Team(int teamNum)
	{
		teamNumber = teamNum;
		matches = new ArrayList<Match>();
		System.out.println("new team created");
	}
	
	public void addMatch(Match match)
	{
		matches.add(match);
		System.out.println("match added");
	}
	
	public void writeStatFile(String filename, int ranking) throws IOException
	{

		
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		//how we want the text file to be set up 
		fout.write("Team #" + teamNumber + " - " + matches.size() + " matches played");
		fout.newLine();
		fout.newLine();
		fout.write("hello");
		fout.close();
	}
}
