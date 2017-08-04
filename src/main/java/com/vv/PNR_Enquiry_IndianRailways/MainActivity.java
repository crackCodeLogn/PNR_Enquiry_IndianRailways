package com.vv.PNR_Enquiry_IndianRailways;

import com.vv.PNR_Enquiry_IndianRailways.HttpsAcquirer.PNR_EnquirerHttps;
import jdk.nashorn.internal.objects.NativeUint16Array;
import sun.applet.Main;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author Vivek
 * @version 1.0
 * @since 01-08-2017
 */
public class MainActivity extends JFrame {

    static final JTextField textPNR = new JTextField(10);
    static JButton buttonSearch;
    static JButton buttonCancel;
    static JButton buttonReset;

    public static boolean disableAll = false;

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
                if (pnr.length()!=0) {
                    if (isPNR_Valid(pnr)) {
                        try {
                            if(!disableAll) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            disableAll = true;
                                            performDisabling();
                                            new PNR_EnquirerHttps(pnr);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        } catch (Exception e1){
                            System.out.println("Exception occured : "+e1);
                        }
                    }
                    else{
                        System.out.println("Invalid PNR!");
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(MainActivity.this, "Invalid PNR!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    System.out.println("Empty!");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(MainActivity.this, "PNR length can't be 0!!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(buttonSearch);

        buttonCancel = new JButton("CANCEL");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(buttonCancel);

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

    private void performDisabling(){
        textPNR.setEnabled(false);
        buttonSearch.setEnabled(false);
        buttonCancel.setEnabled(false);
        buttonReset.setEnabled(false);
    }

    public static void performEnabling(){
        textPNR.setEnabled(true);
        buttonSearch.setEnabled(true);
        buttonCancel.setEnabled(true);
        buttonReset.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainActivity();
                frame.setVisible(true);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
