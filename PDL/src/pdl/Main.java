package pdl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String s = "";
		try {
			FileWriter myWriter = new FileWriter("Tokens.txt");
			myWriter.write(s);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("Ha habido un error al escribir en el fichero Tokens.txt");
			e.printStackTrace();
		}
		try {
			FileWriter myWriter = new FileWriter("TablaSimbolos.txt");
			myWriter.write(s);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("Ha habido un error al escribir en el fichero TablaSimbolos.txt");
			e.printStackTrace();
		}
		try {
			FileWriter myWriter = new FileWriter("Parse.txt");
			myWriter.write(s);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("Ha habido un error al escribir en el fichero Parse.txt");
			e.printStackTrace();
		}
		try {
			FileWriter myWriter = new FileWriter("Errors.txt");
			myWriter.write(s);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("Ha habido un error al escribir en el fichero Errors.txt");
			e.printStackTrace();
		}
		
		
		File file = new File("Errors.txt");
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);
		AnSintactico an = new AnSintactico();
		an.analizar();
	}

}
