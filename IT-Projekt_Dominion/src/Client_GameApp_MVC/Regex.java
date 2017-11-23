package Client_GameApp_MVC;

public class Regex {

	protected enum UserInput {
		clientName,
		ipAddress,
		port,
		password
	}

	public static void main (String[] args) {
		String newInput = "65536";
		boolean isInputCorrect = checkUserInput(newInput, UserInput.port);
		System.out.println(isInputCorrect);
	}
	
	/**
	 * @author Adrian
	 * Checks if the user entered a valid input. This method is applicable for the inputs clientName, ipAddress, port and password.
	 * Returns true if the input is valid.
	 * 
	 * @param userInput
	 * @param inputType
	 * @return valid
	 */
	public static boolean checkUserInput(String userInput, UserInput inputType){
		boolean valid = false;
		final int MAX_INPUT_LENGTH = 255;
		String[] parts = userInput.split("\\.");

		switch(inputType) {
		// ClientName and password can't be longer than MAX_INPUT_LENGTH
		
		case clientName:
		case password:
			if (userInput.length()<=MAX_INPUT_LENGTH && userInput.length() > 0)
				valid = true;
			break;
			// The ipAddress must consist of 4 parts. Each part is an integer from 0 to 255.
		
		case ipAddress:
			if (parts.length == 4){
				valid = true;
				for (String part : parts){
					try {
						int number = Integer.parseInt(part);
						if (number < 0 || number > 255) valid = false;
					} catch (NumberFormatException e) {
						// input was not an integer
						valid = false;
					}
				}		
			}
			break;
			// The port must be an integer from 1 to 65535.
		
		case port:
			try {
				int number = Integer.parseInt(userInput);
				if (number > 0 && number <= 65535) valid = true;
			} catch (NumberFormatException e) {
				// input was not an integer
				valid = false;
			}
			break;
		}
		return valid;
	}
}
