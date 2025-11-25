package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AccountSelectorView  extends JFrame{
	
    private JRadioButton adminRadio;
    private JRadioButton clerkRadio;
    private JRadioButton driverRadio;
    private JRadioButton manegerRadio;

    private JButton proceedButton;
    private ButtonGroup accountTypeGroup;
    
	public AccountSelectorView () {
		
		super("Account Type selection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		setLayout(new BorderLayout(10,10));
		
		JLabel titleLable = new JLabel("Please Select Your Account Type: ",SwingConstants.CENTER);
		titleLable.setFont(new Font("Arial", Font.BOLD, 18));
		
	    adminRadio = new JRadioButton("Administrator");
	    clerkRadio = new JRadioButton("Clerk User");
	    driverRadio = new JRadioButton("Driver Access");
	    manegerRadio = new JRadioButton("Maneger Access");
	    
	    accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(adminRadio);
        accountTypeGroup.add(clerkRadio);
        accountTypeGroup.add(driverRadio);
        accountTypeGroup.add(manegerRadio);
        
        clerkRadio.setSelected(true);
        
        proceedButton = new JButton("Proceed to Login");
        
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));
        
        JRadioButton[] buttons = {adminRadio, clerkRadio, driverRadio, manegerRadio};
        
        for (JRadioButton button : buttons) {
        	selectionPanel.add(button);
            selectionPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        }
        
        add(titleLable, BorderLayout.NORTH);
        add(selectionPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(proceedButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	handleProceedAction();
            }
        });
        
        setLocationRelativeTo(null);
        setVisible(true);
        
	}
	
	
	private void handleProceedAction() {
        String selectedType = "";
        
        if (adminRadio.isSelected()) {
            selectedType = "Administrator";
        } else if (clerkRadio.isSelected()) {
            selectedType = "Standard User";
        } else if (driverRadio.isSelected()) {
            selectedType = "Guest Access";
        } else {
        
        JOptionPane.showMessageDialog(this, "Please select an account type.", "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }
	}
}
