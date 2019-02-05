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
	
	//in auto we need
	// number of cargo in ship, cargo in rocket (in all three levels, each one), hatch in ship, hatch in rocket, platform starting level
	
	public String get
	
	
	public double getAverageRobotScore()
	{
		double total = 0;
		for (int i = 0; i < matches.size(); i++)
		{
			total += matches.get(i).getMatchScore(true);
		}
		return (total)/matches.size();
	}

	
}
