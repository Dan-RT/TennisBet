package com.udes.daniel.tennisbet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Points implements Parcelable {

    private ArrayList<Integer> sets;
    private ArrayList<ArrayList<Integer>> games;
    private ArrayList<Integer> exchange;

    public Points(){
        this.sets = new ArrayList<Integer>();
        this.games = new ArrayList<ArrayList<Integer>>();
        this.exchange = new ArrayList<Integer>();
    }

    public Points(ArrayList<Integer> sets, ArrayList<ArrayList<Integer>> games, ArrayList<Integer> exchange, boolean final_){
        this.sets = sets;
        this.games = games;
        this.exchange = exchange;
        this.final_ = final_;
    }

    public Points(int set_p_1, int set_p_2, ArrayList<ArrayList<Integer>> games, int exchange_p_1, int exchange_p_2, boolean final_){
        this.sets = new ArrayList<Integer>();
        this.games = new ArrayList<ArrayList<Integer>>();
        this.exchange = new ArrayList<Integer>();

        this.sets.add(set_p_1);
        this.sets.add(set_p_2);

        this.games = games;

        this.exchange.add(exchange_p_1);
        this.exchange.add(exchange_p_2);

        this.final_ = final_;
    }

    protected Points(Parcel in) {
        final_ = in.readByte() != 0;
    }

    public static final Creator<Points> CREATOR = new Creator<Points>() {
        @Override
        public Points createFromParcel(Parcel in) {
            return new Points(in);
        }

        @Override
        public Points[] newArray(int size) {
            return new Points[size];
        }
    };

    public ArrayList<Integer> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Integer> sets) {
        this.sets = sets;
    }

    public ArrayList<ArrayList<Integer>> getGames() {
        return games;
    }

    public void setGames(ArrayList<ArrayList<Integer>> games) {
        this.games = games;
    }

    public ArrayList<Integer> getExchange() {
        return exchange;
    }

    public void setExchange(ArrayList<Integer> exchange) {
        this.exchange = exchange;
    }

    private boolean final_;


    public boolean isFinal_() {
        return final_;
    }

    public void setFinal_(boolean final_) {
        this.final_ = final_;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (final_ ? 1 : 0));
    }
}
