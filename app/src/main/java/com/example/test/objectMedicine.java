package com.example.test;

public class objectMedicine {
    private String medicine, condition;
    private int week, dose;
    objectMedicine(){}
    objectMedicine(String medicine, int week, int dose, String condition){
        this.medicine = medicine;
        this.week = week;
        this.dose = dose;
        this.condition = condition;
    }
    public String getMedicine(){return medicine;}
    public String getCondition() { return condition;}
    public int getWeek(){return week;}
    public int getDose(){return dose;}
}
