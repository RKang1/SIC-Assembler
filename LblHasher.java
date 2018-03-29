
public class LblHasher {
	LinkedList inputList;
	LblObj[] lblArr;

	public void Hash(LinkedList inList) {
		inputList = inList;
		ToolBag tool = new ToolBag();

		
		// Finds the next prime number at least twice the number of
		// labels in the linked list
		int primeNum = tool.GetPrime(2 * inputList.numLabel);

		lblArr = new LblObj[primeNum];
		Link currLink = inputList.first;
		String linkLbl;
		LblObj currLblObj;
		int itemFoundAt;
		int index;

		do {
			currLblObj = new LblObj();
			linkLbl = currLink.label;

			if (linkLbl.length() > 0) {

				// Checks if the label already exists in the array
				itemFoundAt = Search(linkLbl);

				// Hash an index from the label
				index = HashGen(linkLbl, lblArr.length);

				if (itemFoundAt < 0) {

					// Linear probing
					if (lblArr[index] != null) {
						for (; lblArr[index] != null; index++) {
							if (index >= lblArr.length - 1) {
								index = -1;
							}
						}
					}
					currLblObj.label = linkLbl;
					currLblObj.address = currLink.address;
					lblArr[index] = currLblObj;
				} else {
					System.out.println(
							"ERROR: \"" + linkLbl + "\" is a duplicate label, only the first will be stored.\n");
				}
			}
			currLink = currLink.next;
		} while (currLink != inputList.first);

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
		int found = -1;
		int index = HashGen(target, lblArr.length);

		while (!stop) {
			if (index >= lblArr.length) {
				index = 0;
			}

			if (lblArr[index] != null && !lblArr[index].label.isEmpty()) {
				if (target.equals(lblArr[index].label)) {
					found = index;
					stop = true;
				}
			} else {
				stop = true;
			}

			index++;
		}
		return found;
	}// Search

	// Prints the array
	public void Print() {
		for (int i = 0; i < lblArr.length; i++) {
			if (lblArr[i] != null) {
				System.out.print("\t" + i);
				lblArr[i].Print();
			}
		}
	}// Print

}// LblHasher
