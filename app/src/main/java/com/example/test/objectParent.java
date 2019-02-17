package com.example.test;

public class objectParent{
    private String name, phone,email,address;
    objectParent(){}
         objectParent(String name, String phone, String email, String address){
         this.name=name;
         this.phone =phone;
         this.address=address;
         this.email=email;
         }
    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getEmail(){ return email; }
    public String getPhone(){ return phone; }

}
