TITLE: Global Consulting Scheduling Application
AUTHOR: Jamie Huang
CONTACT: jhuan19@wgu.edu
APPLICATION VERSION: 1.0
DATE: 10/14/2020
BUILT IN: IntelliJ IDEA 2019.3.4 (Community Edition)

LIBRARIES USED:
Java
    Java SE 11.0.9
	Lib: JavaFX-SDK-11.0.2
MySQL
	Maven: mysql:mysql-connector-java:8.0.21

PURPOSE: Allows users to track and manage appointments with contacts and customers across a variety of office locations and time zones, with appointment alerts.

This application also allows user to run reports on the scheduled appointments, such as:
 - total number of appointments scheduled per month
 - total number of appointments scheduled per type of meeting
 - total number of appointments scheduled per unique office location (a custom/additional report specially added to this project)
 - all schedules filterable by contact, to view all contact schedules



HOW TO RUN:
This project was created, run, and tested in IntelliJ.

In order to run, go to File > Project Structure. Make sure Java 11 SDK is added in Platform SDKs > SDKs. In Project Settings > Project, make sure the Java 11 SDK is selected in Project SDK dropdown setting.
In Project Settings > Libraries, make sure the JavaFX SDK is added as a Java library 'lib' with where the sdk library is stored on your local system.

In the run configuration, make sure these modules are set up in VM settings and the working directory/JRE are appropriately set for your local system.

VM setting example:
--module-path "C:\Program Files\Java\javafx-sdk-11.0.2/lib" --add-modules javafx.controls,javafx.fxml

Once everything is set up, you can run the program in IntelliJ.

To generate/open the Javadoc documentation, you can go to Tools > Generate Javadoc in IntelliJ. If Javadoc does not open automatically, you can open the index.html in the javadoc folder in a browser manually.

You can log into the application for testing with test user credentials:
username: test
password: test