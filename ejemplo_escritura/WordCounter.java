import java.io.*;

/*
 * Por cada linea del archivo de entrada, escribe en una linea la cantidad
 * de palabras (separadas por espacios) en el archivo de salida.
 * 
 * Probar con 'java WordCounter input_test.txt salida.txt'
 * 
 * G. Flores
 */
public class WordCounter {
    public static void main(String[] args) throws Exception {
        /* No es buena practica reportar toodas las
         * excepciones como 'Exception' pero funciona */
        if (args.length < 2) {
            System.out.println(" Uso: java WordCounter <archivo_entrada> <archivo_salida> ");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        File inputFile = new File(inputFileName);
        File outputFile =  new File(outputFileName);
        BufferedReader bRead = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bWrite = new BufferedWriter(new FileWriter(outputFile));
        // https://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html#FileWriter(java.io.File,%20boolean)

        String inputLine;
        int numberOfWords;
        while ((inputLine = bRead.readLine()) != null) {
            // en inputLine esta cada linea del archivo
            numberOfWords = inputLine.split(" ").length;
            bWrite.append(""+numberOfWords+"\n");
        }
        
        bWrite.close();

    }
}
