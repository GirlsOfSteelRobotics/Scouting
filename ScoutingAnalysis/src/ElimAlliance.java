import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ElimAlliance {
	public ArrayList<ElimMatchComplete> matches;
	public int allianceNumber;
	
	public ElimAlliance(int allianceNum)
	{
		allianceNumber = allianceNum;
		matches = new ArrayList<ElimMatchComplete>();
	}
	
	public void addMatch(ElimTeamData match)
	{
		String matchType = match.matchType;
		int matchNumber = match.matchNumber;
		
		boolean added = false;
		
		for (int i = 0; i < matches.size(); i++)
		{
			if (matches.get(i).matchType.equals(matchType) && matches.get(i).matchNumber == matchNumber)
			{
				matches.get(i).addMatchData(match);
				added = true;
			}
		}
		
		if (!added)
		{
			ElimMatchComplete matchComp = new ElimMatchComplete(matchType, matchNumber);
			matchComp.addMatchData(match);
			matches.add(matchComp);
		}
	}
	
	public void writeDataFile(String filename, int ranking) throws IOException
	{
		FileWriter outFile = new FileWriter(filename);
		BufferedWriter fout = new BufferedWriter(outFile);
		
		fout.write("Alliance #" + allianceNumber + " - " + matches.size() + " matches played");
		fout.newLine();
		fout.newLine();
		for (int i = 0; i < matches.size(); i++)
		{
			matches.get(i).writeElimMatch(fout);
			fout.newLine();
		}
		
		fout.close();
	}
}
