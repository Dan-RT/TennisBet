package com.udes.daniel.tennisbet;

public class Bet {

    private int idMatch;
    private int player;
    private double amount;

    public Bet() {

    }

    public Bet(int id, int player, double amount) {
        setIdMatch(id);
        setPlayer(player);
        setAmount(amount);
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toJSon() {
        return "{" +
                "  \"id_match\": " + idMatch + ",\n" +
                "  \"id_player\": " + player + ",\n" +
                "  \"bet_amount\": " + amount +
                "}";
    }
}
