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
public class PaymentCharge extends MonopolyCode {
    
    private final int amount;
    
    public PaymentCharge(String[] s, Terminal terminal) {  //Constructor de la clase PaymentCharge, recibiendo un array de Strings proveniente de la frase de configuracion y la terminal del programa
        super(Integer.parseInt(s[0]),s[2],terminal);  //Llamada al constructor de la clase padre (monopolyCode) con sus correspondientes parametros
        if (s.length == 4){
            amount = Integer.parseInt(s[3]);
        }
        else{
            Pattern pattern = Pattern.compile("(-?\\d+)(€|$)");
            Matcher matcher = pattern.matcher(s[2]);
            if (matcher.find()){
                amount = Integer.parseInt(matcher.group(1));
            }
            else{
            amount = 0;
            }  
        }
    }

    @Override
    public void doOperation(Player player) {  //Sobreescribimos el metodo doOperation para implementarlo de manera concreta para esta clase. El metodo hace pagar una cantidad determinada (la que indique la tarjeta) a la banca
        showSummary(amount, player);
        if (super.confirmationMessage()){
            if(player.pay(-amount,true)){
                terminal.show("Transaccion realizada con exito");
            }
            else{
                terminal.show("El jugador %s ha quedado en bancarrota. Todo su dinero y propiedades restantes pasaran a la banca"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor()));
                player.setBankrupt();
                player.traspaseProperties(null);
            }
        }
    }

    private void showSummary(int money, Player player) {  //Muestra el resumen de pago o recepcion de dinero a/de la banca
        if (money == 0){
            terminal.show(this.getDescription() + " .[ACEPTAR/CANCELAR]");
        }
        else if (money<0){
            terminal.show("El jugador %s pagara %s a la banca. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+(-amount));
        }
        else{
            terminal.show("El jugador %s recibira %s de la banca. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
        }
    }
}
    
   
