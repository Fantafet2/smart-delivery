package driver;

import javax.swing.*;

import admin.createAdminAccount;
import clientHandler.ClientConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.ClientRequest;
import shared.CurrentDriver;
import shared.Driver;
import shared.ServerResponse;


public class driverPage extends JFrame{
	
	public driverPage() {
super("Driver Login");
		
		JTextField enterUsername;
		JTextField enterPassword;
		JLabel loginLabel;
		JLabel enterUsernameLabel;
		JLabel enterPasswordLabeel;
		JButton loginButton;
		JLabel createaccountLabel;
		JButton createAccount;
		
		JPanel adminLoginPanel;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10));
		
		loginLabel = new JLabel("Eneter Your Email and Password Below: ");
		enterUsernameLabel = new JLabel("Username: ");
		enterUsername = new JTextField(20);
		enterPasswordLabeel = new JLabel("ID#: ");
		enterPassword = new JTextField(20);
		loginButton = new JButton("LOGIN");
		createaccountLabel = new JLabel("Don't have an account, create one useing the button below.");
		createAccount = new JButton("Create Account");
		
		loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterUsernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPasswordLabeel.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createaccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Dimension usernameSize = enterUsername.getPreferredSize();
		Dimension passwordSize = enterPassword.getPreferredSize();
		enterUsername.setMaximumSize(usernameSize);
		enterPassword.setMaximumSize(passwordSize);
		
		adminLoginPanel = new JPanel();
		adminLoginPanel.setLayout(new BoxLayout(adminLoginPanel, BoxLayout.Y_AXIS));
		adminLoginPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		adminLoginPanel.add(loginLabel);
		adminLoginPanel.add(enterUsernameLabel);
		adminLoginPanel.add(enterUsername);
		adminLoginPanel.add(enterPasswordLabeel);
		adminLoginPanel.add(enterPassword);
		adminLoginPanel.add(loginButton);
		adminLoginPanel.add(createaccountLabel);
		adminLoginPanel.add(createAccount);
		
        add(adminLoginPanel, BorderLayout.CENTER);
        
        loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String enteredusername = enterUsername.getText();
        		String enteredpassword = enterPassword.getText();


        		
        		loginButtonClicked(enteredusername,enteredpassword);
        	}
        });
        
        createAccount.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {	
            		SwingUtilities.invokeLater(() -> new createDriverAccount());
            	}
        });

		
	    setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void loginButtonClicked(String username, String password) {

		SwingUtilities.invokeLater(() -> new driverPage ());
		if(username.isEmpty()) {
			System.out.println("Username field cannot be empty");
			JOptionPane.showMessageDialog(null, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		if(password.isEmpty()) {
			System.out.println("Password field cannot be empty");
			JOptionPane.showMessageDialog(null, "The password field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		
		int IDnum = Integer.parseInt(password);

        new Thread(() -> {

        	try {
        		CurrentDriver currentDriver = new CurrentDriver(username,IDnum);

           	 	ClientConnection conn = new ClientConnection();
                
                ClientRequest request = new ClientRequest("DRIVER_LOGIN",currentDriver);
                
                conn.sendFunciton(request);
                
   		     	ServerResponse  response = conn.receive();

   		        conn.close();
   		        
   		     if (response.isSuccess()) {
    		        SwingUtilities.invokeLater(() -> new VeiwAssignedShipments());   	            
   	        } else {
   	            

   	         SwingUtilities.invokeLater(() -> {
		            JOptionPane.showMessageDialog(null, "Server says: " + response.getMessage());
		        });   	        }
   		        

        	 } catch (Exception e1) {
			    	e1.printStackTrace();
			        SwingUtilities.invokeLater(() -> {
			            // Display the class name of the exception, followed by the message
			            String errorType = e1.getClass().getName();
			            String errorMessage = e1.getMessage() != null ? e1.getMessage() : "No detailed message provided.";
			            
			            JOptionPane.showMessageDialog(null, 
			                "Error Type: " + errorType + 
			                "\nDetails: " + errorMessage, 
			                "Communication Error", 
			                JOptionPane.ERROR_MESSAGE);
			        });
			    } 
        	

        }).start();
		
	}
	

}
