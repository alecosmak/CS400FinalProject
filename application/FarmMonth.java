package application;

import java.util.ArrayList;

public class FarmMonth {

   private int farmMonthWeight;
   private String farmID;
   private ArrayList<FarmDay> days;
   private int numDays;
   
   public int getNumDays() {
      return numDays;
   }

   public int getMonthWeight() {
      return farmMonthWeight;
   }
   
   public FarmMonth() {

   }

   public String getFarmID() {
      return farmID;
   }

   public FarmDay getDay(int day) {

      for (FarmDay farmDay : days) {
         if (farmDay.getDay() == day)
            return farmDay;
      }

      return null;
   }

   public void setDaysWeight(int day) {

   }

}
