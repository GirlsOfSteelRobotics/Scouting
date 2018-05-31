
public class Match 
{
	public int matchNumber;
	public String robotCapabilities;
	public String result;
	public int robotRating;
	public String comment;
	
	public Match(String[] lineInput)
	{
		System.out.println("new match");
		String s;
		matchNumber = getInt(lineInput[2]);
		robotCapabilities = getString(lineInput[3]);
		result = getString(lineInput[4]);
		robotRating = getInt(lineInput[5]);
		comment = getString(lineInput[6]);
	}
	
	public static int getInt(String input)
	{
		if(input.charAt(0) == '"')
		{
			input = input.substring(1, input.length() - 1);
		}
		if(input.charAt(0) == ' ')
		{
			input = input.substring(1, input.length());
		}
		if(input.charAt(input.length() - 1) == ' ')
		{
			input = input.substring(0, input.length() - 1);
		}
		return Integer.parseInt(input);
	}
	
	public static String getString(String input)
	{
		if(input.length() == 0)
		{
			return "";
		}
		if(input.charAt(0) == '"')
		{
			return input.substring(1, input.length() - 1);
		}
		else
		{
			return input;
		}
	}
}
