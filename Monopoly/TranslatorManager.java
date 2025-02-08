/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.util.ArrayList;

/**
 *
 * @author smbmo
 */
public class TranslatorManager {
    
    private Translator currentIdiom;
    private Translator[] translators;
    
    public Translator getTranslator(){
        return currentIdiom;
    }
    
    public TranslatorManager(){  //Constructor de la clase TranslatorManager, que crea el array de traductores a partir de los ficheros de traduccion
        translators = new Translator[4];
        translators[0] = new Translator(Constants.FILEPATH_LANGUAGES+"spanish.txt");
        translators[1] = new Translator(Constants.FILEPATH_LANGUAGES+"english.txt");
        translators[2] = new Translator(Constants.FILEPATH_LANGUAGES+"euskera.txt");
        translators[3] = new Translator(Constants.FILEPATH_LANGUAGES+"catalan.txt");
    }
    
    public void changeLanguage(int i){  //Dado un numero de idioma, selecciona el idioma que debe pasar a ser currentIdiom
        currentIdiom = translators[i];
    }
    
}
