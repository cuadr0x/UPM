package pdl;

import java.util.ArrayList;
import java.util.Arrays;

public class EntradasTS {
	ArrayList<String> parametros;
	String lexema;
	String tipo;
	int desplazamiento;
	int numero_parametros;
	String tipo_parametros;
	String devuelto;
	String etiqueta;
	boolean noDeclarado;
	public boolean nuevo;
	
	
	public EntradasTS(String lexema) {
		this.lexema = lexema;
		this.tipo = "-";
		this.desplazamiento = 0;
		this.noDeclarado = false;
		this.tipo = "-";
		this.etiqueta = "-";
		this.devuelto = "-";
		this.nuevo = true;
	}

	public EntradasTS(String lexema, boolean noDeclarado, boolean y) {
		this.tipo = "entero";
		this.lexema = lexema;
		this.noDeclarado = true;
		this.etiqueta = "-";
		this.devuelto = "-";
		this.nuevo = true;
	}
	public void insertarTipoDesplazamiento(String tipo, int desplazamiento) {
		this.desplazamiento = desplazamiento;
		this.tipo = tipo;
	}
	public EntradasTS(String lexema, boolean x) {
		this.lexema = lexema;
		this.tipo = "function";
		this.parametros = new ArrayList<String>();
		this.parametros.add("-");
		this.numero_parametros = 0;
		this.noDeclarado = false;
		this.etiqueta = "-";
		this.devuelto = "-";
		this.nuevo = true;
	}
	public void addParamFunc(String[] param) {
		this.parametros = new ArrayList<String>(Arrays.asList(param));
		this.numero_parametros = param.length;
	}
	public void addTipoRetorno(String tipoRetorno) {
		this.devuelto = tipoRetorno;
	}
	public String getLexema() {
		return lexema;
	}
	
	public String getTipo() {
		return tipo;
	}
	public String getDevuelto() {
		return devuelto;
	}
	public void addEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String toString() {
		if(tipo.equals("function")) {
			String aux = String.format("* LEXEMA: '%s'\nATRIBUTOS:\n+ TIPO: '%s'\n+ NUMPARAM: %d\n", lexema, tipo, numero_parametros);
			int contador = 1;
			if(numero_parametros != 0) {
				for(String s: parametros) {
					aux += "+ TipoParam" + contador + ": '" + s + "'\n";
					aux += "+ ModoParam"+ contador + ": 1\n";
					contador++;
				}
			}
			aux += String.format("+ TIPORETORNO: '%s'\n+ ETIQFUNCION: '%s'", devuelto ,etiqueta );
			return aux;
		}
		return String.format("* LEXEMA: '%s'\nATRIBUTOS:\n+ TIPO: '%s'\n+ DESPL: %d", lexema, tipo, desplazamiento);
	}
}
