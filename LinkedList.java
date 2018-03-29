import java.io.BufferedWriter;
import java.io.FileWriter;

public class LinkedList {
	Link first;
	Link last;
	int numLabel = 0;

	// Inserts link at the end
	public void InsertLast(String inputLine) {
		Link newLink = new Link(inputLine);

		if (first == null) {
			first = newLink;
			last = newLink;
			newLink.next = newLink;
			newLink.prev = newLink;
		} else {
			last.next = newLink;
			newLink.prev = last;
			last = newLink;
			last.next = first;
			first.prev = last;
		}
	}

	public void PrintLast() {
		last.PrintLink();
	}// PrintLast

	public void PrintAll() {
		Link current = first;

		do {
			current.PrintLink();
			current = current.next;
		} while (current != first);
	}// PrintAll

	public void PrintAllP2() {
		Link current = first;

		do {
			current.PrintLinkP2();
			current = current.next;
		} while (current != first);
	}// PrintAllP2

	public void PrintLst(String fileName) {
		Link current = first;
		String headers;

		// Removes current extension from file name
		for (int i = 0; i < fileName.length(); i++) {
			if (fileName.charAt(i) == '.') {
				fileName = fileName.substring(0, i);
				i = fileName.length();
			}
		}
		fileName += ".lst";

		headers = String.format("%-5s", "");
		headers += String.format("%-6s", "Loc");
		headers += String.format("%-14s", "Object Code");
		headers += "Source Code";
//		System.out.println(headers);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(headers + "\n");
			writer.close();

			do {
				current.PrintLinkLst(fileName);
				current = current.next;
			} while (current != first);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// PrintLst

	public void PrintObj(String fileName) {
		Link current = first;

		// Removes current extension from file name
		for (int i = 0; i < fileName.length(); i++) {
			if (fileName.charAt(i) == '.') {
				fileName = fileName.substring(0, i);
				i = fileName.length();
			}
		}
		fileName += ".obj";
//		System.out.println("\n\n");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(String.format("%06X", first.address) + "\n");
			writer.close();
			do {
				current.PrintLinkObj(fileName, last);
				current = current.next;
			} while (current != first);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// PrintObj

}
