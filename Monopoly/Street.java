/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

/**
 *
 * @author smbmo
 */
public class Street extends Property{
    
    private final int housePrice;
    private final int[] costStayingWithHouses;
    private int builtHouses;
    
    public Street(String[] s, Terminal terminal) {
        super(Integer.parseInt(s[0]),s[2],terminal,Integer.parseInt(s[11])*2,false,Integer.parseInt(s[11]));
        costStayingWithHouses = new int[6];
        for (int i = 0; i <= 5; i++){
            costStayingWithHouses[i] = Integer.parseInt(s[i+3]);
        }
        housePrice = Integer.parseInt(s[9]);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(super.toString());
        if (builtHouses == Constants.MAX_HOUSES){
            s.append("\nCasas construidas: %s casas y %s hotel" + Constants.LINE_ARGS_SPLITTER).append(builtHouses-1).append(Constants.LINE_ARGS_SPLITTER).append(1);
        }
        else{
            s.append("\nCasas construidas: %s casas" + Constants.LINE_ARGS_SPLITTER).append(builtHouses);
        }
    return s.toString();
    }

    @Override
    public void doOwnerOperations() {  //Implementacion del metodo concreta para una calle
        switch (doOwnerOperationsMenu()){
            case 1 -> buyHouses();
            case 2 -> sellHouses();
            case 3 -> mortgageProperty();
            case 4 -> unmortgageProperty();
        }
    }
    
    private int doOwnerOperationsMenu(){
        int opt;
        do{
            terminal.show("%s.Comprar casas"+Constants.LINE_ARGS_SPLITTER+1+"\n%s.Vender casas"+Constants.LINE_ARGS_SPLITTER+2+"\n%s.Hipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+3+"\n%s.Deshipotecar la propiedad"+Constants.LINE_ARGS_SPLITTER+4);
            terminal.show("Introduzca numero de opcion");
            opt = terminal.read();
        }
        while ((opt < 1) || (opt > 4));
    return opt;
    } 

    private void buyHouses(){
        if (!this.getMortgaged()){
            Player owner = this.getOwner();
            terminal.show("Introduzca el numero de casas que quiere comprar");
            int involvedHouses = terminal.read();
            if (((involvedHouses) > 0) & (involvedHouses + builtHouses) <= Constants.MAX_HOUSES){
                int amount = involvedHouses * housePrice;
                showHousePurchaseSummary(involvedHouses, amount, owner);
                if(confirmationMessage()){
                    if (owner.pay(amount, false)){
                        builtHouses += involvedHouses;
                        terminal.show("Compra realizada con exito");
                    }
                    else{
                        terminal.show("No se ha podido realizar la compra ya que no dispone de suficiente dinero");
                    }
                }
            }
            else{
                terminal.show("El numero maximo de casas para cada propiedad es de %s. Tan solo puede comprar %s casas en esta propiedad"+Constants.LINE_ARGS_SPLITTER+Constants.MAX_HOUSES+Constants.LINE_ARGS_SPLITTER+(Constants.MAX_HOUSES-builtHouses));
            }
        }
        else{
            terminal.show("No puede comprar casas, la propiedad esta hipotecada");
        }
    }

    public void sellHouses() {
        if (!this.getMortgaged()){
            Player owner = this.getOwner();
            terminal.show("Introduzca el numero de casas que quiere vender");
            int involvedHouses = terminal.read();
            if ((involvedHouses > 0) & (involvedHouses <= builtHouses)){
                int amount = involvedHouses * (housePrice/2);
                showHouseSellSummary(involvedHouses, amount, owner);
                if (confirmationMessage()){
                    owner.pay(-amount, false);
                    builtHouses -= involvedHouses;
                    terminal.show("Venta realizada con exito");
                }
            }
            else{
                terminal.show("No puede vender mas casas de las que tiene construidas. Esta propiedad solo tiene %s casas construidas"+Constants.LINE_ARGS_SPLITTER+builtHouses);
            }
        }
        else{
            terminal.show("No puede vender casas, la propiedad esta hipotecada");
        }
    }

    private void showHousePurchaseSummary(int involvedHouses,int amount, Player owner) {
        if (involvedHouses == 1){
            if (builtHouses == Constants.MAX_HOUSES-1)
                terminal.show("Se va a realizar la compra de 1 hotel para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            else{
                terminal.show("Se va a realizar la compra de 1 casa para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
        }
        else{
            if (builtHouses+involvedHouses == Constants.MAX_HOUSES){
                terminal.show("Se va a realizar la compra de %s casas y 1 hotel para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+(involvedHouses-1)+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
            else{
                terminal.show("Se va a realizar la compra de %s casas para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+involvedHouses+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
        }
    }

    private void showHouseSellSummary(int involvedHouses, int amount, Player owner) {
        if (involvedHouses == 1){
            if(builtHouses == Constants.MAX_HOUSES){
                terminal.show("Se va a realizar la venta de 1 hotel para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
            else{
                terminal.show("Se va a realizar la venta de 1 casa para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
        }
        else{
            if (builtHouses == Constants.MAX_HOUSES){
                terminal.show("Se va a realizar la venta de %s casas y 1 hotel para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+(involvedHouses-1)+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
            else{
                terminal.show("Se va a realizar la venta de %s casas para la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+involvedHouses+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
            }
        }
    }

    public int getBuiltHouses(){
        return builtHouses;
    }

    @Override
    public int getPaymentForRent() { 
        return costStayingWithHouses[builtHouses];
    }

    @Override
    public void showPaymentSummary(int amount, Player player) {
        Player owner = this.getOwner();
        if (builtHouses == Constants.MAX_HOUSES){
            terminal.show("El jugador %s usara la propiedad %s con %s casas y un hotel. Por ello, pagara %s al jugador %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+(builtHouses-1)+Constants.LINE_ARGS_SPLITTER+amount+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor()));
        }
        else{
            terminal.show("El jugador %s usara la propiedad %s con %s casas. Por ello, pagara %s al jugador %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+builtHouses+Constants.LINE_ARGS_SPLITTER+amount+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(owner.getColor()));
        }
    }

}
