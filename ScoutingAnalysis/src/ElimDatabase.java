import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ElimDatabase {

	public ArrayList<ElimAlliance> alliances;
	
	public ElimDatabase(String filename) throws IOException
	{
		alliances = new ArrayList<ElimAlliance>();
		FileReader fileInput = new FileReader(filename);
		BufferedReader file = new BufferedReader(fileInput);
		String line = file.readLine(); //Get rid of header line
		
		//For each match entry
		while ((line = file.readLine()) != null) {
			String[] lineInput = line.split(",");
			int allianceNumber = Match.getInt(lineInput[2]);
			
			//Create new match object
			ElimTeamData match = new ElimTeamData(lineInput);
			int index = getAllianceIndex(allianceNumber);
			
			//Find or create team
			ElimAlliance alliance;
			if (index == -1) 
			{
				alliance = new ElimAlliance(allianceNumber);
				alliances.add(alliance);
			}
			else
			{
				alliance = alliances.get(index);
			}
			
			alliance.addMatch(match);
		}
		
		for (int i = 0; i < alliances.size(); i++)
		{
			if (alliances.get(i).matches.size() == 0) 
			{
				alliances.remove(i);
				i--;
			}
		}
		
		file.close();
	}
	
	public int getAllianceIndex(int allianceNumber)
	{
		for (int i = 0; i < alliances.size(); i++)
		{
			if (alliances.get(i).allianceNumber == allianceNumber)
				return i;
		}
		return -1; //alliance not in database
	}
	
	public void writeElimDataSheets(String foldername) throws IOException
	{
		for (int i = 0; i < alliances.size(); i++)
		{
			String filename = foldername + "/Alliance" + Integer.toString(alliances.get(i).allianceNumber) + "-Darwin.txt";
			System.out.println("Writing " + filename);
			alliances.get(i).writeDataFile(filename, i+1);
		}
		
	}
}
