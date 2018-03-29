
public class LblObj {
	String label;
	int address;
	String use = "main";
	String csect = "main";
	
	public void Print(){
		System.out.println("\t" + label + '\t' + String.format("%03X", address) + '\t' + use + '\t' + csect);
	}// Print
}// LblObj
