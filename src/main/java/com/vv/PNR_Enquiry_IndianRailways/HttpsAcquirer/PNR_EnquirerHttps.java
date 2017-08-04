package com.vv.PNR_Enquiry_IndianRailways.HttpsAcquirer;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.vv.PNR_Enquiry_IndianRailways.LoggerFormatter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This deals with the Https Acquirer for the pnr request...
 *
 * @author Vivek
 * @version 1.0
 * @since 01-08-2017
 */
public class PNR_EnquirerHttps {

    private final static String pnr1 = "8417675523";
    //private static Logger log = LoggerFactory.getLogger(PNR_EnquirerHttps.class); //slf4j one
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PNR_EnquirerHttps.class.getName());

    public static void main(String[] args) throws IOException {
        logger = LoggerFormatter.formatTheLoggerOutput(logger);
        //the above line should be written in each and every class where logging is to be used

        int responseCode = 404;

        HttpsURLConnection httpsURLConnection = null;
        URL url = new URL("https://www.railyatri.in/pnr-status/" + pnr1); //this https enabled site was allowing automated data extraction without giving any forbidden access response code
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
            pw.write("lccp_pnrno1="+pnr1);
            //pw.write("fullname="+pnr1);

            //pw.write("pnr_no="+pnr1);
            //pw.write("pnrno="+pnr1);
            //pw.write("pnr=" + pnr1);
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
        }

        logger.info("Establishing inflow path now, if possible...");
        try {
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                int lineNo = 1;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(lineNo>=606 && lineNo<=692)
                    logger.info("Line number " + lineNo + " --> " + line);
                    lineNo++;
                }
                bufferedReader.close();
            }
            else{
                logger.info("No reading of webpage as connection not ok!");
            }
        } catch (Exception e) {
            logger.info("Exception in the reading part... \n\tException : " + e);
        }
    }
}