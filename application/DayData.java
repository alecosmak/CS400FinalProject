package application;

public class DayData {

   private int day;
   private String farmID;
   private int weight;
   
   
   public DayData(int day, String farmID, int weight) {
      this.day = day;
      this.farmID = farmID;
      this.weight = weight;
   }
   
   
   public int getDay() {
      return day;
   }
   
   public String getFarmID() {
      return farmID;
   }
   
   public int getWeight() {
      return weight;
   }
   
}
