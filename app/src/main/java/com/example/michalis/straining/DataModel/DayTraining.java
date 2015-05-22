package com.example.michalis.straining.DataModel;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import com.example.michalis.straining.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Michalis on 4/30/2015.
 *
 * Class to model a Day Training
 * For constructor, we use Builder Pattern
 * we use Factory Constructor Method as well
 */
public class DayTraining implements Serializable {
    private static Calendar calendar = Calendar.getInstance();
    public static final String total = "Total";

    private int idAthlete;
    private String date;
    private String description;
    private int t1;
    private int t2;
    private int t3;
    private int t2_3;
    private int t1_3;
    private int ta;
    private int tt;
    private int gx;
    private int gp;
    private int gd;
    private int texniki;
    private int drastiriotita;
    private int time;
    private int number;
    private int id_race;

    private DayTraining(Builder builder){
        this.idAthlete = builder.idAthlete;
        this.date = builder.date;
        this.description = builder.description;
        this.t1 = builder.t1;
        this.t2 = builder.t2;
        this.t3 = builder.t3;
        this.t2_3 = builder.t2_3;
        this.t1_3 = builder.t1_3;
        this.ta = builder.ta;
        this.tt = builder.tt;
        this.gx = builder.gx;
        this.gp = builder.gp;
        this.gd = builder.gd;
        this.texniki = builder.texniki;
        this.drastiriotita = builder.texniki;
        this.time = builder.time;
        this.number = builder.number;
        this.id_race = builder.id_race;
    }

    /**
     * Factory Constructor for Day Training
     *
     * @param json
     * @return An object of Day Training that produced from json
     */
    public static DayTraining newInstance(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return  new DayTraining.Builder()
                        .setRunning( root.optInt("t1"), root.optInt("t2"), root.optInt("t3"), root.optInt("t2_3"), root.optInt("t1_3"), root.optInt("ta"), root.optInt("tt"))
                        .setBreathing(root.optInt("gx"), root.optInt("gp"),root.optInt("gd"))
                        .setTrainingInfo(root.optInt("id_athlete"), root.optString("date"), filterJsonTotalInput(root.optString("description")), root.optInt("texniki"), root.optInt("drastiriotita"), root.optInt("time"), root.optInt("number"), root.optInt("id_race"))
                        .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString() {
        return "DayTraining{" +
                "idAthlete=" + idAthlete +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t2_3=" + t2_3 +
                ", t1_3=" + t1_3 +
                ", ta=" + ta +
                ", tt=" + tt +
                ", gx=" + gx +
                ", gp=" + gp +
                ", gd=" + gd +
                ", texniki=" + texniki +
                ", drastiriotita=" + drastiriotita +
                ", time=" + time +
                ", number=" + number +
                ", id_race=" + id_race +
                '}';
    }

    /**
     * Return the String Total if no description is available
     *
     * @param input
     * @return
     */
    private static String filterJsonTotalInput(String input) {
        if (input.equals("null")) {
            return total;
        }
        return input;
    }

    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    public int getT1(){return t1;}

    public int getT2(){return t2;}

    public int getT3() {
        return t3;
    }

    public int getT2_3() {
        return t2_3;
    }

    public int getT1_3() {
        return t1_3;
    }

    public int getTt() {
        return tt;
    }

    public int getTa() {
        return ta;
    }

    public int getTime() {return time;}

    public int getTotalKm(){
        return t1 + t2 + t3 + tt + ta + t1_3 + t2_3;
    }

    public int getGx() {
        return gx;
    }

    public int getGp() {
        return gp;
    }

    public int getGd() {
        return gd;
    }

    public int getTexniki() {
        return texniki;
    }

    public int getDrastiriotita() {
        return drastiriotita;
    }

    /**
     * Returns the id of the day of the DayTraining
     *
     * @return an integer id for the day
     *         Sunday = 0
     *         Monday = 1
     */
    public int getDay(){
        String[] dateSplit = this.date.split("-");
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateSplit[2]));
        calendar.set(Calendar.MONTH, Integer.valueOf(dateSplit[1])-1);
        calendar.set(Calendar.YEAR, Integer.valueOf(dateSplit[0]));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String getDate() {String[] dateSplit = this.date.split("-"); return dateSplit[2]+"/"+dateSplit[1]+"/"+dateSplit[0];}



    /**
     * Builder Constructor Pattern
     */
    public static class Builder {
        private int idAthlete;
        private String date;
        private String description;
        private int t1;
        private int t2;
        private int t3;
        private int t2_3;
        private int t1_3;
        private int ta;
        private int tt;
        private int gx;
        private int gp;
        private int gd;
        private int texniki;
        private int drastiriotita;
        private int time;
        private int number;
        private int id_race;

        public DayTraining build(){
            return  new DayTraining(this);
        }

        public Builder setRunning(int t1, int t2, int t3, int t2_3, int t1_3, int ta, int tt){
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t2_3 = t2_3;
            this.t1_3 = t1_3;
            this.ta = ta;
            this.tt = tt;
            return this;
        }

        public Builder setBreathing(int gx, int gp, int gd) {
            this.gx = gx;
            this.gp = gp;
            this.gd = gd;
            return this;
        }

        public Builder setTrainingInfo(int idAthlete, String date, String description, int texniki,
                                       int drastiriotita, int time, int number, int id_race){
            this.idAthlete = idAthlete;
            this.date = date;
            this.description = description;
            this.texniki = texniki;
            this.drastiriotita = drastiriotita;
            this.time = time;
            this.number = number;
            this.id_race = id_race;
            return this;
        }
    }
}
