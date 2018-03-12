package com.vv.PNR_Enquiry_IndianRailways;

import com.vv.PNR_Enquiry_IndianRailways.HttpsAcquirer.PNR_EnquirerHttps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vivek
 * @version 1.0
 * @since 01-08-2017
 */
public class MainActivity extends JFrame {

    private static final JTextField textPNR = new JTextField(10);
    private static JButton buttonSearch;
    private static JButton buttonExit;
    private static JButton buttonReset;

    public static boolean disableAll = false;
    //public final static String smallLogoPath = "/src/main/java/com/vv/PNR_Enquiry_IndianRailways/raw/logoIR_medium256.jpg";
    //public final static String smallLogoPath = "/com/vv/PNR_Enquiry_IndianRailways/raw/logoIR_medium256.jpg";
    ////public final static String smallLogoPath = "/logoIR_medium256.jpg"; //image in the resources folder of the project
    //public final static String smallLogoPath = "/logoIR_full1024.png"; //image in the resources folder of the project
    public final static String smallLogoPath = "/logoIR_small32.png"; //image in the resources folder of the project
    public final static String mediumLogoPath = "/logoIR_medium256.png"; //image in the resources folder of the project
    public final static String bigLogoPath = "/logoIR_full1024.png"; //image in the resources folder of the project


    public MainActivity() {
        setTitle("PNR Enquirer");
        setLayout(new FlowLayout());

        final JLabel displayPNR = new JLabel("Enter the PNR ");
        add(displayPNR);

        //textPNR = new JTextField(10);
        add(textPNR);

        buttonSearch = new JButton("SEARCH");
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String pnr = textPNR.getText();
                if (pnr.length() != 0) {
                    if (isPNR_Valid(pnr)) {
                        try {
                            if (!disableAll) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disableAll = true;
                                        performDisabling();
                                        new PNR_EnquirerHttps(pnr);
                                    }
                                }).start();
                            }
                            /*
                            if(!disableAll){
                                disableAll = true;
                                performDisabling();
                                //PNR_EnquirerHttps.PNR_EnquirerHttpsRunner(pnr);
                                new PNR_EnquirerHttps(pnr);

                                /*
                                while(PNR_EnquirerHttps.loopON){

                                }

                            }
                            */
                        } catch (Exception e1) {
                            System.out.println("Exception occured : " + e1);
                        }
                    } else {
                        System.out.println("Invalid PNR!");
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(MainActivity.this, "Invalid PNR!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("Empty!");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(MainActivity.this, "PNR length can't be 0!!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(buttonSearch);

        buttonExit = new JButton("EXIT");
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(buttonExit);

        buttonReset = new JButton("RESET");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPNR.setText("");
            }
        });
        add(buttonReset);
    }

    private boolean isPNR_Valid(String pnr) {
        boolean isValid = false;
        if (pnr.length() == 10) {
            int i;
            for (i = 0; i < pnr.length(); i++) {
                char ch = pnr.charAt(i);
                if (!Character.isDigit(ch)) break;
            }
            if (i == 10) isValid = true;
        }
        return isValid;
    }

    /**
     * Locking up the interface whilst the request is being processed
     */
    public void performDisabling() {
        System.out.println("Inside the disabling function");
        textPNR.setEnabled(false);
        buttonSearch.setEnabled(false);
        buttonExit.setEnabled(false);
        buttonReset.setEnabled(false);
    }

    /**
     * Switches on the interface, so that the next input can be taken
     */
    public static void performEnabling() {
        textPNR.setEnabled(true);
        buttonSearch.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonReset.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainActivity();
                frame.setVisible(true);
                frame.pack();
                frame.setLocationRelativeTo(null);
                try {
                    frame.setIconImage(new ImageIcon(Toolkit.getDefaultToolkit().createImage(MainActivity.class.getResource(smallLogoPath))).getImage());
                } catch (Exception npe1) {

                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
