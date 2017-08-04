package com.vv.PNR_Enquiry_IndianRailways;

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

    public MainActivity() {
        setTitle("PNR Enquirer");
        setLayout(new FlowLayout());
        JLabel displayPNR = new JLabel("Enter the PNR ");
        add(displayPNR);

        final JTextField textPNR = new JTextField(10);
        add(textPNR);

        JButton buttonSearch = new JButton("SEARCH");
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pnr = textPNR.getText();
                if (pnr != null) {
                    if (isPNR_Valid(pnr)) {


                    }
                }
            }
        });
        add(buttonSearch);

        JButton buttonCancel = new JButton("CANCEL");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(buttonCancel);

        JButton buttonReset = new JButton("RESET");
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
