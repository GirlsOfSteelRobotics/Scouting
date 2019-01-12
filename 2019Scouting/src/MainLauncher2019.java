import java.io.IOException;

public class MainLauncher2019 {

	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		System.out.println("Loading database...");
		Database2019 data = new Database2019("Test form.csv", false); //filename, include practice matches
		data.writeDataSheets("DataSheets");
		System.out.println("Teams in database: " + data.data.size());
		System.out.println("Done");
				
	}

}
