package com.example.test;

public class childDetails {
    private String immunization,bowel_movement,fever,inception,infected_area,intake,environment,crying,vomit,breast_feed,dehydration;
    childDetails(){}
    childDetails(String immunization,String bowel_movement,String fever,String inception,String infected_area,String intake,String environment,String crying,String vomit,String breast_feed,String dehydration){
        this.immunization=immunization;this.bowel_movement=bowel_movement;this.fever=fever;this.inception=inception;this.infected_area=infected_area;
        this.intake=intake;this.environment=environment;this.crying=crying;this.vomit=vomit;this.breast_feed=breast_feed;this.dehydration=dehydration;
    }
    String getImmunization(){return immunization;}
    String getBowel_movement(){return bowel_movement;}
    String getFever(){return fever;}
    String getInception(){return inception;}
    String getInfected_area(){return infected_area;}
    String getIntake(){return intake;}
    String getEnvironment(){return environment;}
    String getCrying(){return crying;}
    String getVomit(){return vomit;}
    String getBreast_feed(){return breast_feed;}
    String getDehydration(){return dehydration;}
}
