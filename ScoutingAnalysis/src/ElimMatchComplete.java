import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ElimMatchComplete {

	public ArrayList<ElimTeamData> matchData;
	public String matchType;
	public int matchNumber;
	
	public ElimMatchComplete(String type, int number)
	{
		matchType = type;
		matchNumber = number;
		matchData = new ArrayList<ElimTeamData>();
	}
	
	public void addMatchData(ElimTeamData data)
	{
		matchData.add(data);
	}

	public void writeElimMatch(BufferedWriter fout) throws IOException {
		// TODO Auto-generated method stub
		fout.write("----------------------------------------");
		fout.newLine();
		fout.write(matchType + " Match #" + matchNumber);
		fout.newLine();
		
		int totalAutoSwitch = 0;
		int totalAutoScale = 0;
		
		int totalScale = 0;
		int totalSwitch = 0;
		int totalEZ = 0;
		
		int totalClimbs = 0;
		
		for (int i = 0; i < matchData.size(); i++)
		{
			totalAutoSwitch += matchData.get(i).autoCubesSwitch;
			totalAutoScale += matchData.get(i).autoCubesScale;
			
			totalScale += matchData.get(i).cubesScale;
			totalSwitch += matchData.get(i).cubesSwitch;
			totalEZ += matchData.get(i).cubesEZ;
			
			if (matchData.get(i).endgameFunction.contains("Lifted") || matchData.get(i).endgameFunction.equals("Climb"))
			{
				totalClimbs++;
			}
		}
		
		fout.write("AUTONOMOUS: " + totalAutoSwitch + " cubes in switch, " + totalAutoScale + " cubes in scale");
		fout.newLine();
		
		fout.write("TELEOP: (total = " + totalScale + " scale, " + totalSwitch + " switch, " + totalEZ + " EZ)");
		fout.newLine();
		
		for (int i = 0; i < matchData.size(); i++)
		{
			ElimTeamData data = matchData.get(i);
			fout.write("Team " + data.teamNumber + ": ");
			fout.write(data.cubesScale + " in scale, " + data.cubesSwitch + " in switch, " + data.cubesEZ + " in EZ");
			fout.newLine();
			fout.write("\tStrategy: " + data.strategyComments);
			fout.newLine();
		}
		
		fout.write("ENDGAME: (#robots in air = " + totalClimbs + ")");
		fout.newLine();
		
		for (int i = 0; i < matchData.size(); i++)
		{
			ElimTeamData data = matchData.get(i);
			fout.write("\tTeam " + data.teamNumber + ": ");
			fout.write(data.endgameFunction + ", lifted " + data.robotsLifted + " OTHER robots");
			fout.newLine();
		}
	}
}
