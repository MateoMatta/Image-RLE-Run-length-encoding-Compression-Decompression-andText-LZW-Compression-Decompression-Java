package RLE_Img;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.imageio.ImageIO;

public class RLE {


	public final static char CARACTER_ESPECIAL= '#'; 
	public final static char RED= 'r'; 
	public final static char GREEN= 'g'; 
	public final static char BLUE= 'b'; 
	public final static char CARACTER = 'c'; 
	public final static char CARACTER_BIN = '1'; 
	public final static char NUMERO= 'n'; 
	public final static char NUMERO_BIN = '0'; 

	public RLE()
	{		
	}

	public static double contarBits(String data, String infoAPoner) {
		double longitudInfo = data.length();
		double infoSize=longitudInfo*8;
		DecimalFormat df = new DecimalFormat("#.00");
		if(infoSize>1024000000) {
			System.out.println(infoAPoner + " : " + df.format(infoSize/1024000000) + " Gb");
			return infoSize;

		}else if(infoSize>1024000) {
			System.out.println(infoAPoner+ " : " + df.format(infoSize/1024000) + " Mb");
			return infoSize;
		}else if(infoSize>1024) {
			System.out.println(infoAPoner + " : " + df.format(infoSize/1024) + " Kb");
			return infoSize;
		}else {
			System.out.println(infoAPoner + " : " + df.format(infoSize) + " bytes");		
			return infoSize;
		}
	}

	public static void mostrarImagen(Image imagen)
	{

		JFrame marco = new JFrame();
		marco.setSize(500, 500);
		JLabel label = new JLabel(new ImageIcon(imagen));
		marco.add(label);
		marco.setVisible(true);
	}


	public static String tomarURLImagen() {
		String url=null;
		JFileChooser fileChooser = new JFileChooser("./docs");
		int seleccion = fileChooser.showOpenDialog(null);
		if (seleccion == JFileChooser.APPROVE_OPTION)
		{
			File se= fileChooser.getSelectedFile();
			url= se.getPath();
		}	

		return url;
	}

	/** 255r255r220r....220r -> #255-2r#220-4r */
	public static String compresionRLE(String cadenaEntrada, String caracterSeparador)
	{ 
		String cadenaSalida ="";

		//CaracterSeparador para el Split
		String[] strings = cadenaEntrada.split(caracterSeparador);


		int i=0;

		while(i< strings.length)
		{
			int numeroAComparar = Integer.parseInt(strings[i]);

			try {


				if(i+1 < strings.length) {  //ya 1
					if( numeroAComparar == Integer.parseInt(strings[i+1]) ) //ya 2
					{
						//255r255r250r255r211r = 2
						//255r255r    =2
						//255r255r255r = +2
						//255r255r255r250r = 2+

						int contador =2;

						if(  i+2 < strings.length) //ya 3 (2)
						{
							if(numeroAComparar == Integer.parseInt(strings[i+2])) {  //ya 4 (2)

								contador =3;
								//---- mas cosas

								while( i+contador<strings.length  )
								{


									if(numeroAComparar == Integer.parseInt(strings[i+contador]))
									{

										contador++; //seguir por aqui
									}else
									{
										break;

									}



								}

							}else  //ya 4 (2)
							{
								contador =2;

							}


						}else //ya 3 (2)
						{
							contador =2;

						}



						cadenaSalida += (""+CARACTER_ESPECIAL)+numeroAComparar+"-"+contador+caracterSeparador;



						i+= contador-1;


					}else //ya 2
					{
						cadenaSalida +=numeroAComparar + caracterSeparador;
						//							#255r-2#220r-4
						//							#255-2r#220-4r209r201r#215-7r
						//208r
					}

				}else  //ya 1
				{
					cadenaSalida +=numeroAComparar + caracterSeparador;
				}



			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("Resultado:");
			}

			i++;


		}

		return cadenaSalida;
	} 

	/** 260-260-255r255r-250g-200b -> 260x260x#255-2r#220-4r209r201r#215-7r */

	public static String compresionDeImagenRLE(String cadenaEntrada)
	{
		String[] cadenas= cadenaEntrada.split("-");

		String alto = cadenas[0];
		String ancho = cadenas[1];
		String reds = cadenas[2];
		String greens = cadenas[3];
		String blues = cadenas[4];



		return alto  +"x"+ ancho + "x" + compresionRLE(reds, "r") + compresionRLE(greens, "g") + compresionRLE(blues, "b");

	}


	/** 260x260x255r210r100r#255-2r200r120g255g#200-10g255b250b#170-5b  -> 260-260-255r255r-250g-200b */


	public static String descompresionRLE(String cadenaEntrada)
	{
		String cadenaSalida ="";

		String[] strings = cadenaEntrada.split("x");


		//Strings[0] = alto
		//Strings[1] = ancho
		//Strings[2] = C1 = cadena comprimida_:
		//cadenaSalida += (""+CARACTER_ESPECIAL)+valorCadaColor+"-"+contador+caracterSeparador;
		//y/o valorCadaColor + caracterSeparador

		//e.g.:100r#255-2r255g#170-5b

		String[] c1 = strings[2].split("r");
		//hasta  c1.lenght-2 hay elementos del primer array

		for (int i = 0; i < c1.length-1; i++) {
			if(c1[i].charAt(0) == '#')
			{
				String s = c1[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				for (int j = 0; j < limite ; j++) {
					cadenaSalida += datos[0] + "r";
				}
			}else
			{
				cadenaSalida += c1[i] + "r";
			}
		}

		//-------

		cadenaSalida += "-";

		String[] c2 = c1[c1.length-1].split("g");

		for (int i = 0; i < c2.length-1; i++) {
			if(c2[i].charAt(0) == '#')
			{
				String s = c2[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				for (int j = 0; j < limite ; j++) {
					cadenaSalida += datos[0] + "g";
				}
			}else
			{
				cadenaSalida += c2[i] + "g";
			}
		}


		//------


		cadenaSalida += "-";

		String[] c3 = c2[c2.length-1].split("b");

		for (int i = 0; i < c3.length; i++) {
			if(c3[i].charAt(0) == '#')
			{
				String s = c3[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				for (int j = 0; j < limite ; j++) {
					cadenaSalida += datos[0] + "b";
				}
			}else
			{
				cadenaSalida += c3[i] + "b";
			}
		}

		cadenaSalida = strings[0] + "-" + strings[1] + "-" +cadenaSalida;


		return cadenaSalida;

	}

	/** 0-255 y/0 r,g,b,x,#,-  --> 00000000-11111111 Y/O 00110110, 00110101, ... */

	public static String codificacionBinariaIndividual(String caracter, char tipoDeValor) 
	{
		//Conversion de cada caracter del diccionario de compresion a binario
		String n = ""; //

		int x=0;
		if(tipoDeValor == CARACTER)
		{
			x= caracter.charAt(0);
			n= String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0');


		}else if(tipoDeValor == NUMERO)
		{
			x= Integer.parseInt(caracter);
			n= String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0');
		}


		return n;
	}

	/** 260x260x#255-2r#220-4r209g201b#215-7b -> 110100110101
	 * 			 1111111101111000*/

	public static String codificacionBinariaCompleta(String cadenaComprimida)
	{


		//Simple mapeo a binario de los caracteres y numeros numeros se toman como completos respectivamente

		String secuenciaBinaria="";

		String[] strings = cadenaComprimida.split("x");

		int num = 0;

		//Strings[0] = alto
		int alto = Integer.parseInt(strings[0]);
		//Strings[1] = ancho
		int ancho = Integer.parseInt(strings[1]);

		String altoBinario = String.format("%16s", Integer.toBinaryString(alto)).replace(' ', '0');
		//enviar altoBinario

		String anchoBinario = String.format("%16s", Integer.toBinaryString(ancho)).replace(' ', '0');
		//enviar anchoBinario



		//Strings[2] = C1 = cadena comprimida_:
		//cadenaSalida += (""+CARACTER_ESPECIAL)+valorCadaColor+"-"+contador+caracterSeparador;
		//y/o valorCadaColor + caracterSeparador

		//e.g.:100r#255-2r255g#170-5b

		String[] c1 = strings[2].split("r");
		//hasta  c1.lenght-2 hay elementos del primer array

		for (int i = 0; i < c1.length-1; i++) {
			if(c1[i].charAt(0) == '#')
			{

				String s = c1[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				//Secuencia binaria de 16 bits para numero de repeticiones
				//De resto, de 8 bits

				int x=0;
				x= Integer.parseInt(datos[1]);
				String n= String.format("%16s", Integer.toBinaryString(x)).replace(' ', '0');

				secuenciaBinaria += "1"
						+ codificacionBinariaIndividual("#", CARACTER) //1 # 255 - 2 r
						+ codificacionBinariaIndividual(datos[0], NUMERO)
						+ codificacionBinariaIndividual("-", CARACTER)
						+ n 													//2500 repeticiones e.g. -->0000100111000100
						+ codificacionBinariaIndividual("r", CARACTER);


			}else
			{
				//0 11111111 + 01110010
				secuenciaBinaria += "0" + codificacionBinariaIndividual(c1[i], NUMERO) + codificacionBinariaIndividual("r", CARACTER);
			}
		}

		//-------


		String[] c2 = c1[c1.length-1].split("g");

		for (int i = 0; i < c2.length-1; i++) {
			if(c2[i].charAt(0) == '#')
			{

				String s = c2[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				//Secuencia binaria de 16 bits para numero de repeticiones
				//De resto, de 8 bits

				int x=0;
				x= Integer.parseInt(datos[1]);
				String n= String.format("%16s", Integer.toBinaryString(x)).replace(' ', '0');

				secuenciaBinaria += "1"
						+ codificacionBinariaIndividual("#", CARACTER) 		//1 # 255 - 2 g
						+ codificacionBinariaIndividual(datos[0], NUMERO)
						+ codificacionBinariaIndividual("-", CARACTER)
						+ n 												//2500 repeticiones e.g. -->0000100111000100
						+ codificacionBinariaIndividual("g", CARACTER);


			}else
			{	//0 11111111 + 01100111
				secuenciaBinaria += "0" +  codificacionBinariaIndividual(c2[i], NUMERO) + codificacionBinariaIndividual("g", CARACTER);
			}
		}


		//------

		String[] c3 = c2[c2.length-1].split("b");

		for (int i = 0; i < c3.length; i++) {
			if(c3[i].charAt(0) == '#')
			{

				String s = c3[i].substring(1);
				String[] datos= s.split("-");
				int limite = Integer.parseInt(datos[1]);

				//Secuencia binaria de 16 bits para numero de repeticiones
				//De resto, de 8 bits

				int x=0;
				x= Integer.parseInt(datos[1]);
				String n= String.format("%16s", Integer.toBinaryString(x)).replace(' ', '0');

				secuenciaBinaria += "1"
						+ codificacionBinariaIndividual("#", CARACTER) 		//1 # 255 - 2 b
						+ codificacionBinariaIndividual(datos[0], NUMERO)
						+ codificacionBinariaIndividual("-", CARACTER)
						+ n 												//2500 repeticiones e.g. -->0000100111000100
						+ codificacionBinariaIndividual("b", CARACTER);


			}else
			{	//0 11111111 + 01100010
				secuenciaBinaria += "0" + codificacionBinariaIndividual(c3[i], NUMERO) + codificacionBinariaIndividual("b", CARACTER);
			}
		}


		String xBinario = codificacionBinariaIndividual("x", CARACTER);


		secuenciaBinaria = altoBinario + xBinario  +anchoBinario + xBinario + secuenciaBinaria;
		return secuenciaBinaria;

	}

	/** 0000000000000000-1111111111111111 -> binario 16 bits a numero*/
	public static String decodificacionIndividual16bits(String digitoBinario) {
		String frase = "";
		for (int i = 0; i < digitoBinario.length(); i += 16) {
			String cadenaSeparada = digitoBinario.substring(i, i + 16);
			/*entrega un numero decimal a partir de un numero binario de 16 bit*/

			int decimal = Integer.parseInt(cadenaSeparada, 2);
			frase = decimal + "";

		}
		return frase;
	}

	/** 00000000-11111111 -> binario 8 bits a ASCII o numero*/
	public static String decodificacionIndividual8bits(String digitoBinario, char tipoValor) {
		String frase = "";

		for (int i = 0; i < digitoBinario.length(); i += 8) {
			String cadenaSeparada = digitoBinario.substring(i, i + 8);
			/*entrega un numero decimal a partir de un numero binario de 8 bit*/
			int decimal = Integer.parseInt(cadenaSeparada, 2);

			if(tipoValor == CARACTER_BIN)
			{
				frase = frase + (char) decimal;

			}else if (tipoValor == NUMERO_BIN)
			{
				frase = frase + decimal;

			}
		}


		return frase;
	}


	/** (1)11010011(0)01010000 -> 2600x2600x#255-2r#220-4r209r201r#215-7r */
	public static String decodificacionBinaria(String secuenciaBinaria)
	{

		//Simple desmapeo de la secuencia binaria

		String cadenaComprimida = "";

		int alto = Integer.parseInt(decodificacionIndividual16bits(secuenciaBinaria.substring(0, 16)));
		String x1Ox2 = decodificacionIndividual8bits(secuenciaBinaria.substring(16,24), CARACTER_BIN); // o substring(40,48)
		int ancho = Integer.parseInt(decodificacionIndividual16bits(secuenciaBinaria.substring(24, 40)));

		String secuenciaDecodificada = secuenciaBinaria.substring(48);
		int i = 0;

		while(i < secuenciaDecodificada.length())
		{
			char tipoDeSecuencia = secuenciaDecodificada.charAt(i);


			if(tipoDeSecuencia == CARACTER_BIN)
			{
				String numeral = secuenciaDecodificada.substring(i+1, i+9);
				String valorIndividualColor = secuenciaDecodificada.substring(i+9, i+17);				
				String guion = secuenciaDecodificada.substring(i+17, i+25);
				String repeticiones = secuenciaDecodificada.substring(i+25, i+41); //deco16
				String letraColor = secuenciaDecodificada.substring(i+41, i+49);

				String c = decodificacionIndividual8bits(numeral, CARACTER_BIN) + 
						decodificacionIndividual8bits(valorIndividualColor, NUMERO_BIN) +
						decodificacionIndividual8bits(guion, CARACTER_BIN) +
						decodificacionIndividual16bits(repeticiones)+
						decodificacionIndividual8bits(letraColor, CARACTER_BIN);

				//#255-2r
				cadenaComprimida += c;



				i += 49;


			}else if( tipoDeSecuencia == NUMERO_BIN)
			{
				String valorIndividualColor = secuenciaDecodificada.substring(i+1, i+9);
				String letraColor = secuenciaDecodificada.substring(i+9,i+17);

				String c = decodificacionIndividual8bits(valorIndividualColor, NUMERO_BIN) 
						+ decodificacionIndividual8bits(letraColor, CARACTER_BIN);
				cadenaComprimida +=  c;

				i += 17;

			}


		}

		cadenaComprimida = alto + x1Ox2 + ancho + x1Ox2 + cadenaComprimida;


		return cadenaComprimida;
	}



	public static String[] cadenaColoresPorFilaDePixeles(String URLImagen, char color)
	{
		String[] valores = new String[3];


		String cadena = "";

		InputStream input;
		try {
			input = new FileInputStream(URLImagen);
			ImageInputStream imageInput = ImageIO.createImageInputStream(input);
			BufferedImage imagenL = ImageIO.read(imageInput);


			int alto = imagenL.getHeight();
			int ancho= imagenL.getWidth();

			valores[1] = alto + "";
			valores[2] = ancho + "";


			for (int y = 0; y < imagenL.getHeight(); y++) {

				for (int x = 0; x< imagenL.getWidth(); x++) {

					int srcPixel = imagenL.getRGB(x, y);

					Color c = new Color(srcPixel);

					if(color == RED)
					{

						int valR = c.getRed();
						cadena += valR+"r";
					}else if(color == GREEN)
					{
						int valG = c.getGreen();
						cadena += valG+"g";

					}else if(color == BLUE)
					{
						int valB = c.getBlue();
						cadena += valB+"b";

					}


				}

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		cadena= cadena.trim();

		valores[0] = cadena;

		return valores;

	}

	/** IMAGEN->260-260-255r255r-250g-200b */
	public static String cadenaCompletaDeColores(String URLImagen) 
	{

		//valores[0] = cadena completa
		//valores[1] = alto
		//valores[2] = ancho
		String laCadena = cadenaColoresPorFilaDePixeles(URLImagen, RED)[1]+"-"+cadenaColoresPorFilaDePixeles(URLImagen, RED)[2]+"-"+
				cadenaColoresPorFilaDePixeles(URLImagen, RED)[0] + "-" + cadenaColoresPorFilaDePixeles(URLImagen, GREEN)[0]
						+"-" + cadenaColoresPorFilaDePixeles(URLImagen, BLUE)[0];
		//System.out.println(xd);

		return laCadena;


	}


	/**  cadenadescomprimida --> IMAGEN */ 
	public static void construirYMostrarImagenDeCadena(String cadenaDeEntrada) throws IOException
	{
		String[] cadenasDeColores = cadenaDeEntrada.split("-");

		int alto = Integer.parseInt(cadenasDeColores[0]);
		int ancho = Integer.parseInt(cadenasDeColores[1]);

		String[] redsArray= cadenasDeColores[2].split("r");
		String[] greensArray= cadenasDeColores[3].split("g");
		String[] bluesArray= cadenasDeColores[4].split("b");


		BufferedImage newImage = new BufferedImage(alto, ancho, BufferedImage.TYPE_INT_RGB);
		/* Iterar por la matriz de píxeles */



		int contador= 0;
		for (int j = 0; j < alto; j++) {
			for (int i = 0; i < ancho; i++) {

				if(contador < (alto*ancho) && !redsArray[contador].equals("") )
				{

					int r = Integer.parseInt(redsArray[contador].trim());				
					int g = Integer.parseInt(greensArray[contador].trim());
					int b = Integer.parseInt(bluesArray[contador].trim());

					Color c = new Color(r,g,b);

					newImage.setRGB(i, j, c.getRGB());
					contador++;
				}

			}
		}

		ImageIO.write(newImage, "PNG", new File("./docs/output.jpg"));

		//Mostrar imagen
		mostrarImagen(newImage);


		System.out.println("Imagen creada y guardada como 'output.jpg'");

	}

	public static void main(String[] args) throws IOException {


		String cadena = "";

		try {

			/**1. Lectura de imagen y conversión a cadena de caracteres divididos por RGB*/

			String URLImagen = null;

			while( URLImagen == null)
			{
				URLImagen = tomarURLImagen();
			}

			System.out.println("Convirtiendo imagen a cadena de caracteres divididos por RGB");

			cadena = cadenaCompletaDeColores(URLImagen);

			

			/**2. Compresion RLE*/

			System.out.println("Comprimiendo cadena de caracteres con RLE");
			String cadenaComprimida = compresionDeImagenRLE(cadena);

	/** I Conteo de bits de datos sin comprimir */

			String cantidadBitsSinComprimir = cadena;

	/** II Conteo de bits de datos comprimidos */

			String cantidadBitsConCompresion = cadenaComprimida;
			
	/** III Relacion de compresion de compresion de imagen con Run-length encoding */

			double cantidadSin = contarBits(cantidadBitsSinComprimir, "Sin compresión");
			double cantidadCon = contarBits(cantidadBitsConCompresion, "Con compresión");			
			double RC = cantidadSin/cantidadCon;
			
			DecimalFormat df = new DecimalFormat("#.00");
			
			System.out.println("Relación de compresión: " + RC);
			
			
			/**3. Secuencia binaria*/

			System.out.println("Codificando cadena comprimida por RLE");
			String secBinaria = codificacionBinariaCompleta(cadenaComprimida);


			/**4. Decodificacion de secuencia binaria enviada por el canal */

			System.out.println("Decodificando la secuencia binaria");
			String cadenaDecodificada = decodificacionBinaria(secBinaria);


			/**5. Descompresion RLE de cadena decodificada*/

			System.out.println("Descomprimiendo cadena con RLE");
			String cadenaDescomprimida = descompresionRLE(cadenaDecodificada);

			/** 6. Construcción de imágen apartir de cadena de caracteres*/


			construirYMostrarImagenDeCadena(cadenaDescomprimida);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
