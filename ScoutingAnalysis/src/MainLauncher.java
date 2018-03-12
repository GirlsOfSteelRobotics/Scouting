import java.io.IOException;

public class MainLauncher {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		System.out.println("Loading database...");
		Database data = new Database("GPRScoutingData.csv", true); //filename, include practice matches?
		data.writeRankedList("RankedList.txt", true); //by avg robot score, includeAuto?
		data.writeDataSheets("DataSheets");
		//data.writeSwitchRankedList("MaxSwitchRanking.txt", false); //by max in switch, includeAuto?
		//data.writeScaleRankedList("MaxScaleRanking.txt", false); //by max in scale, includeAuto?
		data.writeGoodScaleRobots("ScaleRobots.txt", 5, false); //at least x cubes, sorted by robot score, includeAuto?
		data.writeGoodSwitchRobots("SwitchRobots.txt", 4, false); //at least x cubes, sorted by robot score, includeAuto?
		data.writeMaxCubesList("MaxCubesRanking.txt", false, false); //filename, inlcudeEZ?, includeAuto?
		data.writeCSVfile("DataSummary.csv");
	}

}
