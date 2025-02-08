/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

/**
 *
 * @author smbmo
 */
public class Transport extends Property{
    
    private final int[] costStaying;
    
    public Transport(String[] s, Terminal terminal) {  //Constructor de la clase Transport, recibiendo un array de Strings proveniente de la frase de configuracion y la terminal del programa
        super(Integer.parseInt(s[0]),s[2],terminal,Integer.parseInt(s[7])*2,false,Integer.parseInt(s[7])); //LLamada al constructor de la clase padre (Property) pasandole los parametros necesarios para el constructor de la clase padre y tambien de la clase abuelo (MonopolyCode)
        costStaying = new int[4];
        for (int i = 0; i <= 3; i++){
            costStaying[i] = Integer.parseInt(s[i+3]);
        }
    }

    @Override
    public String toString() {  //  El metodo toString() de Service es el metodo base para cualquier propiedad sin sobreescribir nada nuevo en el metodo
        return super.toString();
    }

    @Override
    public void doOwnerOperations() { // Implementacion concreta del metodo que permite hacer operaciones al propietario sobre el transporte
        switch (doOwnerOperationsMenu()){
            case 1 -> mortgageProperty();
            case 2 -> unmortgageProperty();
        }
    }
    
    private int doOwnerOperationsMenu(){  //Muestra el menu de operaciones del propietario y devuelve el numero de opcion a realizar
        int opt;
        do{
            terminal.show("%s.Hipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+1+"\n%s.Deshipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+2);
            terminal.show("Introduzca numero de opcion");
            opt = terminal.read();
        }
        while ((opt < 1) || (opt > 2));
    return opt;
    }

    @Override
    public int getPaymentForRent() {  //Calcula y devuelve el precio de alquiler al caer en un transporte en base al numero de cartas de transporte que tenga el propietario
        int transports = getPropertiesOfThisClass();
    return costStaying[transports-1];
    }

    @Override
    public void showPaymentSummary(int amount, Player player) {  //Muestra el resumen del pago de un jugador al propietario por caer en su transporte
        Player owner = this.getOwner();
        terminal.show("El jugador %s usara la propiedad %s. Por ello, pagara %s al jugador %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+amount+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor()));
    }
    
    
}
