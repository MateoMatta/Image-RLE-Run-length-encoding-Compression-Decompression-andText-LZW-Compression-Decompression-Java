package codificacionFuente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Compresion {
	
	private Palabra palabra;
	private String informacion;
	public Compresion() {
		super();
	}
	public static void main(String[] args) {
		
		Compresion c = new Compresion();
		
		c.leer();
		c.compresion();
		c.imprimirCodificacion();
		c.imprimirRepeticionLetras();
		c.imprimirProbabilidades();
		c.imprimirInformacionPropia();
		c.imprimirEntropia();
		c.imprimirSalidas();
		c.escribir();
		c.palabra.totalTamañoArchivoSalida();
		
		}
	public void leer() {
		informacion = "";
		//Lectura de archivo, por favor separar la informacion con espacio en el respectivo orden:	
		//numero unico de boleta, nombres y apellidos del asistente (todo junto coon iniciales en mayuscula), Documento de identidad, Edad, Genero (Masculino o Femenino). 
		File archivo = null;
		
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	    	  
	    	  //Por favor cambie la ruta del archivo
	         archivo = new File (".\\Datos\\Prueba.txt");
	         System.out.println("Tamaño archivo entrada");
	         
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         String linea;
	         while((linea=br.readLine())!=null) {
	        	 informacion += linea;
	         }
	         
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }		
	}
	public void imprimirCodificacion() {
		System.out.println("La informacion ingresada es:\n" + informacion);
	    System.out.println("\nLa codificacion es:");
	    System.out.println(palabra.getCodificacion().toString());
//		for (int i = 0; i < palabra.getCodificacion().size(); i++) {
//			System.out.println(palabra.getCodificacion().get(i));
//		}
	}
	public void imprimirRepeticionLetras() {
		System.out.println("\nRepeticion de las letras:");
		System.out.println(palabra.getRepeticionLetra().toString());
	}
	public void imprimirSalidas() {
		System.out.println("\nDiccionario");
		for (int i = 0; i < palabra.getDiccionario().size(); i++) {
			System.out.println(palabra.getDiccionario().get(i)+"="+(i));
		}
	}
	public void imprimirInformacionPropia() {
		System.out.println("\nInformacion propia:");
		System.out.println(palabra.getInformacionPropia().toString());
	}
	public void imprimirEntropia() {
		System.out.println("\nEntropia:");
		System.out.println(palabra.getEntropia());
	}
	public void imprimirProbabilidades() {
		System.out.println("\nProbabilidades");
		System.out.println(palabra.getProbabilidad().toString());
	}
	public void escribir() {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	//Por favor cambie la ruta del archivo
            fichero = new FileWriter(".\\Datos\\\\PruebaSalida.txt");
            pw = new PrintWriter(fichero);
            
            String codigo = mensaje();
            
        //    for (int i = 0; i < palabra.getCodificacion().size(); i++) {
        //    	codigo += palabra.getCodificacion().get(i);
        //    }
                pw.println(codigo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
          
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	public void compresion() {
		palabra = new Palabra(informacion);
		palabra.getInformacion().trim();
		String nuevaPalabra = palabra.getInformacion();
		String aux = "";
		for (int i = 0; i < nuevaPalabra.length(); i++) {
			if ((nuevaPalabra.charAt(i) + "").equals(" ")) {
				aux += "~";
			} else {
				aux += nuevaPalabra.charAt(i) + "";
			}
		}
		palabra.setInformacion(aux);
		palabra.setDiccionario(iniciarDiccionario());		
		ArrayList<String> dicc = palabra.getDiccionario();
		int j = palabra.getInformacion().length();
		String w = "";
		String k = "";
		String wk = "";
		int cont = 0;
		k = palabra.getInformacion().charAt(cont) + "";
		palabra.setDiccionario(dicc);
		palabra.repeticionLetra();
		palabra.probabilidades();
		palabra.informacionPropia();
		palabra.entropia();
		palabra.totalTamañoArchivo();
		while (j > 0) {
			k = palabra.getInformacion().charAt(cont) + "";
			wk = w + "" + k;
			if (estaDicc(dicc, wk)) {
				w = wk;
			} else {
				palabra.getCodificacion().add(codigo(w));
				dicc.add(wk);
				palabra.setDiccionario(dicc);
				w = k;
			}
			j--;
			cont++;
		}
		palabra.getCodificacion().add(codigo(w));
	}
	public ArrayList<String> iniciarDiccionario() {
		ArrayList<String> dicc = new ArrayList<String>();
		for (int i = 0; i < palabra.getInformacion().length(); i++) {
			boolean esta = false;
			String actual = palabra.getInformacion().charAt(i) + "";
			for (int j = 0; j < dicc.size(); j++) {
				if (dicc.get(j).equals(actual)) {
					esta = true;
				}
			}
			if (esta == false) {
				dicc.add(actual);
			}

		}
		return dicc;
		
		

	}
	public boolean estaDicc(ArrayList<String> dicc, String wk) {
		boolean esta = false;
		for (int i = 0; i < dicc.size() && !esta; i++) {
			if (wk.equals(dicc.get(i))) {
				esta = true;
			}
		}

		return esta;
	}
	public String codigo(String w) {
		String codigo = "";
		boolean termino = false;
		for (int i = 0; i < palabra.getDiccionario().size()&&!termino; i++) {
			if(w.equals(palabra.getDiccionario().get(i))) {
				codigo = i+"";
				termino = true;
			}
		}
		
		return codigo;
	}
	public String mensaje(){
		int cantidadInfo = palabra.getCodificacion().size();
		String tamano = "";
		String mensaje = "";
		
		
		
		while(cantidadInfo>0) {
			tamano += cantidadInfo % 2;
			cantidadInfo /= 2;
		}
		 String palabrainvertida = "";
		 for (int i = tamano.length()-1; i>=0; i--){
	            palabrainvertida += tamano.charAt(i);
	        }
		 tamano = palabrainvertida;
		
		
		if (tamano.length()<7) {
			int resto = 7 - tamano.length();
			String aux = tamano;
			tamano = "";
			while (resto > 0) {
				tamano += "0";
				resto--;
			}
			tamano += aux;
		}
		
		for (int i = 0; i < palabra.getCodificacion().size(); i++) {
			convertirDecimal(i);
		}
		
		mensaje = tamano;
		
		for (int i = 0; i < palabra.getBinarios().size(); i++) {
			mensaje += palabra.getBinarios().get(i);
		}
		
		diccionarioBinario();
		
		for (int i = 0; i < palabra.getDiccBin().size(); i++) {
			mensaje += palabra.getDiccBin().get(i);
		}
		
		return mensaje;
		
		
	}
	public void convertirDecimal(int i){
		String actual = palabra.getCodificacion().get(i);
		int decimal = Integer.parseInt(actual);
		String bin = "";
		
		if(decimal > 0) {		
		while (decimal > 0) {
			bin += 	decimal % 2;
			decimal /= 2;
		}
		}
		else {
			bin += 0;
		}
		String palabrainvertida = "";
		 for (int k = bin.length()-1; k>=0; k--){
	            palabrainvertida += bin.charAt(k);
	        }
		 bin = palabrainvertida;
		if (bin.length()<6) {
			int resto = 6 - bin.length();
			String aux = bin;
			bin = "";
			while (resto > 0) {
				bin += "0";
				resto--;
			}
			bin += aux;
		}
		
		palabra.getBinarios().add(bin);
	}
	public void diccionarioBinario() {
		
		palabra.setDiccInicial(iniciarDiccionario());
		
		ArrayList<String> init = palabra.getDiccInicial();
				
		int cont = 0;
		
		while(cont<init.size()) {
				
				char letra = init.get(cont).charAt(0);
				int ascii = (int) letra;
				
				String bin = "";
				
				if(ascii > 0) {		
				while (ascii > 0) {
					bin += 	ascii % 2;
					ascii /= 2;
				}
				}
				else {
					bin += 0;
				}
				String palabrainvertida = "";
				 for (int k = bin.length()-1; k>=0; k--){
			            palabrainvertida += bin.charAt(k);
			        }
				 bin = palabrainvertida;
				if (bin.length()<7) {
					int resto = 7 - bin.length();
					String aux = bin;
					bin = "";
					while (resto > 0) {
						bin += "0";
						resto--;
					}
					bin += aux;
				}
				palabra.getDiccBin().add(bin);
			
			
			cont++;
		}
		
	}
	

	
}
