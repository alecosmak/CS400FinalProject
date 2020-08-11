/**
 * Project: Milk Weights Final Project
 * Files: RunFinalProject.java, YearData.java, MonthData.java,
 * DayData.java, FarmReportRow.java, TimeReportRow.java, Months.java,
 * cheeseLogo.jpg
 * 
 * Description: This is the final project for CS 400 Summer 2020. This program
 * is an interactive data visualizer that utilizes a GUI to display the data.
 * Through the GUI the user can add data from CSV files and display that data on
 * tables. The tables are interactive and give stats on the data.
 * 
 * Author: Alec Osmak
 * Email: osmak@wisc.edu
 */

package application;

import java.text.DecimalFormat;

/**
 * This class is the object stored in the rows of the farm report table. It
 * represents the total weight for a single farm and month during a particular
 * year.
 * 
 * @author Alec Osmak
 */
public class FarmReportRow {

   private Months month; // the month it is
   private int totalWeight; // total weight for this farm during this month
   private int allFarmsWeight; // total weight of all farms during this month
   private DecimalFormat percentFormatter; // formats percent

   /**
    * Creates a new instance using the month and the sum of weight for all farms
    * during that month.
    * 
    * @param month          The month that this instance represents.
    * @param allFarmsWeight The total weight of all farms during this month.
    */
   FarmReportRow(Months month, int allFarmsWeight) {
      this.month = month;
      this.allFarmsWeight = allFarmsWeight;
      totalWeight = 0;
      percentFormatter = new DecimalFormat("###.##");
   }

   /**
    * Gets the month field of this instance.
    * 
    * @return The month it is.
    */
   public Months getMonth() {
      return month;
   }

   /**
    * Gets the totalWeight field of this instance.
    * 
    * @return The total weight.
    */
   public int getTotalWeight() {
      return totalWeight;
   }

   /**
    * Returns the percent of the weight that this farm makes up out of the total
    * for all farms during this month. The percent is rounded to two decimal
    * places.
    * 
    * @return The percent of total weight this farm makes up.
    */
   public double getPercentTotal() {
      double percent = ((double) totalWeight / allFarmsWeight) * 100;
      return Double.valueOf(percentFormatter.format(percent));
   }

   /**
    * Adds the specified amount of weight to this farm's total.
    * 
    * @param weight The amount of weight to add.
    */
   void addWeight(int weight) {
      totalWeight += weight;
   }

}
