package com.example.test;

public class objectDoctor {
    private String name,uid, phone,email,degree,gender,mci,specialization,clinic,city,exp_yrs,rating,token;boolean verified;
    int count;
    objectDoctor(){}
         objectDoctor(String name, String phone, String email, String degree,String gender,String clinic,String mci,String specialization,String city,String exp_yrs,String rating,boolean verified,String uid,int count,String token){
         this.name=name;this.phone =phone;this.degree=degree;this.email=email;this.clinic=clinic;this.city=city;this.exp_yrs=exp_yrs;
         this.gender=gender;this.mci=mci;this.specialization=specialization;this.rating=rating;this.verified=verified;this.uid=uid;
         this.count=count;this.token= token;
         }
    public String getName(){ return name; }
    public String getDegree(){ return degree; }
    public String getEmail(){ return email; }
    public String getPhone(){ return phone; }
    public String getMci(){ return mci; }
    public String getGender(){ return gender; }
    public String getSpecialization(){ return specialization; }
    public String getClinic(){ return clinic; }
    public String getCity(){ return city; }
    public String getExp_yrs(){ return exp_yrs; }
    public String getRating(){ return rating; }
    public boolean getVerified(){ return verified; }
    public String getUid(){return uid;}
    public int getCount(){ return count; }
    public String getToken(){return token;}
}
