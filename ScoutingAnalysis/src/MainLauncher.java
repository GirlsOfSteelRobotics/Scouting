//import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.PrintStream;
//import java.util.Scanner;

public class MainLauncher {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		System.out.println("Loading database...");
		Database data = new Database("MVRScoutingData.csv");
		data.writeRankedList("RankedList.txt"); //by robot score
		data.writeDataSheets("DataSheets");
		data.writeSwitchRankedList("MaxSwitchRanking.txt"); //by max in switch
		data.writeScaleRankedList("MaxScaleRanking.txt"); //by max in scale
		data.writeGoodScaleRobots("ScaleRobots.txt", 5); //at least x cubes, sorted by robot score
		data.writeGoodSwitchRobots("SwitchRobots.txt", 4); //at least x cubes, sorted by robot score
		data.writeMaxCubesList("MaxCubesRanking.txt", false); //by max cubes (not including EZ)
		
	}

}
