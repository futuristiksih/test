package com.example.test;

public class objectParent{
    private String name, phone,email,address,count,uid,token;
    objectParent(){}
         objectParent(String name, String phone, String email, String address,String count,String uid,String token){
         this.name=name;
         this.phone =phone;
         this.address=address;
         this.email=email;this.count=count;this.uid=uid;this.token=token;
         }
    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getEmail(){ return email; }
    public String getPhone(){ return phone; }
    public String getCount(){ return count; }
    public String getUid(){return uid;}
    public String getToken(){return token;}
}
