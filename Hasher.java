import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

public class Hasher {
	File dataFile;
	String[] hashArr;
	ToolBag tool;

	Hasher(File inputFile) {
		dataFile = inputFile;
		tool = new ToolBag();
	}

	public void Hash() {

		try {

			if (dataFile.exists()) {
				int strNum = 0;
				LineNumberReader lineReader = new LineNumberReader(new FileReader(dataFile));

				// Counts how many lines are in the data file
				while (lineReader.readLine() != null) {
					strNum++;
				}
				lineReader.close();

				// Finds the next prime number at least twice the number of
				// lines in the data file to use as array size
				int primeNum = tool.GetPrime(2 * strNum);

				hashArr = new String[primeNum];
				BufferedReader buffReader = new BufferedReader(new FileReader(dataFile));
				String currLine = "";
				String fWord = "";
				int index = 0;
				int itemFoundAt;

				while ((currLine = buffReader.readLine()) != null) {
					// Finds the first word of the line
					fWord = tool.GetFirst(currLine);

					// Checks if the first word already exists in the array
					itemFoundAt = Search(fWord);

					// Hashes an index from the first word
					index = HashGen(fWord, hashArr.length);

					// Inputs the current line if it contains more than one word
					if (!fWord.equals(currLine)) {
						if (itemFoundAt < 0) {

							// Linear probing
							if (hashArr[index] != null) {
								for (; hashArr[index] != null; index++) {
									if (index >= hashArr.length - 1) {
										index = -1;
									}
								}
							}
							//System.out.println(currLine + " stored at " + index + ".\n");

							hashArr[index] = currLine;
						} else {
							// System.out.println("ERROR " + fWord + " already
							// exists at " + index + ".\n");
						}
						// Searches for the current line if it is only one word
					} else {
						if (itemFoundAt >= 0) {
						} else {
							// System.out.println("ERROR " + currLine + " not
							// found.\n");
						}
					}
				}

				buffReader.close();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}// Hash

	public int HashGen(String key, int arrSize) {
		int hashVal = 0;

		for (int i = 0; i < key.length(); i++) {
			int letter = key.charAt(i);
			hashVal = (hashVal * 26 + letter) % arrSize;
		}

		return hashVal;
	}// HashGen
	
	// Searches for the target in the array
	public int Search(String target) {		
		boolean stop = false;
		
		int index = HashGen(target, hashArr.length);
		int found = -1;
		

		while(!stop){
			if (index >= hashArr.length){
				index = 0;
			}
			
			if (target.equals(tool.GetFirst(hashArr[index]))) {
				found = index;
				stop = true;
			}
			
			if (hashArr[index] == null){
				stop = true;
			}
			index++;
		}
		return found;
	}// Search

	public String GetLine(int target) {
		String line;

			line = hashArr[target];

		return line;
	}// GetLine
}// Hasher
