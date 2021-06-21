import java.io.*;

import java.util.*;

public class CodHuffman {
	
	private Map<Character, Integer> frec;
	private Character[] caract;
	private int nCaract;
	private Map<Character, String> mapaCodigos;
	private Heap minHeap;
	
	//Constructor
	public CodHuffman(Map<Character, Integer> mapa, Character[] arreglo, int n, Map<Character, String> cod) {
		frec = mapa;
		caract = arreglo;
		nCaract = n;
		mapaCodigos = cod;
		minHeap = new Heap();
	}
	
	//Metodo de instancia, construye un min heap con las frecuencias de los
	//caracteres a codificar
	public void construirHeap() {
		for(int i=0; i<caract.length; ++i) {
			float frecuencia = frec.get(caract[i]);
			NodoArbol nodo = new NodoArbol(new Tupla(caract[i], frecuencia));
			minHeap.insertar(nodo);
		}
	}
	
	//Metodo de instancia, construye un arbol con la codificacion de Huffman para
	//los caracteres almacenados
	public NodoArbol construirArbol() {
		//Iterar mientras el minHeap tenga mas de un elemento
		while(minHeap.largo() > 1) {
			NodoArbol min1 = minHeap.extraer();
			NodoArbol min2 = minHeap.extraer();
			NodoArbol nuevoArbol = min1.fusionar(min2);
			minHeap.insertar(nuevoArbol);
		}
		return minHeap.extraer();
	}
	
	//Clase tupla para generar tuplas (caracter,frecuencia)
	public class Tupla {
		
		private Character caracter;
		private float frecuencia;
		
		//Constructor
		public Tupla(Character c, float f) {
			caracter = c;
			frecuencia = f;
		}
	}
	
	//Clase heap para implementar un minHeap (prioridad ascendente)
	public class Heap {
		
		private List<NodoArbol> arreglo;
		
		//Constructor
		public Heap() {
			arreglo = new ArrayList<NodoArbol>();
		}
		
		//Metodo de instancia, retorna la cantidad de nodos en el heap
		public int largo() {
			return arreglo.size();
		}
		
		//Metodo de instancia, inserta una tupla en el heap
		public void insertar(NodoArbol nodo) {
			//Paso 1: Agregar el valor en la primera posición libre del heap
			arreglo.add(nodo);
			
			//Paso 2: Revisar si se cumple la restricción de orden
			//Calculcar ubicacion del nodo padre y nodo hijo en el arreglo (notar 
			//que arreglos parten en cero)
			int nodoHijo = arreglo.size() - 1;
			int nodoPadre = (nodoHijo / 2) - 1;
			
			//Iterar mientras el elemento no sea la raiz
			while(nodoPadre >= 0) {
				//Revisar si el padre es mayor que el hijo
				Tupla padre = arreglo.get(nodoPadre).val;
				Tupla hijo = arreglo.get(nodoHijo).val;
				if(padre.frecuencia > hijo.frecuencia) {
					arreglo.set(nodoHijo, arreglo.get(nodoPadre));
					arreglo.set(nodoPadre, nodo);
					//Actualizar ubicaciones de los nodos
					nodoHijo = nodoPadre;
					nodoPadre = (nodoHijo / 2) - 1;
				}
				//Si el padre es menor que el hijo, se cumple la condicion de orden
				else {
					break;
				}
			}
		}
		
		//Metodo de instancia, extrae el minimo elemento del heap (su raiz)
		public NodoArbol extraer() {
			//Extraer la raiz del heap
			NodoArbol minimo = arreglo.get(0);
			
			//Reemplazar raiz con el ultimo elemento del heap y eliminar raiz
			//original
			arreglo.set(0, arreglo.get(arreglo.size() - 1));
			arreglo.remove(arreglo.size() - 1);
			
			//Calcular indices del nodo padre y sus hijos (notar que indices 
			//parten del cero)
			int nodoPadre = 0;
			int nodoIzq = 2*(nodoPadre + 1) - 1;
			int nodoDer = 2*(nodoPadre + 1);
			
			//Iterar mientras el nodo padre tenga hijos
			while(nodoIzq < arreglo.size() - 1) {
				Tupla hijoMin = arreglo.get(nodoIzq).val;
				int nodoMin = nodoIzq;
				//Si el nodo padre tiene dos hijos, elegir el menor para comparar
				if(nodoDer < arreglo.size() - 1) {
					Tupla hijoDer = arreglo.get(nodoDer).val;
					if(hijoMin.frecuencia > hijoDer.frecuencia) {
						hijoMin = hijoDer;
						nodoMin = nodoDer;
					}
				}
				//Comparar nodo hijo con nodo padre
				Tupla padre = arreglo.get(nodoPadre).val;
				if(padre.frecuencia > hijoMin.frecuencia) {
					NodoArbol arbolPadre = arreglo.get(nodoPadre);
					arreglo.set(nodoPadre, arreglo.get(nodoMin));
					arreglo.set(nodoMin, arbolPadre);
					
					//Redefinir indices del padre e hijos
					nodoPadre = nodoMin;
					nodoIzq = 2*(nodoPadre + 1) - 1;
					nodoDer = 2*(nodoPadre + 1);
				}
				//Si se cumple la condicion de orden, temrinar el proceso
				else {
					break;
				}
			}
			return minimo;
		}
	}
	
	//Clase nodoArbol para implementar arboles binarios
	public class NodoArbol {
		
		private Tupla val;
		private NodoArbol izq;
		private NodoArbol der;
		
		//Constructor
		public NodoArbol(Tupla valor) {
			val = valor;
			izq = null;
			der = null;
		}
		
		//Constructor
		public NodoArbol(Tupla valor, NodoArbol izquierdo, NodoArbol derecho) {
			val = valor;
			izq = izquierdo;
			der = derecho;
		}
		
		//Metodo de instancia, fusiona dos arboles binarios, creando una nueva
		//raiz con frecuencia igual a la suma de sus dos hijos y caracter nulo
		public NodoArbol fusionar(NodoArbol otroArbol) {
			float nuevaFrec = val.frecuencia + otroArbol.val.frecuencia;
			Tupla nuevoValor = new Tupla(null, nuevaFrec);
			return new NodoArbol(nuevoValor, this, otroArbol);
		}
	}
	
	//Metodo de instancia, recorre el arbol de Huffman para obtener las
	//codificaciones de los simbolos codificados
	public void obtenerLineas(NodoArbol codigos, String prefijo, ArrayList<String> listaLineas, ArrayList<Float> listaFrecuencias) {
		Tupla tuplaActual = codigos.val;
		if(tuplaActual.caracter != null) {
			//Caso base: El nodo actual es una tupla (caracter,frecuencia)
			String caracter = Character.toString(tuplaActual.caracter);
			int ascii = (int) tuplaActual.caracter;
			String codAscii = Integer.toString(ascii);
			String codificacion = prefijo;
			float frecuencia = tuplaActual.frecuencia / nCaract * 100;
			String frecPorcentaje = Float.toString(frecuencia) + "%";
			
			String linea = String.format("%20s %20s %20s %20s", caracter, codAscii, codificacion, frecPorcentaje);
			listaLineas.add(linea);
			listaFrecuencias.add(frecuencia);
			
			mapaCodigos.put(tuplaActual.caracter, codificacion);
		}
		//Iterar a lo largo de todos los subarboles
		if(codigos.der != null) {
			obtenerLineas(codigos.der, prefijo+"1", listaLineas, listaFrecuencias);
		}
		if(codigos.izq != null) {
			obtenerLineas(codigos.izq, prefijo+"0", listaLineas, listaFrecuencias);
		}
	}
	
	//Metodo de instancia, escribe en un archivo la tabla de codificacion obtenida
	//para el archivo de entrada
	public void escribirTabla(ArrayList<String> listaLineas, ArrayList<Float> listaFrecuencias, Writer writer) throws IOException {
		//Paso 1: Ordenar arreglo de lineas en frecuencia descendente usando
		//Quicksort
		quicksort(listaFrecuencias, listaLineas, 0, listaFrecuencias.size()-1);
		//Paso 2: Escribir archivo
		for(int i=0; i<listaLineas.size(); ++i) {
			writer.write(listaLineas.get(i));
			writer.write(System.lineSeparator());
		}
	}
	
	//Metodo estatico, ordena un arreglo entre los indices inicio y fin
	//utilizando quicksort
	public static void quicksort(ArrayList<Float> listaOrdenada, ArrayList<String> listaAux, int inicio, int fin) {
		//Paso 0: Verificar validez del intervalo
		if(inicio < fin) {
			//Paso 1: Elegir un pivote aleatorio
			Random r = new Random();
			int rand = r.nextInt((fin - inicio) + 1) + inicio;
			//Paso 2: Situar pivote al final
			Float pivoteOrd = listaOrdenada.get(rand);
			String pivoteAux = listaAux.get(rand);
			listaOrdenada.set(rand, listaOrdenada.get(fin));
			listaOrdenada.set(fin, pivoteOrd);
			listaAux.set(rand, listaAux.get(fin));
			listaAux.set(fin, pivoteAux);
			//Paso 3: Crear subarreglos
			int i = inicio;
			for(int j=inicio; j<=fin-1; ++j) {
				if(listaOrdenada.get(j) > pivoteOrd) {
					Float valorOrd = listaOrdenada.get(i);
					String valorAux = listaAux.get(i);
					listaOrdenada.set(i, listaOrdenada.get(j));
					listaOrdenada.set(j, valorOrd);
					listaAux.set(i, listaAux.get(j));
					listaAux.set(j, valorAux);
					++i;
				}
			}
			listaOrdenada.set(fin, listaOrdenada.get(i));
			listaOrdenada.set(i, pivoteOrd);
			listaAux.set(fin, listaAux.get(i));
			listaAux.set(i, pivoteAux);
			//Paso 4: Ordenar recursivamente
			quicksort(listaOrdenada, listaAux, inicio, i-1);
			quicksort(listaOrdenada, listaAux, i+1, fin);
		}
	}

	public static void main(String[] args) {
		
		if (args.length < 2) {
            System.out.println(" Uso: java CodHuffman <archivo_entrada> <archivo_salida> ");
            return;
        }
		
		String dirEntrada = args[0];
		String dirSalida = args[1];
		
		//Crear mapa (simbolo, codigo)
		Map<Character, String> codigos = new HashMap<Character, String>();
		
		//Leer archivo de entrada y calcular pesos de cada letra
		try {
			File archivo = new File(dirEntrada);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "ISO-8859-1"));
			String linea;
			//Crear contador de frecuencias para cada caracter
			Map<Character, Integer> frecuencias = new HashMap<Character, Integer>();
			//Crear contador para guardar el numero total de caracteres
			int n = 0;
			while((linea = br.readLine()) != null) {
				for(int i=0; i<linea.length(); ++i) {
					++n;
					Integer contador = frecuencias.get(linea.charAt(i));
					int nuevoContador = (contador == null ? 1 : contador+1);
					frecuencias.put(linea.charAt(i), nuevoContador);
				}
			}	
			//Luego de leer el archivo completo, obtener el conjunto de caracteres
			//que aparecieron en el archivo
			Set<Character> llaves = frecuencias.keySet();
			Character[] caracteres = new Character[llaves.size()];
			llaves.toArray(caracteres);
			
			//Crear clase CodHuffman que permite construir un codigo de Huffman a
			//partir de un arreglo de caracteres, un mapa que asigne a cada
			//simbolo su frecuencia y el numero total de caracteres
			CodHuffman codigo = new CodHuffman(frecuencias, caracteres, n, codigos);
			
			//FIN PREPROCESAMIENTO DE LA ENTRADA
			//Paso 1: Construir min heap con las frecuencias de cada letra		
			codigo.construirHeap();
			//Paso 2: Crear arbol binario con la codificacion de Huffman
			NodoArbol codificacion = codigo.construirArbol();
			//Paso 3: Escribir tabla con la codificacion resultante
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirSalida), "ISO-8859-1"))) {
				writer.write(String.format("%20s %20s %20s %20s", "Carácter", "Cód ASCII (dec.)", "codificación", "frecuencia"));
				writer.write(System.lineSeparator());
				//Paso 3a: Obtener las lineas de los caracteres codificados, pero
				//no ordenados en frecuencia descendente
				ArrayList<String> lineas = new ArrayList<String>();
				ArrayList<Float> frecs = new ArrayList<Float>();
				codigo.obtenerLineas(codificacion, "", lineas, frecs);
				//Paso 3b: Ordenar los caracteres segun frecuencia y escribir la
				//codificacion resultante en el archivo de salida
				codigo.escribirTabla(lineas, frecs, writer);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//Paso 4: Crear archivo comprimido
		String[] carpetas = dirEntrada.split("/");
		String dirComprimido = "[COMPRIMIDO]" + carpetas[carpetas.length - 1];
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirComprimido), "ISO-8859-1"))) {
			try {
				//Leer archivo de entrada e ir escribiendo cada simbolo codificado 
				File archivo = new File(dirEntrada);
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "ISO-8859-1"));
				String linea;
				while((linea = br.readLine()) != null) {
					for(int i=0; i<linea.length(); ++i) {
						Character simbolo = linea.charAt(i);
						writer.write(codigos.get(simbolo));
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}