package com.example.test;

public class myDoctors {
    private String ap_id,doc_name,name,date,status;
    myDoctors(){}
    myDoctors(String name,String ap_id,String doc_name,String status,String date){
        this.name=name;this.ap_id=ap_id;this.date=date;this.doc_name=doc_name;this.status=status;
    }
    String getName(){return name;}
    public String getDoc_name(){ return doc_name; }
    public String getDate(){ return date; }
    public String getStatus(){ return status;}
    public String getId(){return ap_id;}
}
