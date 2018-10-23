package com.udes.daniel.tennisbet;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {

    private String first_name;
    private String surname;
    private int age;
    private int rank;
    private String country;

    public Player (){

    }

    public Player(String first_name, String surname, int age, int rank, String country) {
        this.first_name = first_name;
        this.surname = surname;
        this.age = age;
        this.rank = rank;
        this.country = country;
    }

    protected Player(Parcel in) {
        first_name = in.readString();
        surname = in.readString();
        age = in.readInt();
        rank = in.readInt();
        country = in.readString();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(first_name);
        parcel.writeString(surname);
        parcel.writeInt(age);
        parcel.writeInt(rank);
        parcel.writeString(country);
    }

    @Override
    public String toString() {
        return this.first_name + " " + this.surname ;
    }

}
