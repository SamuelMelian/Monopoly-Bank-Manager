/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author smbmo
 */
public class RepairsCard extends MonopolyCode{
    private final int amountForHouse;
    private final int amountForHotel;
    
    public RepairsCard(String[] s, Terminal terminal) { //Constructor de la clase Repairs, recibiendo un array de Strings proveniente de la frase de configuracion y la terminal del programa
        super(Integer.parseInt(s[0]),s[2],terminal);  //Llamada al constructor de la clase padre (monopolyCode) con sus correspondientes parametros
        Pattern pattern = Pattern.compile("(\\d+)(€)");
        Matcher matcher = pattern.matcher(s[2]);
        matcher.find();
        amountForHouse = Integer.parseInt(matcher.group(1));
        matcher.find();
        amountForHotel = Integer.parseInt(matcher.group(1));
    }

    @Override
    public void doOperation(Player player) { //Sobreescribimos el metodo doOperation para implementarlo de manera concreta para esta clase. El metodo hace pagar una cantidad determinada (la que indique la tarjeta) a la banca
        int payment = amountForHouse*player.getTotalHouses()+amountForHotel*player.getTotalHotels();
        showSummary(payment, player);
        if (confirmationMessage()){
            if(player.pay(payment,true)){
                terminal.show("Pago realizado con exito");
            }
            else{
                terminal.show("El jugador %s ha quedado en bancarrota. Todo su dinero y propiedades restantes pasaran a la banca"+terminal.translateColor(player.getColor()));
                player.setBankrupt();
                player.traspaseProperties(null);
            }
        }
    }

    private void showSummary(int payment, Player player) {   //Muestra el resumen de pago a la banca
        terminal.show("El jugador %s pagara %s a la banca por tener %s casas y %s hoteles. [ACEPTAR/CANCELAR]"+ Constants.LINE_ARGS_SPLITTER +terminal.translateColor(player.getColor())+ Constants.LINE_ARGS_SPLITTER +payment+ Constants.LINE_ARGS_SPLITTER +player.getTotalHouses()+ Constants.LINE_ARGS_SPLITTER +player.getTotalHotels());
    }
    
   
}
