package com.example.test;

public class myPatients {
    private String ap_id,guardian,name,date,status;
    myPatients(){}
    myPatients(String name,String ap_id,String guardian,String status,String date){
        this.name=name;this.ap_id=ap_id;this.date=date;this.guardian=guardian;this.status=status;
    }
    String getName(){return name;}
    public String getGuardian(){ return guardian; }
    public String getDate(){ return date; }
    public String getStatus(){ return status;}
    public String getId(){return ap_id;}
}
