package com.example.test;

public class objectChild {
    private String name,dob,gender,birth_weight;
    objectChild(){}
    objectChild(String name,String dob,String gender,String birth_weight){
        this.name=name;this.gender=gender;this.dob=dob;this.birth_weight=birth_weight;
    }
    public String getName(){return name;}
    public String getGender(){ return gender; }
    public String getBirth_weight(){ return birth_weight; }
    public String getDob(){ return dob; }

}
