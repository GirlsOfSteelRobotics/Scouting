//import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.PrintStream;
//import java.util.Scanner;

public class MainLauncher {

	public static void main(String[] args) throws NumberFormatException, IOException {
		//Scanner input = new Scanner(System.in);
		//PrintStream output = new PrintStream(System.out);
		//int i; String s;
		
		System.out.println("Loading database...");
		Database data = new Database("MVRScoutingData.csv");
		data.sortByRobotScore();
		data.writeDataSheets("DataSheets");
		data.writeRankedList("RankedList.txt"); //by robot score
		//data.writeSwitchRankedList("MaxSwitchRanking.txt"); //by max in switch
		//data.writeScaleRankedList("MaxScaleRanking.txt"); //by max in scale
		data.writeGoodScaleRobots("ScaleRobots.txt");
		data.writeGoodSwitchRobots("SwitchRobots.txt");
		data.writeMaxCubesList("MaxCubesRanking.txt"); //by max cubes
		/*
		boolean done = false;
		while(!done)
		{
			output.println("Scouting Analysis Menu");
			output.println("1. Display Team Statistics");
			output.println("2. Generate Data Sheets");
			output.println("3. Quit");
			output.print("Choose an option: ");
			
			i = input.nextInt();
			output.println();
			
			if (i == 1) //Display Team Stats
			{
				output.print("Enter a team number: ");
				int teamNumber = input.nextInt();
				
				data.printStats(teamNumber, output);
				
				output.print("See matches? (y/n): ");
				if (input.next().equals("y"))
				{
					output.println();
					data.printMatches(teamNumber, output);
				}
				
				output.println();
				output.print("Return to menu? (y/n): ");
				if (!input.next().equals("y"))
					done = true;
			}
			else if (i == 2)
			{
				output.println("Creating folder with data sheets...");
				data.writeDataSheets("DataSheets");
			}
			else done = true;
			
			output.println();
		}
		
		output.println("Goodbye!");
		input.close();
		*/

	}

}
