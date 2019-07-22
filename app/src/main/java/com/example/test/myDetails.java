package com.example.test;

public class myDetails{
    private String ap_id,doc_name,date,status;
    myDetails(){}
    myDetails(String ap_id,String doc_name,String status,String date){
        this.ap_id=ap_id;this.date=date;this.doc_name=doc_name;this.status=status;
    }
    public String getDoc_name(){ return doc_name; }
    public String getDate(){ return date; }
    public String getStatus(){ return status;}
    public String getId(){return ap_id;}
}
