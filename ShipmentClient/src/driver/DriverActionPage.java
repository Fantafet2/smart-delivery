package driver;

import javax.swing.*;

import main.AccountSelectionWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DriverActionPage extends JFrame {

    private JButton addDriverBtn;
    private JButton viewOrdersBtn;


    public DriverActionPage() {
        super("Driver Application Menu");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // --- TITLE ---
        JLabel title = new JLabel("Driver Main Menu");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        // --- ADD DRIVER BUTTON ---
        addDriverBtn = new JButton("Add a New Driver");
        addDriverBtn.setPreferredSize(new Dimension(200, 40));
        add(addDriverBtn);

        // --- VIEW ORDERS BUTTON ---
        viewOrdersBtn = new JButton("Login");
        viewOrdersBtn.setPreferredSize(new Dimension(200, 40));
        add(viewOrdersBtn);

        // --- ADD ACTION LISTENERS ---
        addDriverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new NewDriver());

            }
        });

        viewOrdersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new driverPage());

            }
        });

        setLocationRelativeTo(null); 
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DriverActionPage();
        });
    }
}