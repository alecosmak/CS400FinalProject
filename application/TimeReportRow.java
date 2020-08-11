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
 * This class is the object stored in the rows of the annual and monthly report
 * tables. It represents the total weight for a single farm during a particular
 * year or year and month depending on the report.
 * 
 * @author Alec Osmak
 */
public class TimeReportRow {

   private String farmID; // name of this farm
   private int totalWeight; // total weight for this farm during time period
   private int allFarmsWeight; // total weight of all farms during time period
   private DecimalFormat percentFormatter; // formats percent

   /**
    * Creates a new instance using the farm's id and the sum of weight for all
    * farms.
    * 
    * @param farmID         The id of this farm.
    * @param allFarmsWeight The total weight of all farms.
    */
   TimeReportRow(String farmID, int allFarmsWeight) {
      this.farmID = farmID;
      totalWeight = 0;
      this.allFarmsWeight = allFarmsWeight;
      percentFormatter = new DecimalFormat("###.##");
   }

   /**
    * Gets the farm id field of this instance.
    * 
    * @return The farm id.
    */
   public String getFarmID() {
      return farmID;
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
    * for all farms. The percent is rounded to two decimal places.
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
