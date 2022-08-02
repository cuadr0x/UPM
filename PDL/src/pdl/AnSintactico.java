package pdl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnSintactico {
		String sig_token;
		int idPosTS;
		ArrayList<String> firstB;
		ArrayList<String> firstF;
		ArrayList<String> firstS;
		ArrayList<String> firstE;
		ArrayList<String> firstT;
		ArrayList<String> firstVp;
		ArrayList<String> followX;
		ArrayList<String> followC;
		ArrayList<String> followL;
		ArrayList<String> followQ;
		ArrayList<String> followH;
		ArrayList<String> followA;
		ArrayList<String> followK;
		ArrayList<String> followEp;
		ArrayList<String> followRp;
		ArrayList<String> followUp;
		ArrayList<String> followN;
		ArrayList<String> followZ;
		ArrayList<Token> tokens;
		int contador;
		int DespAct;
		int contadorTS;
		ArrayList<TablaSimbolos> ts;
		TablaSimbolos TSActual;
		Map<String, Integer> tamanos;
		TablaSimbolos TSG;
		TablaSimbolos Temporal;
		boolean temporalenuso;
		int DespG;
		int funcionPosTSG;
		String TiposFuncion;
		AnLexico al;
		Token tokenActual;
		//output
		ArrayList<Token> tokenList;
		ArrayList<TablaSimbolos> tablasDeSimbolos;
		String parseCompleto;
		
		public AnSintactico() {
			firstB = new ArrayList<String>(Arrays.asList("let", "if", "id", "return", "print", "input", "switch"));
			firstF = new ArrayList<String>(Arrays.asList("function"));
			firstS = new ArrayList<String>(Arrays.asList("id", "return", "print", "input"));
			firstE = new ArrayList<String>(Arrays.asList("id", "cteentera", "cadena", "abreparentesis"));
			firstT = new ArrayList<String>(Arrays.asList("int", "boolean", "string"));
			firstVp = new ArrayList<String>(Arrays.asList("cteentera", "cadena"));
			followX = new ArrayList<String>(Arrays.asList("puntocoma"));
			followC = new ArrayList<String>(Arrays.asList("cierrallave", "case"));
			followL = new ArrayList<String>(Arrays.asList("cierraparentesis"));
			followQ = new ArrayList<String>(Arrays.asList("cierraparentesis"));
			followH = new ArrayList<String>(Arrays.asList("abreparentesis"));
			followA = new ArrayList<String>(Arrays.asList("cierraparentesis"));
			followK = new ArrayList<String>(Arrays.asList("cierraparentesis"));
			followEp = new ArrayList<String>(Arrays.asList("cierraparentesis", "coma", "puntocoma"));
			followRp = new ArrayList<String>(Arrays.asList("ologico", "cierraparentesis", "coma", "puntocoma"));
			followUp = new ArrayList<String>(Arrays.asList("oprelacional", "ologico", "cierraparentesis", "coma", "puntocoma"));
			followN = new ArrayList<String>(Arrays.asList("suma", "oprelacional", "ologico", "cierraparentesis", "coma", "puntocoma"));
			followZ = new ArrayList<String>(Arrays.asList("cierrallave"));
			sig_token = null;
			contador = 1;
			contadorTS = 1;
			Temporal = TSG;
			tamanos = new HashMap<String, Integer>();
			tamanos.put("entero", 2);
			tamanos.put("logico", 2);
			tamanos.put("cadena", 128);
			al = new AnLexico();
			tokenList = new ArrayList<Token>();
			tablasDeSimbolos = new ArrayList<TablaSimbolos>();
			parseCompleto = "";
			temporalenuso = false;
		}
		
		public void output() {

			try {
				String s = "";
				for(Token i: tokenList) {
					s += i + "\n";
				}
				FileWriter myWriter = new FileWriter("Tokens.txt");
				myWriter.write(s);
				myWriter.close();
			} catch (IOException e) {
				System.out.println("Ha habido un error al escribir en el fichero Tokens.txt");
				e.printStackTrace();
			}
			try {
				String s = "";
				for(TablaSimbolos i: tablasDeSimbolos) {
					s += i + "\n";
				}
				FileWriter myWriter = new FileWriter("TablaSimbolos.txt");
				myWriter.write(s);
				myWriter.close();
			} catch (IOException e) {
				System.out.println("Ha habido un error al escribir en el fichero TablaSimbolos.txt");
				e.printStackTrace();
			}
			try {
				FileWriter myWriter = new FileWriter("Parse.txt");
				myWriter.write(parseCompleto);
				myWriter.close();
			} catch (IOException e) {
				System.out.println("Ha habido un error al escribir en el fichero Parse.txt");
				e.printStackTrace();
			}
			
		}
		public String Alex() {

			contador++;
			if(temporalenuso) {
				TSActual = Temporal;
				temporalenuso = false;
			}
			TokenTS tokents = al.genToken(TSActual);
			TSActual = tokents.ts;
			
			Token token = tokents.t;
			tokenList.add(token);
			if(token.getId().equals("id")) {
				idPosTS = token.getValueInt();
				boolean encontrado = false;
				boolean encontradoG = false;
				/*if(TSActual.getLexemaPos(idPosTS).equals("num"))
					System.out.println(TSG);*/
				for(int i = 0; i < TSG.tablaSimbolos.size(); i++) {
					if(TSActual.getEntrada(idPosTS).lexema.equals(TSG.getEntrada(i).lexema) && !TSActual.equals(TSG)) {
						encontrado = true;
					}
					if(TSActual.getEntrada(idPosTS).lexema.equals(TSG.getEntrada(i).lexema) && TSActual.equals(TSG) && !(idPosTS < TSActual.tablaSimbolos.size())) {
						encontradoG = true;
					}
				}
				
				if(TSActual.getEntrada(idPosTS).noDeclarado) {
					if(TSG.equals(TSActual) && TSActual.getEntrada(idPosTS).nuevo) {
						TSActual.insertarTipoTS(idPosTS, "entero", DespAct);
						DespAct += 2;
						TSActual.getEntrada(idPosTS).nuevo = false;
					}
					else if (!encontrado && !TSG.equals(TSActual)) {
						TSG.add(TSActual.getEntrada(idPosTS));
						int pts = TSG.getPosition(TSActual.getEntrada(idPosTS).lexema);
						TSG.insertarTipoTS(pts, "entrada", DespG);
						TSActual.tablaSimbolos.remove(idPosTS);
						DespG += 2;
						idPosTS = pts;
						temporalenuso = true;
						Temporal = TSActual;
						TSActual = TSG;
					}
				}
			}
			tokenActual = token;
			return token.getId();
			
		}
		
		public String error() {
			switch(sig_token) {
				case "$": sig_token = "EOF"; break;
				case "asignacion1": sig_token = "="; break;
				case "asignacion2": sig_token = "|="; break;
				case "oprelacional": sig_token = "=="; break;
				case "ologico": sig_token = "||"; break;
				case "cierraparentesis": sig_token = ")"; break;
				case "abreparentesis": sig_token = "("; break;
				case "abrellave": sig_token = "{"; break;
				case "cierrallave": sig_token = "}"; break;
				case "menos": sig_token = "-"; break;
				case "suma": sig_token = "+"; break;
				case "dospuntos": sig_token = ":"; break;
				case "puntocoma": sig_token = ";"; break;
				case "coma": sig_token = ";"; break;
				case "cadena": sig_token = tokenActual.valueString; break;
				case "cteentera": sig_token = String.valueOf(tokenActual.valueInt); break;
				case "id": sig_token = TSActual.getEntrada(idPosTS).lexema; 
							for(int i = 0; i < TSG.tablaSimbolos.size(); i++) {
								if(TSActual.getEntrada(idPosTS).lexema.equals(TSG.getEntrada(i).lexema) && TSActual.getEntrada(idPosTS).noDeclarado) {
									sig_token = TSG.getEntrada(i).lexema;
								}
							}
							break;   // GLOBAL CUIDAO
			}
			return "ERROR en linea " + tokenActual.getLinea() + ": Error sentencia no esperada \"" + sig_token +"\"";
		}
		
		
		public void analizar() {
			//SEMANTICO
			//System.out.print("Des ");
			parseCompleto += "Des ";
			TSG = new TablaSimbolos("main", contadorTS);
			contadorTS++;
			DespG = 0;
			TSActual = TSG;
			DespAct = DespG;
			//SEMANTICO
			sig_token = Alex();
			P();
			//SEMANTICO
			tablasDeSimbolos.add(TSActual);
			//System.out.println("\n" + TSActual);
			TSG = null;
			TSActual = null;
			//SEMANTICO
			for(Token e: tokenList) {
				//System.out.println(e);
			}
			if(!sig_token.equals("$")) {
				System.err.println("Error en linea "+ tokenActual.num_linea + ": El Analisis sintactico ha terminado sin llegar al final del fichero"); //CAMBIARLO
				System.exit(-1);
			}
			else {
				//System.out.println("CORRECTO");
			}
			output();	
		}
		
		public void equipara(String terminal) {
			if(terminal == sig_token) {
				sig_token = Alex();
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
		}
		
		public void P() {
			if(firstB.contains(sig_token)) {
				//System.out.print("1 ");
				parseCompleto += "1 ";
				B();
				P();
			}
			else if(firstF.contains(sig_token)) {
				//System.out.print("2 ");
				parseCompleto += "2 ";
				F();
				P();

			}
			else if(sig_token.equals("$")) {
				//System.out.print("3 ");
				parseCompleto += "3 ";
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
		}
		public String B() {
			String Btipo = "tipo_ok";
			if(sig_token.equals("let")) {
				//System.out.print("4 ");
				parseCompleto += "4 ";
				equipara("let");
				//SEMANTICO SINTACTICO
				String Ttipo = T();
				//SEMANTICO SINTACTICO
				equipara("id");
				//SEMANTICO
				TSActual.insertarTipoTS(idPosTS, Ttipo, DespAct);
				DespAct += tamanos.get(Ttipo);
				//SEMANTICO
				equipara("puntocoma");
				
			}
			else if(sig_token.equals("if")) {
				//System.out.print("5 ");
				parseCompleto += "5 ";
				equipara("if");
				equipara("abreparentesis");
				String Etipo = E();
				//SEMANTICO
				 if(Etipo != "logico") {
					 System.err.println("ERROR en linea " + tokenActual.getLinea() + ": La condicion del if debe ser logica");
					 System.exit(-1);
					 Btipo = "tipo_err";
				 }
				//SEMANTICO
				equipara("cierraparentesis");
				String Stipo = S();
				//SEMANTICO
				if(!Stipo.equals("tipo_ok")) {
					Btipo = "tipo_err";
				}
				//SEMANTICO
			}
			else if(firstS.contains(sig_token)) {
				//System.out.print("6 ");
				parseCompleto += "6 ";
				String Stipo = S();
				Btipo = Stipo;
			}
			else if(sig_token.equals("switch")) {
				//System.out.print("7 ");
				parseCompleto += "7 ";
				String lineaErrorSwitch = tokenActual.getLinea();
				equipara("switch");
				equipara("abreparentesis");
				String Etipo = E();
				equipara("cierraparentesis");
				equipara("abrellave");
				String Ztipo = Z();
				equipara("cierrallave");
				//SEMANTICO                              
				if(Ztipo.equals("tipo_vacio")) {
					System.err.println("ERROR en el switch-case linea " + lineaErrorSwitch + ": No se permite un switch sin case"); //PROBLEMA LINEA
					Btipo = "tipo_err";
					System.exit(-1);
				}
				else if(Etipo != Ztipo) {
					System.err.println("ERROR en el switch-case linea " + lineaErrorSwitch + ": Tipos diferentes en el case (" + Etipo + ", " + Ztipo+ ")"); //PROBLEMA LINEA
					Btipo = "tipo_err";
					System.exit(-1);
				}
				else {
					Btipo = Etipo;
				}
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Btipo;
		}
		
		public String T() {
			if(sig_token.equals("int")) {
				//System.out.print("8 ");
				parseCompleto += "8 ";
				equipara("int");
				//SEMANTICO
				return "entero";	
				//SEMANTICO
			}
			else if(sig_token.equals("boolean")) {
				//System.out.print("9 ");
				parseCompleto += "9 ";
				equipara("boolean");
				//SEMANTICO
				return "logico";
				//SEMANTICO
			}
			else if(sig_token.equals("string")) {
				//System.out.print("10 ");
				parseCompleto += "10 ";
				equipara("string");
				//SEMANTICO
				return "cadena";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return null;
		}
		
		public String S() {
			String Stipo = "tipo_ok";
			if(sig_token.equals("id")) {
				//System.out.print("11 ");
				parseCompleto += "11 ";
				TablaSimbolos TSaux = TSActual;
				equipara("id");
				int idpos = idPosTS;
				boolean encontrado = false;
				boolean esGlobal = false;
				String tipoGlobal = "";
				try {
					for(int i = 0; i < TSG.tablaSimbolos.size(); i++) {
						if(TSActual.getEntrada(idPosTS).lexema.equals(TSG.getEntrada(i).lexema) && !TSActual.equals(TSG)) {
							encontrado = true;
							if(TSG.getEntrada(i).tipo.equals("function")) {
								TSActual.tablaSimbolos.remove(idPosTS);
								Stipo = TSG.getEntrada(i).getDevuelto();
							}
							if(TSActual.getEntrada(idPosTS).noDeclarado) {
								TSActual.tablaSimbolos.remove(idPosTS); 
								esGlobal = true;
								tipoGlobal = TSG.getEntrada(i).tipo;
							}
							
						}
					}
				} catch(Exception IndexOutOfBounds) {
					
				}
				String Mtipo = M();
				//SEMANTICO
				if(esGlobal && tipoGlobal.equals(Mtipo)) {
					Stipo = tipoGlobal;
				}
				else if(esGlobal && !tipoGlobal.equals(Mtipo)) {
					Stipo = "tipo_err";
				}
				else if(!(TSaux.buscaTipoTS(idpos).equals(Mtipo)) && !TSaux.buscaTipoTS(idpos).equals("function")) {
					System.err.println("ERROR en linea " + tokenActual.num_linea + ": Asignacion de tipo incorrecta (" + TSaux.buscaTipoTS(idPosTS) + ", "+ Mtipo+")");
				}
				//SEMANTICO
			}
			else if(sig_token.equals("return")) {
				String StipoDev;
				//System.out.print("12 ");
				parseCompleto += "12 ";
				equipara("return");
				String Xtipo = X();
				equipara("puntocoma");
				//SEMANTICO              BASTANTE DUDOSO
				StipoDev = Xtipo;
				if(!TSG.getEntrada(funcionPosTSG).getDevuelto().equals(StipoDev)) {
					System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Tipo de retorno incorrecto");
					System.exit(-1);
				}
				Stipo = StipoDev;
				//SEMANTICO
			}
			else if(sig_token.equals("print")) {
				//System.out.print("13 ");
				parseCompleto += "13 ";
				equipara("print");
				equipara("abreparentesis");
				String Etipo = E();
				equipara("cierraparentesis");
				equipara("puntocoma");
				//SEMANTICO
				if(!(Etipo.equals("cadena") || Etipo.equals("entero"))) {
					System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Atributo del print() de tipo incorrecto");
					Stipo = "tipo_err";
					System.exit(-1);
				}
				//SEMANTICO
			}
			else if(sig_token.equals("input")) {
				//System.out.print("14 ");
				parseCompleto += "14 ";
				equipara("input");
				equipara("abreparentesis");
				equipara("id");
				equipara("cierraparentesis");
				equipara("puntocoma");
				//SEMANTICO
				String idTipo = TSActual.buscaTipoTS(idPosTS);
				if(!(idTipo.equals("cadena") || idTipo.equals("entero"))) {
					System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Atributo del input() de tipo incorrecto");
					Stipo = "tipo_err";
					System.exit(-1);
				}
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Stipo;
		}
		
		public String M() {
			String Mtipo = "tipo_ok";
			if(sig_token.equals("asignacion1")) {
				//	System.out.print("15 ");
				parseCompleto += "15 ";
				equipara("asignacion1");
				String Etipo = E();
				equipara("puntocoma");
				//SEMANTICO
				Mtipo = Etipo;
				//SEMANTICO
			}
			else if(sig_token.equals("abreparentesis")) {
				//System.out.print("16 ");
				parseCompleto += "16 ";
				funcionPosTSG = idPosTS;
				equipara("abreparentesis");
				String linea = tokenActual.getLinea();
				String LTipo = L();
				equipara("cierraparentesis");
				equipara("puntocoma");
				//SEMANTICO
				String idTipo = TSActual.buscaTipoTS(idPosTS);
				ArrayList<String> par = TSG.getEntrada(funcionPosTSG).parametros;
				String aux= "";
				for(String s: par) {
					aux += s;
				}
				if(aux.equals("-")) aux = "tipo_vacio";
				if(!aux.equals(LTipo)) {
					Mtipo = "tipo_err";
					System.err.println("ERROR en linea " + linea + ": Tipo parametro incorrecto");
					System.exit(-1);
				}
				Mtipo = TSG.getEntrada(funcionPosTSG).devuelto;
				//SEMANTICO
			}
			else if(sig_token.equals("asignacion2")) {
				//System.out.print("17 ");
				parseCompleto += "17 ";
				equipara("asignacion2");
				String Etipo = E();
				equipara("puntocoma");
				//SEMANTICO
				Mtipo = Etipo;
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Mtipo;
		}
		
		public String X() {
			String Xtipo = "tipo_ok";
			if(firstE.contains(sig_token)) {
				//System.out.print("18 ");
				parseCompleto += "18 ";
				String Etipo = E();
				//SEMANTICO
				Xtipo = Etipo;
				//SEMANTICO
			}
			else if(followX.contains(sig_token)) {
				//System.out.print("19 ");
				parseCompleto += "19 ";
				//SEMANTICO
				Xtipo = "tipo_vacio";
				//SEMANTICO	
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Xtipo;
		}
		
		public String C() {       //BASTANTE DUDOSO EN GENERAL
			String Ctipo = "tipo_ok";
			if(firstB.contains(sig_token)) {
				//System.out.print("20 ");
				parseCompleto += "20 ";
				Ctipo = B();
				C();
			}
			else if(sig_token.equals("break")){
				//System.out.print("21 ");
				parseCompleto += "21 ";
				equipara("break");
				equipara("puntocoma");
			}
			else if(followC.contains(sig_token)) {
				//System.out.print("22 ");
				parseCompleto += "22 ";
				//SEMANTICO
				Ctipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Ctipo;
		}
		
		public String L() {
			String Ltipo = "tipo_ok";//SIN HACER
			if(firstE.contains(sig_token)) {
				//System.out.print("23 ");
				parseCompleto += "23 ";
				int pos = idPosTS;
				String Etipo = E();
				if(Etipo.equals("function")) {
					Etipo = TSG.getEntrada(pos).devuelto;
				}
				String TiposFuncion = Etipo;
				TiposFuncion += Q();
				Ltipo = TiposFuncion;
			}
			else if(followL.contains(sig_token)) {
				//System.out.print("24 ");
				parseCompleto += "24 ";
				Ltipo = "tipo_vacio";
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Ltipo;
		}
		
		public String Q() {
			String Qtipo = "tipo_ok";
			if(sig_token.equals("coma")) {
				//System.out.print("25 ");
				parseCompleto += "25 ";
				equipara("coma");
				int pos = idPosTS;
				String Etipo = E();
				if(Etipo.equals("function")) {
					Etipo = TSG.getEntrada(pos).devuelto;
				}
				String TiposFuncion = "";
				TiposFuncion += Etipo;
				
				String res = Q();
				if(!res.equals("tipo_vacio")) {
					TiposFuncion += res;
				}
				Qtipo = TiposFuncion;
			}
			else if(followQ.contains(sig_token)) {
				//System.out.print("26 ");
				parseCompleto += "26 ";
				Qtipo  = "tipo_vacio";
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Qtipo;
		}
		
		public void F() {  
			//System.out.print("27 ");
			parseCompleto += "27 ";
			equipara("function");
			equipara("id");
			//SEMANTICO
			TablaSimbolos TSL = new TablaSimbolos(TSActual.getLexemaPos(idPosTS), contadorTS); //PONER NOMBRE TS
			contadorTS++;
			funcionPosTSG = idPosTS;
			int DespL = 0;
			DespG = DespAct;
			TSG = TSActual;
			TSActual = TSL;
			DespAct = DespL; 
			//SEMANTICO
			String Htipo = H(); 
			TSG.insertarRetorno(funcionPosTSG, Htipo);
			String etiqueta = "Et" + TSG.getEntrada(funcionPosTSG).lexema;
			TSG.insertarEtiqueta(funcionPosTSG, etiqueta);
			equipara("abreparentesis");
			A();
			equipara("cierraparentesis");
			equipara("abrellave");
			String Ctipo = C();
			tablasDeSimbolos.add(TSActual);
			TSActual = TSG;
			DespAct = DespG;
			Temporal = TSG;
			equipara("cierrallave");
		}
		
		public String H() {
			String Htipo = "tipo_ok";
			if(firstT.contains(sig_token)) {
				//System.out.print("28 ");
				parseCompleto += "28 ";
				String Ttipo = T();
				//SEMANTICO
				Htipo = Ttipo;
				//SEMANTICO
			}
			else if(followH.contains(sig_token)) {
				//System.out.print("29 ");
				parseCompleto += "29 ";
				//SEMANTICO
				Htipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Htipo;
		}
		
		public String A() {
			String Atipo = "tipo_ok";
			if(firstT.contains(sig_token)) {
				//System.out.print("30 ");
				parseCompleto += "30 ";
				String Ttipo = T();
				equipara("id");
				TSActual.insertarTipoTS(idPosTS, Ttipo, DespAct);
				DespAct += tamanos.get(Ttipo);
				String Ktipo = K();
				String tipoFuncion;
				//SEMANTICO
				if(Ktipo.equals("tipo_vacio")) {
					tipoFuncion = Ttipo;
				}
				else {
					tipoFuncion = Ttipo + " " + Ktipo;
				}
				String[] splited = tipoFuncion.split("\\s+");
				TSG.insertarTipoParamFun(funcionPosTSG, splited); 
				//SEMANTICO
			}
			else if(followA.contains(sig_token)) {
				//System.out.print("31 ");
				parseCompleto += "31 ";
				//SEMANTICO
				Atipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Atipo;
		}
		
		public String K() {
			String Ktipo = "tipo_ok";
			if(sig_token.equals("coma")) { 
				//System.out.print("32 ");
				parseCompleto += "32 ";
				equipara("coma");
				String Ttipo = T();
				TSActual.insertarTipoTS(idPosTS, Ttipo, DespAct);
				DespAct += tamanos.get(Ttipo);
				equipara("id");
				String K1tipo = K();
				if(K1tipo == "tipo_vacio") {
					Ktipo = Ttipo;
				}
				else {
					Ktipo = Ttipo + " " + K1tipo;
				}
			}
			else if(followK.contains(sig_token)) {
				//System.out.print("33 ");
				parseCompleto += "33 ";
				//SEMANTICO
				Ktipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Ktipo;
		}
		
		public String E() {
			String Etipo = "tipo_ok";
			//System.out.print("34 ");
			parseCompleto += "34 ";
			String Rtipo = R();
			String Eptipo = Ep();
			//SEMANTICO
			
			if(Eptipo.equals("tipo_vacio")){
				Etipo = Rtipo;
			}
			else if(Eptipo == "tipo_err" || !Eptipo.equals(Rtipo)) {
				System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Los operandos del ologico deben de ser logicos");
				System.exit(-1);
			}
			else if(Eptipo.equals(Rtipo)){
				Etipo = "logico";
			}
			//SEMANTICO
			return Etipo;
		}
		
		public String Ep() {
			String Eptipo = "tipo_ok";
			if(sig_token.equals("ologico")) {
				//System.out.print("35 ");
				parseCompleto += "35 ";
				equipara("ologico");
				String Rtipo = R();
				String Ep1tipo = Ep();
				//SEMANTICO
				if(Rtipo.equals("logico")){
					Eptipo = Rtipo;
				}
				else {
					Eptipo = "tipo_err";
				}
				//SEMANTICO
			}
			else if(followEp.contains(sig_token)) {
				//System.out.print("36 ");
				parseCompleto += "36 ";
				//SEMANTICO
				Eptipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Eptipo;
		}
		
		public String R() {
			String Rtipo = "tipo_ok";
			//System.out.print("37 ");
			parseCompleto += "37 ";
			String Utipo = U();
			String Rptipo = Rp();
			//SEMANTICO
			if (Rptipo.equals("tipo_vacio")) {
				Rtipo = Utipo;
			}
			else if(!Rptipo.equals(Utipo)) {
				System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Los operandos de la comparacion deben ser del mismo tipo");
				System.exit(-1);
			}
			else if (Rptipo.equals(Utipo)){
				Rtipo = "logico";
			}
			//SEMANTICO
			return Rtipo;
		}

		public String Rp(){
			String Rptipo = "tipo_ok";
			if(sig_token.equals("oprelacional")){
				//System.out.print("38 ");
				parseCompleto += "38 ";
				equipara("oprelacional");
				String Utipo = U();
				Rp();
				//SEMANTICO
				if(Utipo.equals("cadena") || Utipo.equals("entero") || Utipo.equals("logico")){
					Rptipo = Utipo;
				}
				//SEMANTICO
			}
			else if(followRp.contains(sig_token)){
				//System.out.print("39 ");
				parseCompleto += "39 ";
				//SEMANTICO
				Rptipo = "tipo_vacio";
				//SEMANTICO
			}
			else{
				System.err.println(error());
				System.exit(-1);
			}
			return Rptipo;
		}

		public String U() {
			String Utipo = "tipo_ok";
			//System.out.print("40 ");
			parseCompleto += "40 ";
			String Vtipo = V();
			String Uptipo = Up();
			//SEMANTICO
			if(Uptipo.equals("tipo_vacio")) {
				Utipo = Vtipo;
			}
			else if(Uptipo == "tipo_err" || !Uptipo.equals(Vtipo)) {
				System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Los operandos de la suma deben ser de tipo entero");
				System.exit(-1);
			}
			else if((Uptipo.equals(Vtipo)) && (Uptipo.equals("entero"))){
				Utipo = "entero";
			}
			else{
				Utipo = "tipo_err";
			}
			//SEMANTICO
			return Utipo;
		}

		public String Up(){
			String Uptipo = "tipo_ok";
			if(sig_token.equals("suma")){
				//System.out.print("41 ");
				parseCompleto += "41 ";
				equipara("suma");
				String Vtipo = V();
				Up();
				//SEMANTICO
				if(Vtipo.equals("entero")){
					Uptipo = "entero";
				}
				else{
					Uptipo = "tipo_err";
				}
				//SEMANTICO
			}
			else if(followUp.contains(sig_token)){
				//System.out.print("42 ");
				parseCompleto += "42 ";
				//SEMANTICO
				Uptipo = "tipo_vacio";
				//SEMANTICO
			}
			else{
				System.err.println(error());
				System.exit(-1);
			}
			return Uptipo;
		}

		public String V(){
			String Vtipo = "tipo_ok";
			if(sig_token.equals("id")){
				//System.out.print("43 ");
				parseCompleto += "43 ";
				equipara("id");
				boolean esGlobal = false;
				String tipoGlobal = "";
				String idTipo = TSActual.buscaTipoTS(idPosTS);
				int idts = idPosTS;
				String Ntipo = N();
				boolean encontrado = false;
				for(int i = 0; i < TSG.tablaSimbolos.size(); i++) {
					try {
						if(TSActual.getEntrada(idts).lexema.equals(TSG.getEntrada(i).lexema) && !TSActual.equals(TSG)) {
							encontrado = true;
							if(TSG.getEntrada(i).tipo.equals("function")) {
								TSActual.tablaSimbolos.remove(idts);
								idTipo = TSG.getEntrada(i).getDevuelto();
							}
							if(TSActual.getEntrada(idts).noDeclarado) {
								TSActual.tablaSimbolos.remove(idts);
								esGlobal = true;
								tipoGlobal = TSG.getEntrada(i).tipo;
								idTipo = tipoGlobal;
							}
						}
						
					}catch(Exception IndexOutOfBounds) {
						
					}
				}
				//SEMANTICO
				Vtipo = idTipo;
				//SEMANTICO
			}
			else if (sig_token.equals("abreparentesis")){
				//System.out.print("44 ");
				parseCompleto += "44 ";
				equipara("abreparentesis");
				String Etipo = E();
				equipara("cierraparentesis");
				//SEMANTICO
				Vtipo = Etipo;
				//SEMANTICO
			}
			else if(firstVp.contains(sig_token)){
				//System.out.print("45 ");
				parseCompleto += "45 ";
				String Vptipo = Vp();
				//SEMANTICO
				Vtipo = Vptipo;
				//SEMANTICO
			}
			else{
				System.err.println(error());
				System.exit(-1);
			}
			return Vtipo;
		}

		public String N() {
			String Ntipo = "tipo_ok";
			if (sig_token.equals("abreparentesis")){
				//System.out.print("46 ");
				parseCompleto += "46 ";
				equipara("abreparentesis");
				String Ltipo = L();
				equipara("cierraparentesis");
				//SEMANTICO
				Ntipo = Ltipo;
				//SEMANTICO
			}
			else if(followN.contains(sig_token)){
				//System.out.print("47 ");
				parseCompleto += "47 ";
				//SEMANTICO
				Ntipo = "tipo_vacio";
				//SEMANTICO
			}
			else{
				System.err.println(error());
				System.exit(-1);
			}
			return Ntipo;
		}

		public String Vp() {
			String VpTipo = "tipo_ok";
			if(sig_token.equals("cteentera")){
				//System.out.print("48 ");
				parseCompleto += "48 ";
				equipara("cteentera");
				//SEMANTICO
				VpTipo = "entero";				
				//SEMANTICO
			}

			else if (sig_token.equals("cadena")){
				//System.out.print("49 ");
				parseCompleto += "49 ";
				equipara("cadena");
				//SEMANTICO
				VpTipo = "cadena";				
				//SEMANTICO
			}
			else{
				System.err.println(error());
				System.exit(-1);
			}
			return VpTipo;
		}
		
		public String Z() {
			String Ztipo = "tipo_ok";
			if(sig_token.equals("case")) {
				//System.out.print("50 ");
				parseCompleto += "50 ";
				equipara("case");
				String Vptipo = Vp();
				equipara("dospuntos");
				C();
				String Z1tipo = Z();
				//SEMANTICO
				if(Z1tipo.equals("tipo_err")) {
					Ztipo = "tipo_err";
				}
				else if(!(Vptipo.equals(Z1tipo)) && !(Z1tipo.equals("tipo_vacio"))){
					System.err.println("ERROR en linea " + tokenActual.getLinea() + ": Tipos distintos en los case");
					System.exit(-1);
					Ztipo = "tipo_err";
				}
				else if((Vptipo.equals("cadena")) || (Vptipo.equals("entero"))){
					Ztipo = Vptipo;
				} 
				else{
					System.err.println("ERROR en linea " + tokenActual.getLinea() + ": El case debe llevar cadenas o enteros");
					System.exit(-1);
					Ztipo = "tipo_err";
				}
				//SEMANTICO	
			}
			else if(followZ.contains(sig_token)) {
				//System.out.print("51 ");
				parseCompleto += "51 ";
				//SEMANTICO
				Ztipo = "tipo_vacio";
				//SEMANTICO
			}
			else {
				System.err.println(error());
				System.exit(-1);
			}
			return Ztipo;
		}
		
		public static class Tuple {
			public String tipo;
			public int ancho;
			public Tuple(String tipo, int ancho) {
				this.tipo = tipo;
				this.ancho = ancho;
			}
		}
}
