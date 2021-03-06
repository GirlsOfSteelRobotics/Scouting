import java.io.IOException;

public class MainLauncher {

	public static void main(String[] args) throws NumberFormatException, IOException {
		//ElimDatabase elimData = new ElimDatabase("ElimsScoutingData.csv");
		//elimData.writeElimDataSheets("ElimDataSheets");
		
		/*Loading in the database*/
		System.out.println("Loading database...");
		
		/*Printing out data sheets and ranked lists*/
		Database data = new Database("WOWScoutingData.csv", false); //filename, include practice matches?
		data.writeRankedList("RankedList.txt", true); //by avg robot score, includeAuto?
		data.writeDataSheets("DataSheets");
		//data.writeSwitchRankedList("MaxSwitchRanking.txt", false); //by max in switch, includeAuto?
		//data.writeScaleRankedList("MaxScaleRanking.txt", false); //by max in scale, includeAuto?
		data.writeGoodScaleRobots("ScaleRobots.txt", 6, false); //at least x cubes, sorted by robot score, includeAuto?
		data.writeGoodSwitchRobots("SwitchRobots.txt", 5, false); //at least x cubes, sorted by robot score, includeAuto?
		//data.writeGoodEZRobots("EZRobots.txt", 4, false); //at least x cubes, sorted by robot score, includeAuto?
		//data.writeMaxCubesList("MaxCubesRanking.txt", false, false); //filename, inlcudeEZ?, includeAuto?
		
		/*Writing CSV file for team viewing*/
		data.writeCSVfile("DataSummary.csv"); //for presentation to team
	}

}
