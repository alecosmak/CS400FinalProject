package application;

import java.text.DecimalFormat;

public class FarmReportRow {

   private Months month;
   private int totalWeight;
   private int allFarmsWeight;
   private DecimalFormat percentFormatter;

   FarmReportRow(Months month, int allFarmsWeight) {
      this.month = month;
      this.allFarmsWeight = allFarmsWeight;
      totalWeight = 0;
      percentFormatter = new DecimalFormat("###.##");
   }


   public Months getMonth() {
      return month;
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
