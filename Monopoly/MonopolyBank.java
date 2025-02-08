package Monopoly;

/**
 *
 * @author 
 */
public class MonopolyBank {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // El fichero con los codigos se encuentra en "config/MonopolyCode.txt"
        // Los idiomas deben estar en la carpeta "config/languages/"
        // Las partidas antiguas deberán estar en la carpeta "config/oldGames/"
        System.out.println("BIENVENIDO AL MONOPOLY");
        GameManager gameManager = new GameManager();
        gameManager.start();
    }
    
}
