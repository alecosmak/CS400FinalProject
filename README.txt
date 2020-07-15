README

Course: CS400
Semester: Summer 2020
Project name: Milk Weights Final Project
Student Name: Alec Osmak

Email: osmak@wisc.edu

Other notes or comments to the grader:
	You can add any amount of CSV files on the "Input Files" tab as long as they are formatted correctly, the code can not handle errors in CSV files. To add a CSV file, click the "Select File" button, browse through your file manager and open the file, click the "Save" button, and click the "Yes" button on the popup. The data from the files will be added to the table on the "Tables" tab and the table can be sorted by clicking the column headers. If two of the same dates are entered it will show duplicates. The controls on the "Tables" and "Reports" tabs do not do anything yet so they have placeholder options. There is a sample CSV file that can be loaded and a JPG image used on the home page.

Changes from a1:
 - I am no longer going to create charts to represent data
 - Got rid of enum Days
 - I am using ObservableList<> instead ArrayList<> for my data structure
 - I got rid of the FarmMonth class and am instead using a DayData class to store data