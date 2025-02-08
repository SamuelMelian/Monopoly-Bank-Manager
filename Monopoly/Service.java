/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

/**
 *
 * @author smbmo
 */
public class Service extends Property{
    
    private final int[] costStaying;

    public Service(String[] s,Terminal terminal) {  //Constructor de la clase Service, recibiendo un array de Strings proveniente de la frase de configuracion y la terminal del programa
        super(Integer.parseInt(s[0]),s[2],terminal,Integer.parseInt(s[5])*2,false,Integer.parseInt(s[5]));  //Llamamos al constructor de la clase padre pasandole los parametros necesarios para la clase padre y tambien la clase "abuelo"
        costStaying = new int[2];
        for (int i = 0; i <= 1; i++){
            costStaying[i] = Integer.parseInt(s[i+3]);
        }
    }

    @Override
    public String toString() {  //  El metodo toString() de Service es el metodo base para cualquier propiedad sin sobreescribir nada nuevo en el metodo
        return super.toString();
    }

    @Override
    public void doOwnerOperations() {  //Implementacion concreta del metodo operaciones del propietario para un servicio
        switch (doOwnerOperationsMenu()){
            case 1 -> mortgageProperty();
            case 2 -> unmortgageProperty();
        }
    }
    
    private int doOwnerOperationsMenu(){  //Menu del metodo de operaciones del propietario que devuelve un numero de opcion seleccionada
        int opt;
        do{
            terminal.show("%s.Hipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+1);
            terminal.show("%s.Deshipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+2);
            terminal.show("Introduzca numero de opcion");
            opt = terminal.read();
        }
        while ((opt < 1) || (opt > 2));
    return opt;
    }

    @Override
    public int getPaymentForRent() { //Devuelve un entero correspondiente al valor de alquiler en funcion del numero de los dados obtenido y del numero de servicios en posesion del propietario
        int dices;
        do{
            terminal.show("Introduzca el numero obtenido en los dados (Entre %s y %s)"+Constants.LINE_ARGS_SPLITTER+Constants.MIN_DICES+Constants.LINE_ARGS_SPLITTER+Constants.MAX_DICES);
            dices = terminal.read();
        }
        while (!((dices >= Constants.MIN_DICES) && (dices <= Constants.MAX_DICES)));
        int services = getPropertiesOfThisClass();
    return (costStaying[services-1]*dices);
    }

    @Override
    public void showPaymentSummary(int amount, Player player) {  //Muestra un resumen del pago a otro jugador por caer en una casilla de servicio
        Player owner = this.getOwner();
        terminal.show("El jugador %s usara la propiedad %s. Por ello, pagara %s al jugador %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+amount+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor()));
    }
    
}
