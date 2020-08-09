/**
 * Project: Milk Weights Final Project
 * Files: project.zip (RunFinalProject.java, YearData.java, MonthData.java,
 * DayData.java, Months.java, cheeseLogo.jpg, README.txt)
 * 
 * Description: This is the final project for CS 400 Summer 2020. This program
 * is an interactive data visualizer that utilizes a GUI to display the data.
 * Through the GUI the user can add, copy, and change data.
 * 
 * Author: Alec Osmak
 * Email: osmak@wisc.edu
 */

package application;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores the data for a year. Can adds months from a file.
 * 
 * @author Alec Osmak
 */
class YearData {

   private ObservableList<MonthData> monthList; // list of months in the year
   private int year;
   private int numMonths;
   private int totalYearWeight;


   /**
    * Creates a new year from a file for a month. Also initializes fields.
    * 
    * @param file      The file for a month that is part of a new year.
    * @param yearMonth The year and month of the file.
    */
   YearData(File file, String[] yearMonth) {
      monthList = FXCollections.observableArrayList(); // creates new list
      year = Integer.parseInt(yearMonth[0]);
      numMonths = 0;
      totalYearWeight = 0;

      addMonthData(file, yearMonth[1]);
   }


   /**
    * Returns the list of months for this year.
    * 
    * @return The list of months.
    */
   ObservableList<MonthData> getMonthList() {
      return monthList;
   }


   /**
    * Finds and returns a month of data from its month name in the enum.
    * 
    * @param month The name of the month.
    * @return The month of data found, or null if the month doesn't exist.
    */
   MonthData getMonthData(Months month) {
      for (MonthData monthData : monthList) {
         if (monthData.getMonth() == month)
            return monthData;
      }

      return null;
   }


   /**
    * Returns the year that this is.
    * 
    * @return The year it is.
    */
   int getYear() {
      return year;
   }


   /**
    * Returns how many months this year currently has listed.
    * 
    * @return The number of months in this year.
    */
   int getNumMonths() {
      return numMonths;
   }


   /**
    * Returns the sum of all weights sold during this year
    * 
    * @return The total weight for this year.
    */
   int getTotalYearWeight() {
      return totalYearWeight;
   }


   /**
    * Adds a month to this year from a file and the month.
    * 
    * @param file     The file that contains a month of data.
    * @param numMonth A string value for the numerical month.
    */
   void addMonthData(File file, String numMonth) {
      // gets the name of the month from its integer value
      Months month = Months.values()[Integer.parseInt(numMonth) - 1];

      MonthData newMonth = new MonthData(file, month);
      
      for(MonthData monthData : monthList) {
         if(monthData.getMonth() == newMonth.getMonth()) {
            totalYearWeight -= monthData.getTotalMonthWeight();
            monthData = newMonth;
            totalYearWeight += newMonth.getTotalMonthWeight();
            return;
         }
      }
      
      monthList.add(newMonth);

      totalYearWeight += newMonth.getTotalMonthWeight();
      numMonths++;
   }


}
