package pdl;

import java.util.ArrayList;

public class TablaSimbolos {
	
	public ArrayList<EntradasTS> tablaSimbolos;
	int id;
	String nombre;
	public TablaSimbolos(String nombre, int id) {
		this.nombre = nombre;
		this.id = id;
		tablaSimbolos = new ArrayList<EntradasTS>();
	}
	
	public void add(EntradasTS entrada) {
		tablaSimbolos.add(entrada);
	}
	
	public void addParam(ArrayList<String> param) {
		for(String i: param) {
			tablaSimbolos.add(new EntradasTS(i));
		}
	}
	
	public String getLexemaPos(int posTS) {
		return tablaSimbolos.get(posTS).getLexema();
	}
	
	public EntradasTS getEntrada(int posTS) {
		return tablaSimbolos.get(posTS);
	}
	public void insertarTipoTS(int pos, String tipo, int desplazamiento) {
		EntradasTS entrada = tablaSimbolos.get(pos);
		entrada.insertarTipoDesplazamiento(tipo, desplazamiento);
		tablaSimbolos.set(pos, entrada);
	}
	
	public void insertarTipoParamFun(int posTS, String[] param) {
		EntradasTS entrada = tablaSimbolos.get(posTS);
		entrada.addParamFunc(param);
	}
	public void insertarRetorno(int posTS, String tipoRetorno) {
		EntradasTS entrada = tablaSimbolos.get(posTS);
		entrada.addTipoRetorno(tipoRetorno);
	}
	
	public void insertarEtiqueta(int posTS, String etiqueta) {
		EntradasTS entrada = tablaSimbolos.get(posTS);
		entrada.addEtiqueta(etiqueta);
	}
	public String buscaTipoTS(int pos) {
		return tablaSimbolos.get(pos).getTipo();
	}
	public int getPosition(String id) {
		for(int i = 0; i < tablaSimbolos.size(); i++) {
			if(tablaSimbolos.get(i).getLexema().equals(id)) {
				return i;
			}
		}
		return -1;
	}
	public String toString() {
		String aux = nombre + " # " + id + " :\n";
		for(EntradasTS entrada: tablaSimbolos) {
			aux += entrada.toString() + "\n";
			aux += "-------------------------------\n";
		}
		return aux;	
	}
}
