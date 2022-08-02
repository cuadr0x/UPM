package pdl;

public class Token {
	boolean number = false;
	String id;
	String valueString = "";
	int valueInt;
	int num_linea;
	
	public Token(String id, String valueString, int num_linea) {
		this.id = id;
		this.valueString = valueString;
		this.num_linea =  num_linea;
	}
	
	public Token(String id, int num_linea) {
		this.id = id;
		this.num_linea =  num_linea;
	}
	
	public Token(String id, int valueInt, int num_linea) {
		this.id = id;
		this.valueInt = valueInt;
		number = true;
		this.num_linea =  num_linea;
	}
	
	public String getId() {
		return id;
	}
	public int getValueInt() {
		return valueInt;
	}
	
	public String getLinea() {
		return String.valueOf(num_linea);
	}
	public String toString() {
		if(number) {
			return String.format("<%s, %d>", id, valueInt);
		}
		return String.format("<%s, %s>", id, valueString);
	}
}
