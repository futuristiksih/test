 package com.example.test;

import java.text.StringCharacterIterator;

public class doclistdesign {
    private String name,degree,rating,exp_yrs,city;
    doclistdesign(String name, String degree, String exp_yrs, String rating, String city) {
        this.name=name;
        this.exp_yrs= exp_yrs;
        this.degree=degree;
        this.rating=rating;
        this.city=city;
    }
    public String getName(){ return name;}
    public String getExp_yrs(){ return exp_yrs;}
    public String getDegree(){ return degree;}
    public String getRating(){ return rating;}
    public String getCity(){ return city;}
}