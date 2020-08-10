package application;

import java.text.DecimalFormat;

public class TimeReportRow {

   private String farmID;
   private int totalWeight;
   private int allFarmsWeight;
   private DecimalFormat percentFormatter;


   TimeReportRow(String farmID, int allFarmsWeight) {
      this.farmID = farmID;
      totalWeight = 0;
      this.allFarmsWeight = allFarmsWeight;
      percentFormatter = new DecimalFormat("###.##");
   }

   public String getFarmID() {
      return farmID;
   }

   public int getTotalWeight() {
      return totalWeight;
   }

   public double getPercentTotal() {
      double percent = ((double) totalWeight / allFarmsWeight) * 100;
      return Double.valueOf(percentFormatter.format(percent));
   }

   void addWeight(int weight) {
      totalWeight += weight;
   }

}
