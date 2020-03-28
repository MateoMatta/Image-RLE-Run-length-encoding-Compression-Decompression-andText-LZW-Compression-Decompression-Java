package decodificacionFuente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Descompresion {
	
	private static Codigo codigo;
	
	public static void main(String[] args) {
		
		  String code = "";
		  File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	    	  
	    	  //Por favor cambie la ruta del archivo
	         archivo = new File (".\\Datos\\PruebaSalida.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         String linea;
	         while((linea=br.readLine())!=null) {
	        	 code += linea;
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
	      
	      codigo = new Codigo(code);
	      
	      separador();
	      
	      
	      
	      descompresion();
	      
	      System.out.println("\nLa codificacion es:");
			
			//Imprimir codificacion
			
			for (int i = 0; i < codigo.getSalidas().size(); i++) {
				System.out.println(codigo.getSalidas().get(i));
			}
			
			System.out.println("\nDiccionario");
			
			//Imprimir salidas
			
			for (int i = 0; i < codigo.getDiccionario().size(); i++) {
				System.out.println(codigo.getDiccionario().get(i)+"="+(i));
			}
			
	      
	      String mensaje = "";
	      for (int i = 0; i < codigo.getSalidas().size(); i++) {
	    	  mensaje += codigo.getSalidas().get(i);
			
		}
	      String aux = "";
	      for (int i = 0; i < mensaje.length(); i++) {
	    	  if(("~").equals(mensaje.charAt(i)+"")) {
	    		  aux += " ";
	    	  }
	    	  else {
	    		  aux += mensaje.charAt(i);
	    	  }
			
		}
	      
	    mensaje = aux;
	    
	    codigo.setMensaje(mensaje);
	    
	    FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	//Por favor cambie la ruta del archivo
            fichero = new FileWriter(".\\Datos\\PruebaSalidaDescomp.txt");
            pw = new PrintWriter(fichero);
            
            String recibido = codigo.getMensaje();
        //    for (int i = 0; i < palabra.getCodificacion().size(); i++) {
        //    	codigo += palabra.getCodificacion().get(i);
        //    }
                pw.println(recibido);

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
	
	
	private static void separador() {
		String tamano = "";
		for (int i = 0; i < 7; i++) {
			tamano += codigo.getCode().charAt(i)+"";
		}
		int num = 0;
		String palabrainvertida = "";
		 for (int k = tamano.length()-1; k>=0; k--){
	            palabrainvertida += tamano.charAt(k);
	        }
		 tamano = palabrainvertida;
		
		for (int i = 0; i < tamano.length(); i++) {
			String actual = tamano.charAt(i)+"";
			if (actual.equals("1")) {
				num += Math.pow(2, i);
			}
		}
		num = num*6;
		
		mensaje(num);
		
	}
	
	private static void mensaje(int num) {
		int ultimo = 7+num;
		String mensaje = codigo.getCode().substring(7, ultimo);
		for (int i = 0; i < mensaje.length(); i+=6) {
			String actual = "";
				actual = mensaje.substring(i, i+6);
				codigo.getRecibido().add(actual);
		}
		
		for (int i = 0; i < codigo.getRecibido().size(); i++) {
			String binario = codigo.getRecibido().get(i);
			String palabrainvertida = "";
			 for (int k = binario.length()-1; k>=0; k--){
		            palabrainvertida += binario.charAt(k);
		        }
			 binario = palabrainvertida;
			int decimal = 0;
			for (int j = 0; j < binario.length(); j++) {
				String actual = binario.charAt(j)+"";
				if (actual.equals("1")) {
					decimal += Math.pow(2, j);
				}
			}
			codigo.getRecibido().set(i, decimal+"");
			
		}		
		diccionario(ultimo);
		
		
	}
	
	private static void diccionario(int dicc) {
		String diccionario = codigo.getCode().substring(dicc);
		for (int i = 0; i < diccionario.length(); i+=7) {
			String actual = ""	;
			actual = diccionario.substring(i, i+7);
			codigo.getDiccionario().add(actual);
		}
		
		for (int i = 0; i < codigo.getDiccionario().size(); i++) {
			String binario = codigo.getDiccionario().get(i);
			String palabrainvertida = "";
			 for (int k = binario.length()-1; k>=0; k--){
		            palabrainvertida += binario.charAt(k);
		        }
			 binario = palabrainvertida;
			int decimal = 0;
			String letra = "";
			char ascii = 0;
			for (int j = 0; j < binario.length(); j++) {
				String actual = binario.charAt(j)+"";
				if (actual.equals("1")) {
					decimal += Math.pow(2, j);
				}
			}
			ascii = (char) decimal;
			letra = ascii+"";
			codigo.getDiccionario().set(i, letra);
			
			
		}
		
	}
	
	private static void descompresion() {
		ArrayList<String> diccionario = codigo.getDiccionario();
		ArrayList<String> recibido = codigo.getRecibido();
				
		int codViejo = Integer.parseInt(recibido.get(0));
		
		String caracter = diccionario.get(codViejo);
		
		int codNuevo;
		
		String cadena="";
		
		codigo.getSalidas().add(caracter);
		
		for (int i = 1; i < recibido.size(); i++) {
			
			codNuevo = Integer.parseInt(recibido.get(i));
			
			if(codNuevo>diccionario.size()) {
				
				cadena = diccionario.get(codViejo);
				cadena = cadena + caracter;
				
			}
			else {
				
				cadena = diccionario.get(codNuevo);
				
			}
			
			codigo.getSalidas().add(cadena);
			caracter = cadena.charAt(0)+"";
			
			String aux = diccionario.get(codViejo) + caracter;
			
			diccionario.add(aux);
			
			codViejo = codNuevo;
			
		}
		
		
		
		
	}
		

}
