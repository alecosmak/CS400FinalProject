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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Runs the program. Creates the GUI and shows it to the user. Is able to read
 * CSV files and gather the data from them.
 * 
 * @author Alec Osmak
 */
public class RunFinalProject extends Application {

   private Stage mainStage;
   private TableView<DayData> table; // the table displayed
   private ObservableList<YearData> yearList; // list of years
   private File inputFile; // a file input into the ds
   private Font header; // font used for header text
   private double fieldWidth; // used to size TextFields
   private double comboWidth; // used to size ComboBoxes
   private TextField totalTableField;
   private TextField percentTableField;
   private Label percentTableLabel;
   // combo boxes that are updated with data
   private ComboBox<String> tableFarms;
   private ComboBox<String> tableYears;
   private ComboBox<String> tableMonths;
   private ComboBox<String> reportFarms;
   private ComboBox<String> reportYears;
   private ComboBox<String> reportMonths;


   /**
    * Finds and returns a year of data from its integer value.
    * 
    * @param year The year to get.
    * @return The year of data found, or null if it doesn't exist.
    */
   private YearData getYear(int year) {
      for (YearData yearData : yearList) {
         if (yearData.getYear() == year)
            return yearData;
      }

      return null;
   }


   /**
    * Adds a year to the list of years from a file of a month in that year.
    * 
    * @param file      The data for a month in a new year.
    * @param yearMonth The year and month the file is.
    */
   private void addYear(File file, String[] yearMonth) {
      if (yearList == null)
         yearList = FXCollections.observableArrayList();

      yearList.add(new YearData(file, yearMonth));
   }


   /**
    * Adds the data to the table depending on what the user wants to be shown.
    * What is shown is determined from the controls above the table.
    */
   private void displayTableData() {
      table.getItems().clear();

      if (yearList != null) {
         ArrayList<DayData> tableData = new ArrayList<>(); // stores all data

         for (YearData yearData : yearList) {
            for (MonthData monthData : yearData.getMonthList()) {
               for (DayData dayData : monthData.getDayList()) {
                  tableData.add(dayData); // adds all data to tableData
               }
            }
         }

         // list of data to remove from tableData
         ArrayList<DayData> removeList = new ArrayList<>();

         if (tableFarms.getValue() != null) {
            for (int i = 0; i < tableData.size(); i++) { // removes by farm id
               DayData dayData = tableData.get(i);

               if (!dayData.getFarmID().equals(tableFarms.getValue()))
                  removeList.add(dayData);
            }
         }

         if (tableYears.getValue() != null) {
            for (int i = 0; i < tableData.size(); i++) { // removes by year
               DayData dayData = tableData.get(i);
               int year = dayData.getDate().getYear();

               if (year != Integer.valueOf(tableYears.getValue()))
                  removeList.add(dayData);
            }
         }

         if (tableMonths.getValue() != null) {
            String value = tableMonths.getValue();
            Months month = Months.valueOf(value);

            for (int i = 0; i < tableData.size(); i++) { // removes by month
               DayData dayData = tableData.get(i);
               int dateMonth = dayData.getDate().getMonthValue();

               if (month != Months.values()[dateMonth - 1])
                  removeList.add(dayData);
            }
         }

         for (DayData dayData : removeList)
            tableData.remove(dayData); // removes data from tableData

         for (DayData dayData : tableData)
            table.getItems().add(dayData); // adds remaining data to table
      }

      updateTableTotalWeight();
   }


   /**
    * Updates the combo boxes to have all options available from the data.
    */
   private void updateComboBoxes() {
      // resets combo boxes to be empty
      tableFarms.getItems().clear();
      tableYears.getItems().clear();
      tableMonths.getItems().clear();
      reportFarms.getItems().clear();
      reportYears.getItems().clear();
      reportMonths.getItems().clear();

      // adds an any option
      tableFarms.getItems().add(null);
      tableYears.getItems().add(null);
      tableMonths.getItems().add(null);

      // adds all years to the year combo boxes
      for (YearData year : yearList) {
         if (!tableYears.getItems().contains(String.valueOf(year.getYear()))) {
            tableYears.getItems().add(String.valueOf(year.getYear()));
            reportYears.getItems().add(String.valueOf(year.getYear()));
         }
      }

      // adds all months to the month combo boxes
      for (YearData year : yearList) {
         for (MonthData month : year.getMonthList()) {
            if (!tableMonths.getItems()
                  .contains(String.valueOf(month.getMonth()))) {
               tableMonths.getItems().add(String.valueOf(month.getMonth()));
               reportMonths.getItems().add(String.valueOf(month.getMonth()));
            }

         }
      }

      // adds all farm id's to the farm id combo boxes
      for (YearData year : yearList) {
         for (MonthData month : year.getMonthList()) {
            for (DayData day : month.getDayList()) {
               if (!tableFarms.getItems().contains(day.getFarmID())) {
                  tableFarms.getItems().add(day.getFarmID());
                  reportFarms.getItems().add(day.getFarmID());
               }
            }
         }
      }
   }


   /**
    * Creates and formats the combo boxes used in this GUI.
    */
   private void createComboBoxes() {
      // combo boxes for table tab
      tableFarms = new ComboBox<>();
      tableFarms.setPrefWidth(comboWidth);
      tableFarms.setOnAction(a -> displayTableData());

      tableYears = new ComboBox<>();
      tableYears.setPrefWidth(comboWidth);
      tableYears.setOnAction(a -> displayTableData());

      tableMonths = new ComboBox<>();
      tableMonths.setPrefWidth(comboWidth);
      tableMonths.setOnAction(a -> displayTableData());

      // combo boxes for reports tab
      reportFarms = new ComboBox<>();
      reportFarms.setPrefWidth(comboWidth);

      reportYears = new ComboBox<>();
      reportYears.setPrefWidth(comboWidth);

      reportMonths = new ComboBox<>();
      reportMonths.setPrefWidth(comboWidth);
   }


   /**
    * Updates the sum of the weights that the table currently has displayed.
    */
   private void updateTableTotalWeight() {
      int tableTotal = 0;
      for (DayData day : table.getItems())
         tableTotal += day.getWeight();

      totalTableField.setText(String.valueOf(tableTotal));

      int dataTotal = 0;
      for (YearData year : yearList)
         dataTotal += year.getTotalYearWeight();

      double percent = 100.0;

      percent = ((double) tableTotal / dataTotal) * 100;

      DecimalFormat df = new DecimalFormat("###.##"); // 2 decimal digits
      percentTableField.setText(df.format(percent) + "%");
   }


   /**
    * Downloads all available data to a single CSV file.
    * 
    * @param fileName The name of the new CSV file.
    */
   private void downloadData(String fileName) {
      if (!fileName.endsWith(".csv")) // makes sure the file name ends in .csv
         fileName = fileName.concat(".csv");

      try {
         File file = new File(fileName);
         PrintWriter writer = new PrintWriter(file);
         
         // adds all data to the file
         writer.println("date,farm_id,weight");
         for (YearData year : yearList) {
            for (MonthData month : year.getMonthList()) {
               for (DayData day : month.getDayList()) {
                  writer.print(day.getDate() + ",");
                  writer.print(day.getFarmID() + ",");
                  writer.println(day.getWeight());
               }
            }
         }

         writer.flush();
         writer.close();

      } catch (Exception e) { // shows an error dialog for bad file name
         Alert alert = new Alert(AlertType.ERROR, "File name not allowed. File"
               + " creation canceled.");
         alert.setHeaderText("Error With File Name");
         alert.show();
      }
   }


   /**
    * Creates the pane for the Home tab. This pane contains all the information
    * and controls for that tab.
    * 
    * @return The pane for the home tab.
    */
   private Pane createHomePane() {
      BorderPane homePane = new BorderPane();

      // the text to display on the home tab
      String homeString = "Welcome to the Milk Weights GUI!  Please select a "
            + "tab to access milk weight data.\n - Table displays a data table"
            + " of all current data\n - Input Files allows you to add more data"
            + " from a CSV file\n - Reports shows general weight summaries";

      // adds and formats the text for the pane
      Text homeText = new Text(homeString);
      homeText.setFont(new Font(15));
      homePane.setCenter(homeText);
      BorderPane.setAlignment(homeText, Pos.CENTER);

      // vbox for the bottom elements
      VBox bottom = new VBox();
      bottom.setAlignment(Pos.CENTER);
      bottom.setPadding(new Insets(0, 0, 2, 0));


      try { // tries to load in an image to the home tab
         FileInputStream inputImage = new FileInputStream("cheeseLogo.jpg");
         Image image =
               new Image(inputImage, Region.USE_PREF_SIZE, 173, true, true);
         ImageView imageView = new ImageView(image);
         bottom.getChildren().add(imageView);

      } catch (FileNotFoundException e) {
         // popup window for if the home page image cannot be found
         Alert alert = new Alert(AlertType.INFORMATION,
               "Home image could not be found. Everything else will still "
                     + "function correctly.");
         alert.setHeaderText("Problem Loading Image");
         alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
               Region.USE_PREF_SIZE);
         alert.showAndWait();
      }

      Text infoText = new Text("Designed by Alec Osmak for a final project.  "
            + "UW-Madison CS400 Summer 2020");
      bottom.getChildren().add(infoText);
      homePane.setBottom(bottom);

      return homePane;
   }


   /**
    * Creates the table to be displayed on the table tab. Suppresses a warning
    * because it does not like having columns of Strings and Integers.
    * 
    * @return A table that can be displayed.
    */
   @SuppressWarnings("unchecked")
   private TableView<DayData> createTable() {
      table = new TableView<>();

      // creates the columns for the table
      TableColumn<DayData, LocalDate> dateCol = new TableColumn<>("Date");
      dateCol.setPrefWidth(comboWidth);
      TableColumn<DayData, String> farmCol = new TableColumn<>("Farm ID");
      farmCol.setPrefWidth(comboWidth);
      TableColumn<DayData, Integer> weightCol = new TableColumn<>("Weight");
      weightCol.setPrefWidth(comboWidth);

      // gets the data for each columns cell from the DayData class
      dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
      farmCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));
      weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

      table.getColumns().addAll(dateCol, farmCol, weightCol);
      table.setMaxWidth(395);

      return table;
   }


   /**
    * Creates the pane for the table tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the table tab.
    */
   private Pane createTablePane() {
      GridPane topOptions = new GridPane();

      // creates labels for options
      Label farmLabel = new Label("Farm:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // button to download all available data
      Button downloadButton = new Button("Download All Data");
      downloadButton.setOnAction(a -> { // creates dialog for file name input
         if (yearList != null) {
            TextInputDialog inputDialog = new TextInputDialog("fileName");
            inputDialog.setTitle("Enter File Name");
            inputDialog.setHeaderText("Please enter name of new .csv file ("
                  + "exclude file extension).");
            inputDialog.getEditor().setMaxWidth(150);

            Optional<String> result = inputDialog.showAndWait();
            if (result.isPresent()) // downloads data
               downloadData(result.get());
         }
      });

      // adding options and formatting grid
      topOptions.addRow(0, farmLabel, yearLabel, monthLabel);
      topOptions.addRow(1, tableFarms, tableYears, tableMonths, downloadButton);
      topOptions.setHgap(20);
      topOptions.setPadding(new Insets(0, 0, 5, 0));
      topOptions.setAlignment(Pos.CENTER);

      // total weight section
      Label totalLabel = new Label("Total Weight:");
      totalTableField = new TextField("0");
      totalTableField.setPrefWidth(75);
      percentTableLabel = new Label("Pecent of Total:");
      percentTableField = new TextField("0.0%");
      percentTableField.setPrefWidth(75);
      HBox totalWeight = new HBox(10, totalLabel, totalTableField,
            percentTableLabel, percentTableField);
      totalWeight.setAlignment(Pos.CENTER_RIGHT);
      totalWeight.setPadding(new Insets(5, 80, 5, 0));

      // label and fields for bottom options
      Label addLabel = new Label("Add day:");
      TextField dateField = new TextField();
      dateField.setPrefWidth(comboWidth);
      dateField.setPromptText("Date");
      TextField farmField = new TextField();
      farmField.setPromptText("Farm ID");
      farmField.setPrefWidth(comboWidth);
      TextField weightField = new TextField();
      weightField.setPromptText("Weight");
      weightField.setPrefWidth(comboWidth);

      // button to enter day into ds
      Button enterButton = new Button("Enter");
      enterButton.setOnAction(a -> {
         dateField.clear();
         farmField.clear();
         weightField.clear();
      });

      // creating and formatting bottom options
      HBox bottomAdd = new HBox(10, addLabel, dateField, farmField, weightField,
            enterButton);
      bottomAdd.setAlignment(Pos.CENTER);

      // adding to table tab pane
      VBox tablePane =
            new VBox(topOptions, createTable(), totalWeight, bottomAdd);
      tablePane.setPadding(new Insets(10, 10, 10, 10));
      tablePane.setAlignment(Pos.CENTER);

      return tablePane;
   }


   /**
    * Determines how to add the data from a new file.
    * 
    * @param file The file to get data from.
    */
   private void addFile(File file) {
      String name = file.getName();
      name = name.substring(0, name.lastIndexOf(".")); // removes ".csv"

      String[] yearMonth = name.split("-"); // splits date
      int year = Integer.parseInt(yearMonth[0]);

      // determines if it should add or create a year
      if (yearList == null) { // no data yet
         addYear(file, yearMonth);

      } else {
         YearData yearData = getYear(year);

         if (yearData == null) { // new year
            addYear(file, yearMonth);

         } else { // add to existing year
            yearData.addMonthData(file, yearMonth[1]);
         }
      }

      updateComboBoxes();
   }


   /**
    * Creates the pane for the Input Files tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Input Files tab.
    */
   private Pane createInputFilesPane() {
      GridPane pane = new GridPane();

      // creates a FileChooser for selecting a file in the OS file manager
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select CSV File");
      fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

      Label fileLabel = new Label("Selected File:");
      fileLabel.setFont(header);
      TextField fileField = new TextField();
      fileField.setPrefWidth(fieldWidth);
      fileField.setEditable(false);

      // a button that opens the FileChooser dialog
      Button fileButton = new Button("Select File");
      fileButton.setOnAction(a -> {
         inputFile = fileChooser.showOpenDialog(mainStage);
         if (inputFile != null) {
            fileField.setText(inputFile.getName()); // displays file name
         }
      });

      Button saveButton = new Button("Save File");

      // creates confirmation popup when saving file
      saveButton.setOnAction(a -> {
         if (inputFile != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);

            // formatting popup
            alert.setHeaderText("Save File Confirmation");
            alert.setContentText("Saving a new file will overwrite previous "
                  + "data if the year and month are the same. Would you still "
                  + "like to continue?");
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);

            // sets buttons on popup
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            // handles when a button is pressed
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == noButton) {
               alert.close();
               fileField.setText("Save Canceled");
               inputFile = null;
            }

            if (inputFile != null) {
               addFile(inputFile);
               inputFile = null;
               fileField.setText("File Saved");
               displayTableData();
            }
         }
      });

      // formatting GridPane
      pane.add(fileButton, 0, 0);
      pane.addRow(1, fileLabel, fileField, saveButton);
      pane.setHgap(10);
      pane.setVgap(10);
      pane.setPadding(new Insets(40, 10, 10, 10));
      pane.setAlignment(Pos.TOP_CENTER);

      return pane;
   }


   /**
    * Creates the pane for the Reports tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Reports tab.
    */
   private Pane createReportsPane() {
      VBox reportBox = new VBox();
      GridPane grid = new GridPane();

      // creating and formatting all Labels
      Label reportLabel = new Label("Report");
      reportLabel.setFont(header);
      Label selectLabel = new Label("Select");
      selectLabel.setFont(header);
      Label totalLabel = new Label("Total Weight");
      totalLabel.setFont(header);
      Label percentLabel = new Label("Percent of total");
      percentLabel.setFont(header);

      Label farmLabel = new Label("Farm:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // creating and formatting all TextFields
      TextField farmTotalField = new TextField();
      farmTotalField.setEditable(false);
      farmTotalField.setPrefWidth(fieldWidth);
      TextField farmPercentField = new TextField();
      farmPercentField.setEditable(false);
      farmPercentField.setPrefWidth(fieldWidth);

      TextField yearTotalField = new TextField();
      yearTotalField.setEditable(false);
      yearTotalField.setPrefWidth(fieldWidth);
      TextField yearPercentField = new TextField();
      yearPercentField.setEditable(false);
      yearPercentField.setPrefWidth(fieldWidth);

      TextField monthTotalField = new TextField();
      monthTotalField.setPrefWidth(fieldWidth);
      monthTotalField.setEditable(false);
      TextField monthPercentField = new TextField();
      monthPercentField.setEditable(false);
      monthPercentField.setPrefWidth(fieldWidth);

      // adding controls to GridPane
      grid.addRow(0, reportLabel, selectLabel, totalLabel, percentLabel);
      grid.addRow(1, farmLabel, reportFarms, farmTotalField, farmPercentField);
      grid.addRow(2, yearLabel, reportYears, yearTotalField, yearPercentField);
      grid.addRow(3, monthLabel, reportMonths, monthTotalField,
            monthPercentField);

      // formatting GridPane
      GridPane.setHalignment(selectLabel, HPos.CENTER);
      GridPane.setHalignment(totalLabel, HPos.CENTER);
      GridPane.setHalignment(percentLabel, HPos.CENTER);
      grid.setHgap(15);
      grid.setVgap(10);

      Label range = new Label("Select date range");
      range.setFont(header);

      grid.setAlignment(Pos.TOP_CENTER);

      reportBox.getChildren().addAll(grid, range);
      reportBox.setSpacing(40);
      reportBox.setPadding(new Insets(40, 10, 10, 10));
      reportBox.setAlignment(Pos.TOP_CENTER);

      return reportBox;
   }


   /**
    * Creates the tab based GUI and displays it to the user.
    * 
    * @param pStage The primary stage to display the GUI window.
    */
   @Override
   public void start(Stage mainStage) throws Exception {
      this.mainStage = mainStage;
      header = new Font(14);
      fieldWidth = 100;
      comboWidth = 125;

      // creates all the tabs
      Tab homeTab = new Tab("   Home   ");
      Tab tableTab = new Tab("   Table   ");
      Tab inputFilesTab = new Tab("Input Files");
      Tab reportsTab = new Tab("  Reports  ");

      createComboBoxes();

      // adds the panes to each tab
      homeTab.setContent(createHomePane());
      tableTab.setContent(createTablePane());
      inputFilesTab.setContent(createInputFilesPane());
      reportsTab.setContent(createReportsPane());

      // adds the individual tabs to the TabPane
      TabPane tabPane =
            new TabPane(homeTab, tableTab, inputFilesTab, reportsTab);
      tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      // formats the stage and shows it to the user
      Scene scene = new Scene(tabPane);
      mainStage.setScene(scene);
      mainStage.setTitle("Milk Weights GUI");
      mainStage.show();
   }


   /**
    * Starts the program.
    * 
    * @param args The general arguments when running the program.
    */
   public static void main(String[] args) {
      launch(args);
   }


}
