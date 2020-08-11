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

import java.io.File;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * Stores the data for a year. Adds months from CSV file. Gets year from file
 * name.
 * 
 * @author Alec Osmak
 */
class YearData {

   private ObservableList<MonthData> monthList; // list of months in the year
   private int year; // year of this instance
   private int totalYearWeight; // total weight for year


   /**
    * Creates a new year from a file for a month and initializes fields.
    * 
    * @param file      The CSV file for the month.
    * @param yearMonth The year and month of the file.
    */
   YearData(File file, String[] yearMonth) {
      monthList = FXCollections.observableArrayList(); // creates new list
      year = Integer.parseInt(yearMonth[0]);
      totalYearWeight = 0;

      addMonthData(file, yearMonth[1]); // adds month to the year
   }


   /**
    * Returns the data for all months in this year.
    * 
    * @return The list of months.
    */
   ObservableList<MonthData> getMonthList() {
      return monthList;
   }


   /**
    * Returns the year that it is.
    * 
    * @return The year field.
    */
   int getYear() {
      return year;
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
    * Finds and returns a month of data from its name.
    * 
    * @param month The month to search for.
    * @return The month of data found, or null if the month doesn't exist.
    */
   MonthData getMonthData(Months month) {
      for (MonthData monthData : monthList) // goes through all months
         if (monthData.getMonth() == month)
            return monthData;

      return null;
   }


   /**
    * Adds a month to this year from a file and the month.
    * 
    * @param file     The file that contains a month of data.
    * @param numMonth A string value for the numerical month.
    */
   void addMonthData(File file, String numMonth) {
      // turns int month as String into Months value
      Months month = Months.values()[Integer.valueOf(numMonth) - 1];

      MonthData existingMonth = getMonthData(month);
      if(existingMonth != null) { // checks if month already exists
         addToMonth(existingMonth, file);
         return;
      }
      
      // creates new month
      MonthData newMonth = new MonthData(file, month);
      monthList.add(newMonth);

      totalYearWeight += newMonth.getTotalMonthWeight();
   }


   /**
    * Adds data from a file to an already existing month in this year. Will
    * overwrite a day if the new day has the same date and farm id.
    * 
    * @param month The existing month.
    * @param file  The file of data.
    */
   void addToMonth(MonthData month, File file) {
      try { // tries to load file and read its lines
         Scanner reader = new Scanner(file);
         reader.nextLine(); // skips first line
         Boolean failed = false; // whether or not it failed to create a day
         int failedLines = 0; // how many lines failed

         while (reader.hasNextLine()) { // goes through the file
            String[] line = reader.nextLine().split(","); // separates file line
            String date = line[0]; // gets date value

            try {
               // stores info from the line of the file
               String farmID = line[1].trim();
               int weight = Integer.parseInt(line[2]);

               if (farmID.equals("")) // filters out empty string
                  throw new Exception();
               
               month.addDay(date, farmID, weight);

            } catch (Exception e) { // catches when data is not right
               failed = true;
               failedLines++;
            }
         }

         reader.close();

         if (failed) { // shows error dialog
            Alert alert = new Alert(AlertType.ERROR, failedLines + " lines of "
                  + "data had errors and were skipped. All other lines have "
                  + "still been added.");
            alert.setHeaderText("Error Loading " + file.getName());
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);
            alert.show();
         }

      } catch (Exception e) { // catches rest of exceptions (shouldn't happen)
         System.out.println("Error: Unknown\n");
         e.printStackTrace();
      }

      // recalculates total weight for the year
      totalYearWeight = 0;
      for (MonthData currentMonth : monthList)
         totalYearWeight += currentMonth.getTotalMonthWeight();
   }


}
