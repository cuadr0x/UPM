package pdl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnLexico {
	ArrayList<Token> tokens;
	Map<String, String> literals;
	Map<String, String> pReservadas;
	public AnLexico() {
		
		tokens = new ArrayList<Token>();
		literals = new HashMap<String, String>();
		pReservadas = new HashMap<String, String>();
		literals.put("+", "suma");
		literals.put(":", "dospuntos");
		literals.put(";", "puntocoma");
		literals.put("(", "abreparentesis");
		literals.put(")", "cierraparentesis");
		literals.put("{", "abrellave");
		literals.put("}", "cierrallave");
		literals.put(",", "coma");
		literals.put("-", "menos");
		
		pReservadas.put("switch", "switch");
		pReservadas.put("case", "case");
		pReservadas.put("break", "break");
		pReservadas.put("if", "if");
		pReservadas.put("function", "function");
		pReservadas.put("return", "return");
		pReservadas.put("string", "string");
		pReservadas.put("int", "int");
		pReservadas.put("let", "let");
		pReservadas.put("boolean", "boolean");
		pReservadas.put("print", "print");
		pReservadas.put("input", "input");
		
		
		sc = new Scanner(System.in);
		contador = 1;
		todas = new ArrayList<TablaSimbolos>();
		ts = new TablaSimbolos("main", contador);
		todas.add(ts);
		parametros = new ArrayList<String>();
		tupla = new ArrayList<String>();		
		i = 0;
		num_lineas = 1;
		nextChar = true;
		c = '0';
		cte_entera = 1;
		lexema = "";
		id = "";
		contadorLlaves = 0;
		contadorLlavesFinal = -1;
		zonaDeclaracion = false;
		zonaDeclaracionFuncion = false;
		zonaAtributos = false;
		//ALFABETOS
		d = "0123456789";
		a = "abcdefghijklmnñopqrstuvwxyz";
		aExtendido = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
		
		//ESTADO INICIAL
		estado = 0;
		File file = new File("Prueba.txt");
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sc.useDelimiter("");
	}
	private void inicializar() {
		
		literals.put("+", "suma");
		literals.put(":", "dospuntos");
		literals.put(";", "puntocoma");
		literals.put("(", "abreparentesis");
		literals.put(")", "cierraparentesis");
		literals.put("{", "abrellave");
		literals.put("}", "cierrallave");
		literals.put(",", "coma");
		literals.put("-", "menos");
		
		pReservadas.put("switch", "switch");
		pReservadas.put("case", "case");
		pReservadas.put("break", "break");
		pReservadas.put("if", "if");
		pReservadas.put("function", "function");
		pReservadas.put("return", "return");
		pReservadas.put("string", "string");
		pReservadas.put("int", "int");
		pReservadas.put("let", "let");
		pReservadas.put("boolean", "boolean");
		pReservadas.put("print", "print");
		pReservadas.put("input", "input");
		
		todas.add(ts);
		sc = new Scanner(System.in);
		sc.useDelimiter("");
		contador = 1;
		ArrayList<TablaSimbolos> todas = new ArrayList<TablaSimbolos>();
		TablaSimbolos ts = new TablaSimbolos("main", contador);
		TablaSimbolos funcion;
		ArrayList<String> parametros = new ArrayList<String>();
		ArrayList<String> tupla = new ArrayList<String>();		
		i = 0;
		num_lineas = 1;
		nextChar = true;
		c = '0';
		cte_entera = 1;
		String lexema = "";
		String id = "";
		contadorLlaves = 0;
		contadorLlavesFinal = -1;
		zonaDeclaracion = false;
		zonaDeclaracionFuncion = false;
		zonaAtributos = false;
		//ALFABETOS
		d = "0123456789";
		a = "abcdefghijklmnñopqrstuvwxyz";
		aExtendido = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
		
		//ESTADO INICIAL
		estado = 0;
	}
	
	int contador = 1;
	ArrayList<TablaSimbolos> todas;
	TablaSimbolos ts;
	TablaSimbolos funcion;
	ArrayList<String> parametros;
	ArrayList<String> tupla;		
	int i;
	int num_lineas;
	boolean nextChar;
	char c;
	int cte_entera;
	String lexema;
	String id;
	int contadorLlaves;
	int contadorLlavesFinal;
	boolean zonaDeclaracion;
	boolean zonaDeclaracionFuncion;
	boolean zonaAtributos;
	Token tokenReturn;
	//ALFABETOS
	String d;
	String a;
	String aExtendido;
	
	//ESTADO INICIAL
	int estado;
	
	//SCANNER
	Scanner sc;
	
	public TokenTS genToken(TablaSimbolos TSActual) {
		while(sc.hasNext() || !nextChar) {
			if(nextChar) {
				c = sc.next().charAt(0);
			}
			else {
				nextChar = true;
			}
			switch(estado) {
			case 0:
				if((int)c == 32 || (int)c == 13 || (int)c == 10 || (int)c == 9) {
					if((int)c == 10) {
						num_lineas++;
					}		
				}
				else if(literals.containsKey(String.valueOf(c))) {
					if(c == ';') {
						zonaDeclaracion = false;
					}
					if(c == '{' && zonaDeclaracionFuncion) {
						zonaDeclaracionFuncion = false;
						zonaAtributos = false;
						contadorLlavesFinal = contadorLlaves;
						contadorLlaves += 1;
						TSActual.addParam(parametros);
					}
					if(c == '}') {
						contadorLlaves -= 1;
						if(contadorLlaves == contadorLlavesFinal) {
							ts = todas.get(0);
							contadorLlavesFinal = -1;
						}
					}
					if(zonaDeclaracionFuncion && c == '(') {
						zonaAtributos = true;
						parametros.clear();
					}
					return new TokenTS(TSActual, new Token(literals.get(String.valueOf(c)), num_lineas));
				}
				else if(d.indexOf(c) != -1) {
					cte_entera =  Character.getNumericValue(c);
					estado = 8;
				}
				else if(c == '"') {
					estado = 11;
				}
				else if(c == '=') {
					estado = 13;
				}
				else if(c == '|') {
					estado = 16;
				}
				else if(aExtendido.indexOf(c) != -1) {
					id = Character.toString(c);
					estado = 19;
				}
				else if(c == '/') {
					estado = 21;
				}
				else {
					System.err.println("ERROR en linea " + num_lineas + ": Caracter incorrecto " + c);
					System.exit(-1);
				}
				break;
			case 8:
				if(d.indexOf(c) != -1) {
					cte_entera = 10*cte_entera + Character.getNumericValue(c);
					
				}
				else {
					//TOKEN
					Token token = null;
					if(cte_entera >= 32767) {
					
						System.err.println("Error en linea "+ num_lineas +": El valor del entero " + cte_entera + " no está dentro del rango de valores de enteros (-32767, 32767)");
						System.exit(-1);
					}
					else {
						token = (new Token("cteentera", cte_entera, num_lineas));
					}
					estado = 0;
					nextChar = false;
					return new TokenTS(TSActual, token);
				}
				break;
			case 11:
				if(c != '"') {
					lexema += c;
				} //MAL
				else if(c == '"') {
					lexema = '"' + lexema + '"';
					String aux = lexema;
					lexema = "";
					estado = 0;	
					return new TokenTS(TSActual, new Token("cadena", aux, num_lineas));
				}
				break;
			case 13:
				if(c == '=') {
					estado = 0;
					return new TokenTS(TSActual, new Token("oprelacional", num_lineas));
				}
				else {
					estado = 0;
					nextChar = false;
					return new TokenTS(TSActual, new Token("asignacion1", num_lineas));
				}
			case 16:
				if(c == '|') {
					estado = 0;
					return new TokenTS(TSActual, new Token("ologico", num_lineas));
				}
				else if(c == '=') {
					zonaDeclaracion = false;
					estado = 0;
					return new TokenTS(TSActual, new Token("asignacion2", num_lineas));
				}
				else {
					System.err.println("ERROR en linea " + num_lineas + ": Caracter incorrecto " + c);
					System.exit(-1);
				}
				break;
			case 19:
				if(aExtendido.indexOf(c) != -1 | d.indexOf(c) != -1 | c == '_') { 
					id += c;
				}
				else {
					Token token;
					if(zonaDeclaracionFuncion && !zonaAtributos && !(id.equals("int") || id.equals("boolean") || id.equals("string"))) {
						TSActual.add(new EntradasTS(id, true));
					}
					if(pReservadas.containsKey(id)) {
						if(id.equals("let")) {
							zonaDeclaracion = true;
						}
						else if(id.equals("function")) {
							tupla.clear();
							zonaDeclaracionFuncion = true;
						}
						token =  (new Token(pReservadas.get(id), num_lineas)); //PROBLEMAS
					}
					else {
						boolean existe = false;
						for(EntradasTS entrada: TSActual.tablaSimbolos) {
							if(entrada.getLexema().equals(id)) {
								existe = true;
							}
						}
						if(zonaDeclaracion && existe) {
							System.err.println("ERROR en linea " + num_lineas + ": Variable " + id + " ya declarada previamente");
							System.exit(-1);
						}
						else if (!existe && !zonaDeclaracion && !zonaDeclaracionFuncion){ 
							TSActual.add(new EntradasTS(id, true, true));
							//System.out.println(TSActual);
						}
						else if(!existe && !zonaDeclaracionFuncion) {	
							TSActual.add(new EntradasTS(id));
						}
						else if(zonaDeclaracionFuncion && zonaAtributos && !(id.equals("int") || id.equals("boolean") || id.equals("string"))){
							TSActual.add(new EntradasTS(id));
						}
						
						 token = (new Token("id", TSActual.getPosition(id), num_lineas)); 
					}
					estado = 0;
					nextChar = false;
					return new TokenTS(TSActual, token);
				}
				break;
			case 21:
				if(c == '*') {
					estado = 22;
				}
				else {
					System.err.println("ERROR en linea " + num_lineas + ": Caracter incorrecto " + c);
					System.exit(-1);
				}
				break;
			case 22:
				if(c != '*') {}
				else {
					estado = 23;
				}
				break;
			case 23:
				if(c == '/') {
					estado = 0;
				}
				else {
					estado = 22;
				}
				break;
			}
		}
		sc.close();
		return new TokenTS(TSActual, new Token("$", num_lineas));
	}
	

}
