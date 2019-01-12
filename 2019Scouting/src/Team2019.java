import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Team2019 
{
	public int teamNumber;
	public ArrayList<Match2019> matches;
	
	public Team2019(int teamNum)
	{
		teamNumber = teamNum;
		matches = new ArrayList<Match2019>();
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
	
	

	
}
