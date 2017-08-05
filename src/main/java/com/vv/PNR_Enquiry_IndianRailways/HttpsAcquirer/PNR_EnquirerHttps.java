package com.vv.PNR_Enquiry_IndianRailways.HttpsAcquirer;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.vv.PNR_Enquiry_IndianRailways.GUI.PNR_Form;
import com.vv.PNR_Enquiry_IndianRailways.LoggerFormatter;
import com.vv.PNR_Enquiry_IndianRailways.MainActivity;
import com.vv.PNR_Enquiry_IndianRailways.MapOfClassOfTravel;
import com.vv.PNR_Enquiry_IndianRailways.Model.Passenger;
import com.vv.PNR_Enquiry_IndianRailways.Model.PassengerList;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.vv.PNR_Enquiry_IndianRailways.MainActivity.smallLogoPath;

/**
 * This deals with the Https Acquirer for the pnr request...
 *
 * @author Vivek
 * @version 1.0
 * @since 01-08-2017
 */
public class PNR_EnquirerHttps {

    //private static Logger log = LoggerFactory.getLogger(PNR_EnquirerHttps.class); //slf4j one
    //public static boolean loopON = true;
    public PNR_EnquirerHttps(final String requestedPNR) {
        try {
            PNR_EnquirerHttpsRunner(requestedPNR);
        } catch (IOException e) {

        }
    }

    public void PNR_EnquirerHttpsRunner(final String requestedPNR) throws MalformedURLException, IOException {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PNR_EnquirerHttps.class.getName());
        new MapOfClassOfTravel();
        //initializing the map which stores the class and their corresponding description
        logger = LoggerFormatter.formatTheLoggerOutput(logger);
        //the above line should be written in each and every class where logging is to be used

        int responseCode = 404;

        HttpsURLConnection httpsURLConnection = null;
        URL url = new URL("https://www.railyatri.in/pnr-status/" + requestedPNR); //this https enabled site was allowing automated data extraction without giving any forbidden access response code
        logger.info("URL : " + url.toString());

        logger.info("Establishing connection now!");
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            //httpsURLConnection.setDoOutput(true);
            //httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.connect();
            //loopON = true;

            /*
            This entire block of statements were commented out as we are passing no parameter (i.e. the PNE) to this url based page
            PrintWriter pw = new PrintWriter(httpsURLConnection.getOutputStream());
            pw.write("lccp_pnrno1="+requestedPNR);
            //pw.write("fullname="+requestedPNR);

            //pw.write("pnr_no="+requestedPNR);
            //pw.write("pnrno="+requestedPNR);
            //pw.write("pnr=" + requestedPNR);
            pw.flush();
            pw.close();
            */
            responseCode = httpsURLConnection.getResponseCode();
            logger.info("Code received : " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                logger.info("Connection established successfully!!");
            }
        } catch (Exception e) {
            logger.info("Connection establishment failed... \nException : " + e);
            performEnablingFromHere();
        }

        logger.info("Establishing inflow path now, if possible...");
        try {
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                int lineNo = 1, numberOfPassengers = 0;
                String trainNumber = "", trainName = "", boardingStation = "", destinationStation = "", boardingDate = "", classOfTravel = "", chartStatus = "";
                String line;
                //StringBuilder passengerDataToBeProcessed = new StringBuilder();
                ArrayList<String> passengerDataToBeProcessed = new ArrayList<String>();
                //boolean foundEndingTagTable = false;
                while ((line = bufferedReader.readLine()) != null) {
                    //if(lineNo>=606 && lineNo<=692)
                    if (lineNo >= 606) {
                        logger.info("Line number " + lineNo + " --> " + line);
                        if (lineNo >= 670 && line.contains("</table>")) break;

                        //starting the data extraction from this line onwards
                        if (lineNo == 609) {
                            trainNumber = line.substring(0, line.indexOf('-')).trim();
                            trainName = line.substring(line.indexOf('-') + 1).trim();
                        }
                        if (lineNo == 626) boardingStation = line.trim();
                        if (lineNo == 635) destinationStation = line.trim();
                        if (lineNo == 642) boardingDate = line.trim();
                        if (lineNo == 648) classOfTravel = line.trim();
                        if (lineNo == 654) chartStatus = line.trim();
                        if (lineNo >= 672) {
                            passengerDataToBeProcessed.add(line.trim());
                            if (line.contains("</tr>")) numberOfPassengers++;
                        }
                    }
                    lineNo++;
                }
                bufferedReader.close();
                httpsURLConnection.disconnect();
                //loopON = false;
                logger.info("The extracted details:-");
                logger.info("Train number : " + trainNumber);
                logger.info("Train name : " + trainName);
                logger.info("Boarding station : " + boardingStation);
                logger.info("Destination station : " + destinationStation);
                logger.info("Boarding date : " + boardingDate);
                logger.info("Class : " + classOfTravel);
                logger.info("Chart status : " + chartStatus);
                //logger.info("Data to be processed :- \n"+passengerDataToBeProcessed);
                logger.info("The number of passengers in this ticket : " + numberOfPassengers);

                //starting the passenger data processing if everything is working properly
                List<Passenger> listOfPassengers = new ArrayList<Passenger>();
                //performEnablingFromHere();
                if (numberOfPassengers != 0) {
                    int localPointer = 0;
                    for (; localPointer < passengerDataToBeProcessed.size(); localPointer += 22) {
                        //logger.info(passengerDataToBeProcessed.get(localPointer));
                        Passenger passenger = new Passenger();
                        passenger.setNumber(Integer.parseInt(passengerDataToBeProcessed.get(localPointer)));
                        passenger.setBookingStatus(passengerDataToBeProcessed.get(localPointer + 3).trim());
                        passenger.setCurrentStatus(passengerDataToBeProcessed.get(localPointer + 6).trim());
                        listOfPassengers.add(passenger);
                    }
                    for (Passenger passenger : listOfPassengers) {
                        logger.info(passenger.getNumber() + " :: " + passenger.getBookingStatus() + " :: " + passenger.getCurrentStatus());
                    }
                    final PassengerList passengerList = new PassengerList();
                    passengerList.setListOfPassengers(listOfPassengers);
                    //call the function for the pnr form from here
                    final String finalTrainNumber = trainNumber;
                    final String finalTrainName = trainName;
                    final String finalChartStatus = chartStatus;
                    final String finalClassOfTravel = classOfTravel;
                    final String finalBoardingDate = boardingDate;
                    final String finalDestinationStation = destinationStation;
                    final String finalBoardingStation = boardingStation;
                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JFrame frame = new JFrame("PNR sample product");
                                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    frame.setLocationRelativeTo(null);
                                    frame.getContentPane().add(new PNR_Form(requestedPNR, finalTrainNumber, finalTrainName, finalBoardingStation, finalDestinationStation, finalBoardingDate, finalClassOfTravel, finalChartStatus, passengerList).getUI());

                                    // Create and set up the content pane.
                                    // Display the window.
                                    frame.pack();
                                    frame.setResizable(false);
                                    frame.setVisible(true);
                                }
                            });
                        }
                    }).start();
                    */

                    final java.util.logging.Logger finalLogger = logger;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            JFrame frame = new JFrame("PNR based ticket details");
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setLocationRelativeTo(null);
                            frame.getContentPane().add(new PNR_Form(requestedPNR, finalTrainNumber, finalTrainName, finalBoardingStation, finalDestinationStation, finalBoardingDate, finalClassOfTravel, finalChartStatus, passengerList).getUI());

                            // Create and set up the content pane.
                            // Display the window.
                            frame.pack();
                            try {
                                frame.setIconImage(new ImageIcon(Toolkit.getDefaultToolkit().createImage(MainActivity.class.getResource(smallLogoPath))).getImage());
                            } catch (NullPointerException npe1) {
                                finalLogger.info("ERROR in loading the image for the frame... NUll pointer exception occurred!");
                            }
                            frame.setResizable(false);
                            frame.setVisible(true);
                        }
                    });
                } else {
                    logger.info("No passengers, so no processing of their data!");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "PNR record doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                logger.info("No reading of webpage as connection not ok!");
                logger.info("Connection response code : "+responseCode);
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "No Internet Connectivity or the server is down", "Error", JOptionPane.ERROR_MESSAGE);
            }
            httpsURLConnection.disconnect();
            //loopON = false;
            performEnablingFromHere();
        } catch (Exception e) {
            logger.info("Exception in the reading part... \n\tException : " + e);
            performEnablingFromHere();
            //loopON = false;
        }
    }

    public static void performEnablingFromHere() {
        MainActivity.disableAll = false;
        MainActivity.performEnabling();
    }

    /*
    public static void main(String[] args) throws IOException {
        new MapOfClassOfTravel();
        //initializing the map which stores the class and their corresponding description
        logger = LoggerFormatter.formatTheLoggerOutput(logger);
        //the above line should be written in each and every class where logging is to be used

        int responseCode = 404;

        HttpsURLConnection httpsURLConnection = null;
        URL url = new URL("https://www.railyatri.in/pnr-status/" + requestedPNR); //this https enabled site was allowing automated data extraction without giving any forbidden access response code
        logger.info("URL : " + url.toString());

        logger.info("Establishing connection now!");
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            //httpsURLConnection.setDoOutput(true);
            //httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.connect();

            /*
            This entire block of statements were commented out as we are passing no parameter (i.e. the PNE) to this url based page
            PrintWriter pw = new PrintWriter(httpsURLConnection.getOutputStream());
            pw.write("lccp_pnrno1="+requestedPNR);
            //pw.write("fullname="+requestedPNR);

            //pw.write("pnr_no="+requestedPNR);
            //pw.write("pnrno="+requestedPNR);
            //pw.write("pnr=" + requestedPNR);
            pw.flush();
            pw.close();
            responseCode = httpsURLConnection.getResponseCode();
            logger.info("Code received : " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                logger.info("Connection established successfully!!");
            }
        } catch (Exception e) {
            logger.info("Connection establishment failed... \nException : " + e);
        }

        logger.info("Establishing inflow path now, if possible...");
        try {
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                int lineNo = 1, numberOfPassengers = 0;
                String trainNumber = "", trainName = "", boardingStation = "", destinationStation = "", boardingDate = "", classOfTravel = "", chartStatus = "";
                String line;
                //StringBuilder passengerDataToBeProcessed = new StringBuilder();
                ArrayList<String> passengerDataToBeProcessed = new ArrayList<String>();
                //boolean foundEndingTagTable = false;
                while ((line = bufferedReader.readLine()) != null) {
                    //if(lineNo>=606 && lineNo<=692)
                    if (lineNo >= 606) {
                        logger.info("Line number " + lineNo + " --> " + line);
                        if (lineNo >= 670 && line.contains("</table>")) break;

                        //starting the data extraction from this line onwards
                        if (lineNo == 609) {
                            trainNumber = line.substring(0, line.indexOf('-')).trim();
                            trainName = line.substring(line.indexOf('-') + 1).trim();
                        }
                        if (lineNo == 626) boardingStation = line.trim();
                        if (lineNo == 635) destinationStation = line.trim();
                        if (lineNo == 642) boardingDate = line.trim();
                        if (lineNo == 648) classOfTravel = line.trim();
                        if (lineNo == 654) chartStatus = line.trim();
                        if (lineNo >= 672) {
                            passengerDataToBeProcessed.add(line.trim());
                            if (line.contains("</tr>")) numberOfPassengers++;
                        }
                    }
                    lineNo++;
                }
                bufferedReader.close();
                logger.info("The extracted details:-");
                logger.info("Train number : " + trainNumber);
                logger.info("Train name : " + trainName);
                logger.info("Boarding station : " + boardingStation);
                logger.info("Destination station : " + destinationStation);
                logger.info("Boarding date : " + boardingDate);
                logger.info("Class : " + classOfTravel);
                logger.info("Chart status : " + chartStatus);
                //logger.info("Data to be processed :- \n"+passengerDataToBeProcessed);
                logger.info("The number of passengers in this ticket : " + numberOfPassengers);

                //starting the passenger data processing if everything is working properly
                List<Passenger> listOfPassengers = new ArrayList<Passenger>();
                if (numberOfPassengers != 0) {
                    int localPointer = 0;
                    for (; localPointer < passengerDataToBeProcessed.size(); localPointer += 22) {
                        //logger.info(passengerDataToBeProcessed.get(localPointer));
                        Passenger passenger = new Passenger();
                        passenger.setNumber(Integer.parseInt(passengerDataToBeProcessed.get(localPointer)));
                        passenger.setBookingStatus(passengerDataToBeProcessed.get(localPointer + 3).trim());
                        passenger.setCurrentStatus(passengerDataToBeProcessed.get(localPointer + 6).trim());
                        listOfPassengers.add(passenger);
                    }
                    for (Passenger passenger : listOfPassengers) {
                        logger.info(passenger.getNumber() + " :: " + passenger.getBookingStatus() + " :: " + passenger.getCurrentStatus());
                    }
                    final PassengerList passengerList = new PassengerList();
                    passengerList.setListOfPassengers(listOfPassengers);
                    //call the function for the pnr form from here
                    final String finalTrainNumber = trainNumber;
                    final String finalTrainName = trainName;
                    final String finalChartStatus = chartStatus;
                    final String finalClassOfTravel = classOfTravel;
                    final String finalBoardingDate = boardingDate;
                    final String finalDestinationStation = destinationStation;
                    final String finalBoardingStation = boardingStation;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JFrame frame = new JFrame("PNR sample product");
                                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    frame.setLocationRelativeTo(null);
                                    frame.getContentPane().add(new PNR_Form(requestedPNR, finalTrainNumber, finalTrainName, finalBoardingStation, finalDestinationStation, finalBoardingDate, finalClassOfTravel, finalChartStatus, passengerList).getUI());

                                    // Create and set up the content pane.
                                    // Display the window.
                                    frame.pack();
                                    frame.setResizable(false);
                                    frame.setVisible(true);
                                }
                            });
                        }
                    }).start();
                } else {
                    logger.info("No passengers, so no processing of their data!");
                }
            } else {
                logger.info("No reading of webpage as connection not ok!");
            }
        } catch (Exception e) {
            logger.info("Exception in the reading part... \n\tException : " + e);
        }
    }
    */
}