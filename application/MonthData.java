package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MonthData {
   private int totalMonthWeight;
   private Months month;
   private ArrayList<FarmMonth> farmList;
   private int numFarms;

   public int getNumFarms() {
      return numFarms;
   }

   public ArrayList<FarmMonth> getFarmList() {
      return farmList;
   }

   public MonthData(File file) {
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
   }

   public Months getMonth() {
      return month;
   }

   public int getTotalMonthWeight() {
      return totalMonthWeight;
   }

   public FarmMonth getFarm(String farmID) {
      for (FarmMonth farm : farmList) {
         if (farm.getFarmID().equals(farmID))
            return farm;
      }

      return null;
   }

   public void addFarm(FarmMonth farm) {
      numFarms++;

   }


}
