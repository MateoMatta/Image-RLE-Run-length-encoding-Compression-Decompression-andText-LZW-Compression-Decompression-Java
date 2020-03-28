package decodificacionFuente;

import java.util.ArrayList;

public class Codigo {
	
	private String code;
	private ArrayList<String> diccionario;
	private ArrayList<String> recibido;
	private ArrayList<String> salidas;
	private String mensaje;
	public Codigo(String code) {
		super();
		this.code = code;
		this.diccionario = new ArrayList<String>();
		this.recibido = new ArrayList<String>();
		this.salidas = new ArrayList<String>();
		this.mensaje = "";
	}
	public ArrayList<String> getSalidas() {
		return salidas;
	}
	public void setSalidas(ArrayList<String> salidas) {
		this.salidas = salidas;
	}
	public ArrayList<String> getRecibido() {
		return recibido;
	}
	public void setRecibido(ArrayList<String> recibido) {
		this.recibido = recibido;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArrayList<String> getDiccionario() {
		return diccionario;
	}
	public void setDiccionario(ArrayList<String> diccionario) {
		this.diccionario = diccionario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
	

}
