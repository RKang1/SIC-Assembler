import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ToolBag {

	public int StorCalc(String opName, String operand, int lineNum) {
		int addressInc = 0;
		char byteType;
		String byteArg;
		int bArgEnd = 3; // Index of the location of the end of the BYTE
							// argument

		switch (opName) {
		case "RESW":
			addressInc = Integer.parseInt(operand) * 3;
			break;

		case "RESB":
			addressInc = Integer.parseInt(operand) * 1;
			break;

		case "WORD":
			addressInc = 3;
			break;

		case "BYTE":
			byteType = operand.charAt(0);
			for (int i = 2; i < operand.length(); i++) {
				if (operand.charAt(i) == '\'') {
					bArgEnd = i;
				}
			}
			byteArg = operand.substring(2, bArgEnd);

			if (byteType == 'C') {
				addressInc = byteArg.length();
			}

			if (byteType == 'X') {
				if (byteArg.length() % 2 == 0) {
					addressInc = byteArg.length() / 2;
				} else {
					System.out.println("Error: Odd number of X bytes found in operand field on line: " + lineNum);
				}
			}
			break;

		default:
			break;
		}

		return addressInc;
	}// StorCalc

	// Return the type of operator. (sic operator, assembler directive, storage,
	// or unknown
	public String OpType(String opName) {
		File sicops = new File("sicops.txt");
		File directives = new File("asmDir.txt");
		File storage = new File("storWords.txt");
		String type = "unknown";
		String curLine;

		try {
			BufferedReader sicReader = new BufferedReader(new FileReader(sicops));
			BufferedReader dirReader = new BufferedReader(new FileReader(directives));
			BufferedReader storReader = new BufferedReader(new FileReader(storage));

			while ((curLine = sicReader.readLine()) != null) {
				if (opName.equals(GetFirst(curLine))) {
					type = "sicop";
				}
			}
			sicReader.close();

			while ((curLine = dirReader.readLine()) != null) {
				if (opName.equals(GetFirst(curLine))) {
					type = "directive";
				}
			}
			dirReader.close();

			while ((curLine = storReader.readLine()) != null) {
				if (opName.equals(GetFirst(curLine))) {
					type = "storage";
				}
			}
			storReader.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

		return type;
	}// OpType

	// Finds the first word in a string
	String GetFirst(String input) {
		String word;
		int index = 0;
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == ' ' || input.charAt(i) == '\t') {
					index = i;
					i = input.length();
				}
			}
			if (index > 0) {
				word = input.substring(0, index);
				return word;
			} else {
				return input;
			}
		}
		return null;
	}// GetFirst

	// Returns the rest of the line after the first word
	String GetRest(String input) {
		int index = 0;
		for (; input.charAt(index) != ' '; index++) {
		}
		return input.substring((index + 1), input.length());
	}// GetRest
	
	// Checks if the line is blank or a comment
	public boolean TextChk(String line) {
		boolean flag = false;

		char firstChar = ' ';

		// Checks if line is blank
		if (line.length() == 0) {
			flag = true;
			return flag;
		}

		// Finds first non space character
		int index = 0;
		for (; (index < line.length()) && (line.charAt(index) == ' ' || line.charAt(index) == '\t'); index++) {
		}
		
		if (index != line.length()) {
			firstChar = line.charAt(index);
		}

		// Checks if the line is a comment, or full of spaces
		if (index == line.length() || firstChar == '.') {
			flag = true;
		}
		return flag;
	}// TextChk
	
	// Finds the next prime number after the input
		int GetPrime(int number) {
			for (int i = number + 1; true; i++) {
				if (IsPrime(i)) {
					return i;
				}
			}
		}// GetPrime

	// Checks if the input is prime
		private boolean IsPrime(int numIn) {
			for (int i = 2; i * i <= numIn; i++) {
				if (numIn % i == 0) {
					return false;
				}
			}
			return true;
		}// IsPrime
}// ToolBag
