/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smbmo
 */
public class Translator {
    
    private Map<String, String> dictionary;
    
    public Translator(String dictionaryFileName){  //Constructor de la clase Translator, que dado un nombre de fichero de traduccion permite crear el diccionario de traducciones asociado a dicho idioma 
        dictionary = new HashMap();
        try {
            Reader in = new FileReader(dictionaryFileName);
            BufferedReader buf = new BufferedReader(in,10);
            String s;
            s = buf.readLine();
            while (s != null){
                String [] line = s.split(Constants.TRANSLATOR_SPLITTER);
                if (line.length == 2){
                    dictionary.put(line[0],line[1]);
                }
                s = buf.readLine();
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public String translate(String s){  //Traduce una frase obteniendo el valor asociado a ella en el diccionario correspondiente al idioma que TranslatorManager tenga como currentIdiom
        return dictionary.get(s);
    }
    
}
