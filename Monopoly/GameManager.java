/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.regex.Pattern;


/**
 *
 * @author smbmo
 */
public class GameManager {
    
    public void start(){   //Inicio de la secuencia del programa
        Game game = null;
        Terminal terminal = new TextTerminal();
        TranslatorManager tm = terminal.getTranslatorManager();
        tm.changeLanguage(askForLanguage(terminal)-1);  //Preguntamos el idioma al usuario
        String fileName;
        if (newOrLoad(terminal)){
            fileName = askForResumeGame(terminal);
            if (fileName != null){
                try{
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.FILEPATH_OLDGAMES + fileName));
                    game = (Game) ois.readObject();
                    ois.close();
                    game.setTerminal(terminal);  //Como la terminal es transient y no se seraliza al cargar una partida tendremos que incluirla en las clases que haga falta
                }   
                catch (IOException|ClassNotFoundException e){
                    terminal.show("No se ha podido cargar la partida, el fichero esta dañado o no contiene una partida");
                }
            }
            else{
                fileName = askForNewGameName(terminal);
                game = createGame(terminal,fileName);  //Creamos una partida en el caso de que no haya partidas guardadas
            }
        }
        else{
            fileName = askForNewGameName(terminal);
            game = createGame(terminal,fileName);  //Creamos una partida en el caso de que el usuario lo solicite
        } 
    game.play(fileName.replaceFirst("\\.obj$",""));  //Para evitar crear una propiedad en game que guarde el nombre del fichero (no es responsabilidad de game saber en que fichero se guarda), le pasamos como parametro al metodo el nombre del fichero para ir seralizando por cada turno
    }
    
    private boolean newOrLoad(Terminal terminal){  //Pregunta por la terminal si se desea cargar o crear una partida, devolviendo un valor booleano
        int opt;
        do{
            terminal.show("%s.Cargar una partida existente"+Constants.LINE_ARGS_SPLITTER+0);
            terminal.show("%s.Crear una nueva partida"+Constants.LINE_ARGS_SPLITTER+1);
            terminal.show("Introduzca numero de opcion");
            opt = terminal.read();
        }
        while (!(opt == 0 || opt == 1));
    return opt == 0;
    }
    
    private String askForResumeGame(Terminal terminal){  //Pregunta por la terminal el nombre de la partida que se desea cargar, permitiendo cargar solo partidas validas 
        String s = null;
        File carpeta = new File(Constants.FILEPATH_OLDGAMES);
        File[] archivos = carpeta.listFiles();
        if (archivos != null){ 
            int validGames = 0;
            for (int counter = 0; counter < archivos.length; counter++){
                File archivo = archivos[counter];
                if (archivo.isFile() && archivo.getName().endsWith(".obj")){  //Solo muestra por pantalla aquellos archivos con extension .obj
                    terminal.show("Partida %s"+Constants.LINE_ARGS_SPLITTER+(counter+1)+" : "+archivo.getName().replaceFirst("\\.obj$",""));    
                    validGames++;
                }
            }
            if (validGames != 0){  //Ya que podria ocurrir que hubiera archivos, pero por algun error ninguno fuera .obj
                terminal.show("Introduzca el numero de fichero de partida y pulse intro");
                int fileNum = terminal.read();
                if ((fileNum < archivos.length+1) && (fileNum > 0) && (archivos[fileNum-1].getName()).endsWith(".obj")){
                    s = archivos[fileNum-1].getName();
                }
                else{
                    terminal.show("Ha introducido un numero de partida invalido. Se creara una nueva partida");
                }
            }
            else{
                terminal.show("No existen partidas validas guardadas, tendra que crear una nueva") ;   
            }
        }
        else{
            terminal.show("No existen partidas guardadas, tendra que crear una nueva");
        }
    return s;
    }
    
    private int askForLanguage(Terminal terminal){  //Pregunta el idioma en el que se quiere iniciar el programa
        int opt;
        do{
            System.out.println("1.Spanish\n2.English\n3.Euskera\n4.Catalan"); //Mostramos sin la terminal ya que aun no se ha seleccionado el idioma 
            System.out.println("Seleccione idioma 1, 2, 3 or 4");
            opt = terminal.read();
        }
        while ((opt < 1) || (opt > 4));
        return opt;
    }

    private boolean existingGame(String fileName) {  //Busca si ya existen partidas con ese nombre
        boolean exists = false;
        File carpeta = new File(Constants.FILEPATH_OLDGAMES);
        File[] archivos = carpeta.listFiles();
        for  (File archivo : archivos){
            exists = exists || (archivo.getName().equals(fileName + ".obj"));
        }
    return exists;
    }
    
    private String askForNewGameName(Terminal terminal){  //Pregunta al usuario el nombre que le quiere poner a la partida
    boolean nameAlreadyExists;
    boolean validName;
    String fileName;
        do{
            terminal.show("Introduzca el nombre con el que quiere guardar la partida (tan solo letras, numeros y guion bajo)");
            fileName = terminal.readString();
            nameAlreadyExists = existingGame(fileName);
            validName = checkValidGameName(fileName);
            if (nameAlreadyExists){
                terminal.show("Ya existe una partida con ese nombre, pruebe con otro");
            }
            if (!validName){
                terminal.show("El nombre de la partida no es valido");
            }
        }
       while (nameAlreadyExists || !validName);
    return fileName;
    }
    
    private Game createGame(Terminal terminal, String fileName){  //Pasos para crear una partida y guardarla una vez creada
        Game game = new Game(terminal);
        game.initialize();
        game.serialize(fileName);
    return game;
    }

    private boolean checkValidGameName(String fileName) {  //Comprueba si el nombre de partida introducido es valido
        Pattern patron = Pattern.compile("^[a-zA-Z0-9_]+$");  //La expresion regular comprueba que la palabra solo tiene letras mayusculas, minusculas, numeros y guiones bajos
    return patron.matcher(fileName).matches();
    }
    
}
