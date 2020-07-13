package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class YearData {

   private int year;
   private int totalYearWeight;
   private ArrayList<MonthData> monthList;
   private int numMonths;


   public int getNumMonths() {
      return numMonths;
   }

   public ArrayList<MonthData> getMonthList() {
      return monthList;
   }

   public YearData(File file) {

   }

   public int getYear() {
      return year;
   }

   public int getTotalYearWeight() {
      return totalYearWeight;
   }

   public void addMonthData(File file) {
      String name = file.getName();
      name = name.substring(0, name.lastIndexOf(".")); // removes .csv

      String[] splitName = name.split("-");
      // gets the year and month from the title of the file
      int year = Integer.parseInt(splitName[0]);
      Months month = Months.values()[Integer.parseInt(splitName[1])];

      try {
         Scanner reader = new Scanner(file);
         reader.nextLine();
         
         while (reader.hasNextLine()) { // loops through all the lines of the
                                        // file
            String[] line = reader.nextLine().split(",");

            String[] date = line[0].split("-");
            
            int day = Integer.parseInt(date[2]);
            String farmID = line[1];
            int weight = Integer.parseInt(line[2]);

         }

         reader.close();

      } catch (Exception e) {
         System.out.println("Error: Unknown\n" + e);
      }

      numMonths++;
   }

   public void addMonthData(MonthData month) {
      numMonths++;
   }

   public MonthData getMonthData(Months month) {
      for (MonthData monthData : monthList) {
         if (monthData.getMonth() == month)
            return monthData;
      }

      return null;
   }


}
