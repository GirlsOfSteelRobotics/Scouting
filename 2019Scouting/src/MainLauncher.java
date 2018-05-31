import java.io.IOException;

public class MainLauncher {

	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		System.out.println("Loading database...");
		Database data = new Database("Test form.csv", false); //filename, include practice matches
		data.writeDataSheets("DataSheets");
		System.out.println("Teams in database: " + data.data.size());
		System.out.println("Done");
				
	}

}
