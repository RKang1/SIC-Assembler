import java.io.BufferedWriter;
import java.io.FileWriter;

public class Link {
	Link next;
	Link prev;

	int address = -1;
	int addyInc = 0;
	String label = "";
	String command;
	String operand;
	String comment;
	String objCode = "";
	String opType;
	int lineNum;

	public Link(String line) {
		ToolBag tool = new ToolBag();
		if (tool.TextChk(line)) {
			comment = line.trim();
		} else {
			label = line.substring(0, 8).replaceAll("\\s+", "");
			if (line.length() > 18) {
				command = line.substring(9, 17).replaceAll("\\s+", "");
			} else {
				command = line.substring(9).replaceAll("\\s+", "");
			}
			if (line.length() < 30) {

				if (line.length() > 18) {
					operand = line.substring(18).replaceAll("\\s+", "");
				} else {
					operand = "";
				}

				comment = "";
			} else {
				operand = line.substring(18, 29).replaceAll("\\s+", "");
				comment = line.substring(31).trim();
			}
		}

	}// Link

	public void PrintLink() {
		if (address > -1) {
			System.out.print(String.format("%04X", address) + "\t" + label + '\t' + command + '\t' + operand + '\t'
					+ comment + '\n');
		} else {
			System.out.println(comment);
		}
	}// PrintLink

	public void PrintLinkP2() {
		if (address > -1) {
			if (objCode.length() == 8) {
				System.out.print(
						"" + String.format("%03d", lineNum) + '\t' + objCode + " " + String.format("%04X", address)
								+ "\t" + label + '\t' + command + '\t' + operand + '\t' + comment + '\n');
			} else {
				System.out.print(
						"" + String.format("%03d", lineNum) + '\t' + objCode + "\t " + String.format("%04X", address)
								+ "\t" + label + '\t' + command + '\t' + operand + '\t' + comment + '\n');
			}
		} else {
			System.out.println("" + String.format("%03d", lineNum) + "\t\t\t" + comment);
		}
	}// PrintLinkP2

	public void PrintLinkLst(String fileName) {
		String line;

		if (address > -1) {
			line = String.format("%-5s", String.format("%03d", lineNum));
			line += String.format("%-6s", String.format("%04X", address));
			line += String.format("%-14s", objCode);
			line += String.format("%-10s", label);
			line += String.format("%-6s", command);
			line += String.format("%-10s", operand);
			line += comment;
		} else {
			line = String.format("%-25s", String.format("%03d", lineNum));
			line += comment;
		}
//		System.out.println(line);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.write(line + '\n');
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// PrintLinklst

	public void PrintLinkObj(String fileName, Link last) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			if (objCode.length() > 0) {
				writer.write(objCode + '\n');
//				System.out.println(objCode);
			}
			if (this == last){
				writer.write("!");
//				System.out.println("!");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// PrintLinkObj

	public boolean LblChk() {
		boolean flag = false;
		if (label != null && label.length() > 0) {
			flag = true;
		}
		return flag;
	}// LblChk

	public boolean ComChk() {
		boolean flag = false;
		if (!comment.equals("")) {
			flag = true;
		}
		return flag;
	}

}
