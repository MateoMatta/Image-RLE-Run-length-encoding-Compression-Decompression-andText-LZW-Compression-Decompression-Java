package codificacionFuente;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Palabra {
	
	private String informacion;
	private ArrayList<String> diccionario;
	private ArrayList<String> diccInicial;
	private ArrayList<String> diccBin;
	private ArrayList<String> codificacion;
	private ArrayList<String> binarios;
	private Map<String,Double> probabilidad,informacionPropia;
	private double entropia,entropiatoL,longitud;
	private Map<String,Integer> repeticionLetra;
	private double tama�oInfo;// 8 bits*mensaje.lenght
	private double lzw;// los bites deacuerdo al diccionario log2(tama�o diccionario)
	private double mensajeLZW;
	private double tama�oTabla;
	private double relacionCompresion;
	private int r;
	
	
	public Palabra(String informacion) {
		this.informacion = informacion;
		this.diccionario = new ArrayList<String>();
		this.codificacion = new ArrayList<String>();
		this.diccInicial = new ArrayList<String>();
		this.diccBin = new ArrayList<String>();
		this.binarios = new ArrayList<String>();
		this.r=2;
	}
	

	public double getEntropia() {
		return entropia;
	}


	public void setEntropia(double entropia) {
		this.entropia = entropia;
	}


	public Map<String, Double> getInformacionPropia() {
		return informacionPropia;
	}


	public void setInformacionPropia(Map<String, Double> informacionPropia) {
		this.informacionPropia = informacionPropia;
	}


	public Map<String, Integer> getRepeticionLetra() {
		return repeticionLetra;
	}


	public void setRepeticionLetra(Map<String, Integer> repeticionLetra) {
		this.repeticionLetra = repeticionLetra;
	}


	public Map<String, Double> getProbabilidad() {
		return probabilidad;
	}


	public void setProbabilidad(Map<String, Double> probabilidad) {
		this.probabilidad = probabilidad;
	}


	public String getInformacion() {
		return informacion;
	}


	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}


	public ArrayList<String> getDiccionario() {
		return diccionario;
	}


	public void setDiccionario(ArrayList<String> diccionario) {
		this.diccionario = diccionario;
	}


	public ArrayList<String> getCodificacion() {
		return codificacion;
	}


	public void setCodificacion(ArrayList<String> codificacion) {
		this.codificacion = codificacion;
	}


	public ArrayList<String> getDiccInicial() {
		return diccInicial;
	}


	public void setDiccInicial(ArrayList<String> diccInicial) {
		this.diccInicial = diccInicial;
	}

	public ArrayList<String> getBinarios() {
		return binarios;
	}


	public void setBinarios(ArrayList<String> binarios) {
		this.binarios = binarios;
	}


	public ArrayList<String> getDiccBin() {
		return diccBin;
	}


	public void setDiccBin(ArrayList<String> diccBin) {
		this.diccBin = diccBin;
	}
	
	
	public void iniciarDiccionarioInicial() {
		setDiccInicial(diccionario);
	}
	
	public void repeticionLetra() {
		repeticionLetra = new HashMap<String,Integer>();
		String info = getInformacion();
		char[] infodiv = info.toCharArray();
		for (int i = 0; i < diccionario.size(); i++) {
			int contador = 0;
			for (int j = 0; j < infodiv.length; j++) {
				if (diccionario.get(i).equalsIgnoreCase(""+infodiv[j])) {
					contador++;
				}
			}
			repeticionLetra.put(diccionario.get(i), contador);
		}
		
	}
	
	public void probabilidades(){
		probabilidad = new HashMap<String,Double>();
		
		for (int i = 0; i < diccionario.size(); i++) {
			String k = diccionario.get(i);
			double valor = repeticionLetra.get(k);
			double prob = valor/informacion.length();
			probabilidad.put(k, prob);
		}
	}
	
	public void informacionPropia() {
		informacionPropia = new HashMap<String,Double>();
		for (int i = 0; i < diccionario.size(); i++) {
			String k = diccionario.get(i);
			double valor = probabilidad.get(k);
			double prob = Math.log(1/valor) / Math.log(r);
			informacionPropia.put(k, prob);
		}
	}
	public void entropia() {
		entropia = 0.0;
		for (int i = 0; i < diccionario.size(); i++) {
			String k = diccionario.get(i);
			double valor1 = probabilidad.get(k);
			double valor2 = informacionPropia.get(k);
			entropia+=(valor1*valor2);
		}
	}
	public void entropiaParaLaLongitud() {
		entropiatoL = entropia / (Math.log(r) / Math.log(2));
	}
	public void totalTama�oArchivo() {
		DecimalFormat df = new DecimalFormat("#.00");
		double longitudInfo = informacion.length();
		tama�oInfo=longitudInfo*8; // ASCII 8 bits
		if(tama�oInfo>1024000000)
			System.out.println(informacion+"  : " + df.format(tama�oInfo/1024000000) + " Gb");
		else if(tama�oInfo>1024000)
			System.out.println(informacion+"  : " + df.format(tama�oInfo/1024000) + " Mb");
		else if(tama�oInfo>1024)
			System.out.println(informacion+"  : " + df.format(tama�oInfo/1024) + " Kb");
		else
			System.out.println(informacion+"  : " + df.format(tama�oInfo) + " bytes");		
	}
	
	public void totalTama�oArchivoSalida() {
		DecimalFormat df = new DecimalFormat("#.00");
		lzw = Math.log(diccionario.size())/Math.log(2);
        System.out.println("Tama�o LZW "+ lzw + "bits");
        mensajeLZW = codificacion.size()*lzw;// recordemos que tenemos que enviar la tabla
        tama�oTabla = diccInicial.size()*3*8+diccInicial.size()*lzw;//3 -> a=codigo ; 8bits ascii
        mensajeLZW=mensajeLZW+tama�oTabla;
		
 
		if(mensajeLZW>1024000000)
			System.out.println(diccionario.toString()+"  : " + df.format(mensajeLZW/1024000000) + " Gb");
		else if(mensajeLZW>1024000)
			System.out.println(diccionario.toString()+"  : " + df.format(mensajeLZW/1024000) + " Mb");
		else if(mensajeLZW>1024)
			System.out.println(diccionario.toString()+"  : " + df.format(mensajeLZW/1024) + " Kb");
		else
			System.out.println(diccionario.toString()+"  : " + df.format(mensajeLZW) + " bytes");
	
		relacionCompresion = tama�oInfo/mensajeLZW;
		
		System.out.println("\nRelacion de compresion"+ relacionCompresion);
	}
	

}
