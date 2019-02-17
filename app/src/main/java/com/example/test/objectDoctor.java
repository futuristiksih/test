package com.example.test;

public class objectDoctor {
    private String name, phone,email,degree,type;
    objectDoctor(){}
         objectDoctor(String name, String phone, String email, String degree,String type){
         this.name=name;this.phone =phone;this.degree=degree;this.email=email;this.type=type;
         }
    public String getName(){ return name; }
    public String getDegree(){ return degree; }
    public String getEmail(){ return email; }
    public String getPhone(){ return phone; }
    public String getType(){ return type; }

}
