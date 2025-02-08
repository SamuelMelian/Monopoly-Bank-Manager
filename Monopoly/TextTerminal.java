/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.util.Arrays;
import java.util.IllegalFormatConversionException;
import java.util.Scanner;

/**
 *
 * @author smbmo
 */
public class TextTerminal extends Terminal{
    
    @Override
    public void show (String string){  //Mostramos por la terminal una frase, antes obteniendo sus variables y la frase segun como lo hemos codificado y traduciendo la frase despues
        TranslatorManager tm = this.getTranslatorManager();
        Translator t = tm.getTranslator();
        Scanner scanner = new Scanner(string);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String [] parts = line.split(Constants.LINE_ARGS_SPLITTER);  //Separamos el texto(primera posicion del array) de las variables (posiciones 1--n)
            String translation = t.translate(parts[0]);
            Object[] args = Arrays.copyOfRange(parts, 1, parts.length);  //Pasamos las variables a un array de Objects para hacer correcamente String.format
            try{
            if (translation == null){
                System.out.printf(String.format(parts[0],args));  //Si no encuentra la frase a traducir en el diccionario, la devuelve sin traducir haciendo antes String.format
                System.out.println();
            }
            else{
                System.out.printf(String.format(translation,args));  //Si encuentra la traduccion, devuelve la frase traducida introduciendo las variables en su sitio con String.format
                System.out.println();
            }
            } catch (IllegalFormatConversionException ex){
                System.out.printf(String.format(parts[0],args)); //Para no parar la ejecucion del programa, si hubiese algun problema con el String.format(), mostraremos la frase en español
                System.out.println();
            }
        }
    }
    
    @Override
    public int read(){  //Permite leer un numero entero por pantalla
        Scanner scanner = new Scanner(System.in);
        int num;
        do {
            while (!scanner.hasNextInt()) {
                show("Error: Has introducido algo que no es un numero entero");
                scanner.next();
            }
            num = scanner.nextInt();
        } while (num < 0);
        return num;
    }

    @Override
    public String readString() {  //Permite leer un String por pantalla
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    
    @Override
    public String translateColor(Color color){  //Con este metodo traducimos los colores de manera separada a las frases, metiendolos como una variable en las frases que se muestran para no sobrecargar el diccionario con las mismas frases para diferentes colores
        TranslatorManager tm = this.getTranslatorManager();
        Translator t = tm.getTranslator();
        String colorTranslation = t.translate(color.toString());
        if (colorTranslation == null){
            return color.toString();
        }
        else{
            return colorTranslation;
        }
    }

}
    
    

