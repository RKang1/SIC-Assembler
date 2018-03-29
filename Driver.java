import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Driver {
	public static void main(String[] args) {
		try {
			File sicops = new File("sicops.txt");
			File input = new File(args[0]);

			Hasher hashTool = new Hasher(sicops);

			hashTool.Hash();

			int address = 0;
			ToolBag tool = new ToolBag();
			String line;
			String opName;
			String opLine;
			String opType;
			String operand;
			int foundAt = -1;
			LinkedList sicInstr = new LinkedList();
			Link current;
			LblHasher lblHash = new LblHasher();
			int opCode;
			int lineCnt = 0;

			// Pass 1
			BufferedReader buffReader = new BufferedReader(new FileReader(input));

			while ((line = buffReader.readLine()) != null) {
				lineCnt++;
				opName = "";
				opType = "";

				// Determines op name and type if the line is not a comment or
				// blank
				if (!tool.TextChk(line)) {
					if (line.length() > 18) {
						opName = line.substring(9, 17).replaceAll("\\s+", "");
					} else {
						opName = line.substring(9).replaceAll("\\s+", "");
					}
					opType = tool.OpType(opName);

					// Sets the starting address if START is the op name
					if (opName.equals("START")) {
						address = Integer.parseInt(line.substring(19, 29).replaceAll("\\s+", ""), 16);
					}

					// Searches for the op name in the symbol table from
					// sicops.txt
					foundAt = hashTool.Search(opName);

					// Creates a link from the current line of the input file
					// and
					// adds it to the linked list

					switch (opType) {
					case "sicop":
						sicInstr.InsertLast(line);
						foundAt = hashTool.Search(opName);
						opLine = hashTool.GetLine(foundAt);
						sicInstr.last.addyInc = opLine.charAt(14) - '0';
						address += sicInstr.last.prev.addyInc;
						sicInstr.last.address = address;
						sicInstr.last.opType = opType;
						sicInstr.last.lineNum = lineCnt;
						break;

					case "directive":
						sicInstr.InsertLast(line);
						sicInstr.last.addyInc = 0;
						address += sicInstr.last.prev.addyInc;
						sicInstr.last.address = address;
						sicInstr.last.opType = opType;
						sicInstr.last.lineNum = lineCnt;
						break;

					case "storage":
						sicInstr.InsertLast(line);
						if (sicInstr.last.ComChk()) {
							operand = line.substring(19, 29).replaceAll("\\s+", "");
						} else {
							operand = line.substring(19).replaceAll("\\s+", "");
						}
						sicInstr.last.addyInc = tool.StorCalc(opName, operand, lineCnt);
						address += sicInstr.last.prev.addyInc;
						sicInstr.last.address = address;
						sicInstr.last.opType = opType;
						sicInstr.last.lineNum = lineCnt;
						break;

					case "unknown":
						System.out.println("Error: \"" + opName
								+ "\" is an invalid mneumonic, this instruction will be ignored.\n");
						sicInstr.last.opType = opType;
						break;

					default:
						break;
					}

					if (sicInstr.last != null && sicInstr.last.LblChk()) {
						sicInstr.numLabel++;
					}
				} else {
					sicInstr.InsertLast(line);
					sicInstr.last.addyInc = 0;
					address += sicInstr.last.prev.addyInc;
					sicInstr.last.opType = "comment";
					sicInstr.last.lineNum = lineCnt;
				}
			} // while loop

			lblHash.Hash(sicInstr);

			buffReader.close();

			// Pass 2
			buffReader = new BufferedReader(new FileReader(input));
			@SuppressWarnings("unused")
			boolean Nflag;
			@SuppressWarnings("unused")
			boolean Iflag;
			@SuppressWarnings("unused")
			boolean Xflag;
			boolean Bflag;
			@SuppressWarnings("unused")
			boolean Pflag;
			boolean Eflag;
			int xbpe;
			int displacement = 0;
			int targetAddr;
			int baseAddr = -1;
			String targetLbl;
			char byteType;
			String byteArg;
			int bArgEnd = 3;

			current = sicInstr.last;
			do {
				current = current.next;
				xbpe = 0;
				targetAddr = 0;
				targetLbl = current.operand;
				Nflag = false;
				Iflag = false;
				Xflag = false;
				Bflag = false;
				Pflag = false;
				Eflag = false;

				switch (current.opType) {
				case "sicop":
					// Finds command in sicops symtab
					foundAt = hashTool.Search(current.command);

					// Gets line from sicops symtab containg command
					opLine = hashTool.GetLine(foundAt);

					// Determines opcode from the line
					opCode = Integer.parseInt(opLine.substring(8, 10), 16);

					// Handles N & I flags
					switch (current.operand.charAt(0)) {
					case '@':
						Nflag = true;
						opCode += 2;
						targetLbl = targetLbl.substring(1);
						break;

					case '#':
						Iflag = true;
						opCode += 1;
						targetLbl = targetLbl.substring(1);
						break;

					default:
						Nflag = true;
						Iflag = true;
						opCode += 3;
						break;
					}

					// Handles E flag
					if (current.command.charAt(0) == '+') {
						Eflag = true;
						xbpe = 1;
					}

					// Handles X flag
					for (int i = 0; i < targetLbl.length(); i++) {
						if (targetLbl.charAt(i) == ',') {
							if (targetLbl.charAt(i + 1) == 'X') {
								Xflag = true;
								xbpe += 8;
							}
							targetLbl = targetLbl.substring(0, i);
						}
					}

					targetAddr = lblHash.lblArr[lblHash.Search(targetLbl)].address;

					// Calculates displacement if not extended instruction
					// Handles B and P flags
					if (!Eflag) {
						displacement = targetAddr - current.next.address;

						if (displacement < -2048 || displacement > 2047) {
							Bflag = true;
							xbpe += 4;
						} else {
							Pflag = true;
							xbpe += 2;
						}
					}

					if (Bflag) {
						displacement = targetAddr - baseAddr;
					}

					if (Eflag) {
						current.objCode = String.format("%02X", opCode) + String.format("%01X", xbpe)
								+ String.format("%05X", (0xFFFFF & targetAddr));
					} else {
						current.objCode = String.format("%02X", opCode) + String.format("%01X", xbpe)
								+ String.format("%03X", (0xFFF & displacement));
					}
					break;

				case "directive":
					if (current.command.equals("BASE")) {
						baseAddr = lblHash.lblArr[lblHash.Search(current.operand)].address;
					}
					break;

				case "storage":
					if (current.command.equals("WORD")) {
						current.objCode = String.format("%06X", Integer.parseInt(current.operand));
					}

					if (current.command.equals("BYTE")) {
						byteType = current.operand.charAt(0);
						for (int i = 2; i < current.operand.length(); i++) {
							if (current.operand.charAt(i) == '\'') {
								bArgEnd = i;
							}
						}
						byteArg = current.operand.substring(2, bArgEnd);

						if (byteType == 'C') {
							current.objCode = String.format("%02X", (int) byteArg.charAt(0));
							for (int i = 1; i < byteArg.length(); i++) {
								current.objCode += String.format("%02X", (int) byteArg.charAt(i));
							}
						}

						if (byteType == 'X') {
							current.objCode = byteArg;
						}
					}
					break;

				case "comment":
					break;

				case "unknown":
					break;

				default:
					break;
				}

			} while (current != sicInstr.last);

			sicInstr.PrintLst(input.toString());

			sicInstr.PrintObj(input.toString());

			System.out.println(".lst and .obj files created.");

			buffReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// main

}// Driver