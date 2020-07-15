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
import java.time.LocalDate;
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

   private ObservableList<YearData> yearList; // list of years
   private Stage mainStage;
   private File inputFile; // a file input into the ds
   private Font header; // font used for header text
   private TableView<DayData> table;
   private double fieldWidth; // used to size TextFields
   private double comboWidth; // used to size ComboBoxes


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
    * Creates the pane for the Home tab. This pane contains all the information
    * and controls for that tab.
    * 
    * @return The pane for the home tab.
    */
   private Pane createHomePane() {
      BorderPane homePane = new BorderPane();

      // the text to display on the home tab
      String homeString = "Welcome to the Milk Weights GUI!  Please select a "
            + "tab to access milk weight data.\n - Tables displays a data table"
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
         alert.setHeaderText("Problem Loading");
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
    * Creates the table to be displayed on the Tables tab. Suppresses a warning
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
    * Creates the pane for the Tables tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Tables tab.
    */
   private Pane createTablesPane() {
      GridPane topOptions = new GridPane();

      // creates labels for options
      Label farmLabel = new Label("Farm ID:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // creates drop down boxes for options
      ComboBox<String> farms = new ComboBox<>();
      farms.setPrefWidth(comboWidth);
      farms.setEditable(true);
      farms.getItems().addAll("farm 1", "farm 2");

      ComboBox<String> years = new ComboBox<>();
      years.setPrefWidth(comboWidth);
      years.setEditable(true);
      years.getItems().addAll("2019", "2020");

      ComboBox<String> months = new ComboBox<>();
      months.setPrefWidth(comboWidth);
      months.setEditable(true);
      months.getItems().addAll("jan", "feb");

      Button downloadButton = new Button("Download Report");

      // adding options to top
      topOptions.addRow(0, farmLabel, yearLabel, monthLabel);
      topOptions.addRow(1, farms, years, months, downloadButton);

      // formatting grid
      topOptions.setHgap(20);
      topOptions.setPadding(new Insets(0, 0, 5, 0));
      topOptions.setAlignment(Pos.CENTER);

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
      HBox bottomAdd =
            new HBox(addLabel, dateField, farmField, weightField, enterButton);
      bottomAdd.setSpacing(10);
      bottomAdd.setPadding(new Insets(5, 0, 0, 0));
      bottomAdd.setAlignment(Pos.CENTER);

      BorderPane tablesPane =
            new BorderPane(createTable(), topOptions, null, bottomAdd, null);
      tablesPane.setPadding(new Insets(10, 15, 10, 10));

      return tablesPane;
   }


   /**
    * Determines how to add the data from a new file.
    * 
    * @param file The file to get data from.
    */
   private void addFile(File file) {
      String name = file.getName();
      name = name.substring(0, name.lastIndexOf(".")); // removes .csv

      String[] yearMonth = name.split("-");
      int year = Integer.parseInt(yearMonth[0]);

      // determines if it should add on or create a new year
      if (yearList == null) {
         addYear(file, yearMonth);

      } else {
         YearData yearData = getYear(year);

         if (yearData == null) {
            addYear(file, yearMonth);

         } else {
            yearData.addMonthData(file, yearMonth[1]);
         }
      }

      // adds every day of data to the table
      for (YearData yearData : yearList) {
         for (MonthData monthData : yearData.getMonthList()) {
            for (DayData dayData : monthData.getDayList()) {
               table.getItems().add(dayData);
            }
         }
      }
   }


   /**
    * Creates the pane for the Input Files tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Input Files tab.
    */
   private Pane createInputFilesPane() {
      GridPane pane = new GridPane();

      // creates a FileChooser for selecting a file with the OS's file manager
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
            alert.setContentText("Saving a new file may overwrite previous data"
                  + ".  Would you still like to continue?");
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
               fileField.setText("Canceled Save");
               inputFile = null;
            }

            if (inputFile != null) {
               addFile(inputFile);
               inputFile = null;
               fileField.setText("File Saved");
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
      GridPane reportsPane = new GridPane();

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

      // creating and formatting all ComboBoxes
      ComboBox<String> farms = new ComboBox<>();
      farms.setEditable(true);
      farms.setPrefWidth(comboWidth);
      farms.getItems().addAll("farm 1", "farm 2");

      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.setPrefWidth(comboWidth);
      years.getItems().addAll("2019", "2020");

      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.setPrefWidth(comboWidth);
      months.getItems().addAll("jan", "feb");

      // (temporary) handles when an item is selected in a ComboBox
      farms.setOnAction(a -> {
         String value = farms.getValue();
         farmTotalField.setText(value + " total");
         farmPercentField.setText(value + " weight%");
      });
      years.setOnAction(a -> {
         String value = years.getValue();
         yearTotalField.setText(value + " total");
         yearPercentField.setText(value + " weight%");
      });
      months.setOnAction(a -> {
         String value = months.getValue();
         monthTotalField.setText(value + " total");
         monthPercentField.setText(value + " weight%");
      });

      // adding controls to GridPane
      reportsPane.addRow(0, reportLabel, selectLabel, totalLabel, percentLabel);
      reportsPane.addRow(1, farmLabel, farms, farmTotalField, farmPercentField);
      reportsPane.addRow(2, yearLabel, years, yearTotalField, yearPercentField);
      reportsPane.addRow(3, monthLabel, months, monthTotalField,
            monthPercentField);

      // formatting GridPane
      GridPane.setHalignment(selectLabel, HPos.CENTER);
      GridPane.setHalignment(totalLabel, HPos.CENTER);
      GridPane.setHalignment(percentLabel, HPos.CENTER);
      reportsPane.setPadding(new Insets(40, 10, 10, 10));
      reportsPane.setHgap(15);
      reportsPane.setVgap(10);
      reportsPane.setAlignment(Pos.TOP_CENTER);

      return reportsPane;
   }


   /**
    * Creates the GUI and displays it to the user.
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
      Tab tablesTab = new Tab("  Tables  ");
      Tab inputFilesTab = new Tab("Input Files");
      Tab reportsTab = new Tab("  Reports  ");

      // adds the panes to each tab
      homeTab.setContent(createHomePane());
      tablesTab.setContent(createTablesPane());
      inputFilesTab.setContent(createInputFilesPane());
      reportsTab.setContent(createReportsPane());

      // adds the individual tabs to the TabPane
      TabPane tabPane =
            new TabPane(homeTab, tablesTab, inputFilesTab, reportsTab);
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
