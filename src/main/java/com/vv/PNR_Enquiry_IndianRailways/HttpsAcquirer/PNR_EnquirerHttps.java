package com.vv.PNR_Enquiry_IndianRailways.HttpsAcquirer;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.vv.PNR_Enquiry_IndianRailways.LoggerFormatter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.*;

/**
 * This deals with the Https Acquirer for the pnr request...
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

        HttpsURLConnection httpsURLConnection = null;
        URL url = new URL("https://www.railyatri.in/pnr-status/"+pnr1); //giving 404 resp
        //logger.info("URL : "+url.toString());
        logger.info("URL : "+url.toString());
        System.out.println("URL : "+url.toString());

        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.connect();

            /*
            PrintWriter pw = new PrintWriter(httpsURLConnection.getOutputStream());
            pw.write("lccp_pnrno1="+pnr1);
            //pw.write("fullname="+pnr1);

            //pw.write("pnr_no="+pnr1);
            //pw.write("pnrno="+pnr1);
            //pw.write("pnr=" + pnr1);
            pw.flush();
            pw.close();
            */
            logger.info("Code received : " + httpsURLConnection.getResponseCode());
        } catch (Exception e) {
            logger.info("Exception : " + e);
        }

        try {
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            int lineNo = 1;
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                logger.info("Line number " + (lineNo++) + " --> " + line);
            }
            //logger.info("Lines:-\n"+line);
            logger.info("Overshooting the line number : " + lineNo);
            bufferedReader.close();
        } catch (Exception e) {
            logger.info("Exception in the reading part... \n\tException : "+e);
        }
    }
}
