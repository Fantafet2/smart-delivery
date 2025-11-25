package clerk;

import javax.swing.*;

import admin.createAdminAccount;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class clerkPage extends JFrame {
	
	public clerkPage() {
super("Admin Login");
		
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
		enterPasswordLabeel = new JLabel("Password: ");
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
            		SwingUtilities.invokeLater(() -> new createclerkAccount());
            	}
        });

		
	    setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void loginButtonClicked(String username, String password) {

		/*if(username.isEmpty()) {
			System.out.println("Username field cannot be empty");
			JOptionPane.showMessageDialog(null, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		if(password.isEmpty()) {
			System.out.println("Password field cannot be empty");
			JOptionPane.showMessageDialog(null, "The password field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}*/
		
		SwingUtilities.invokeLater(() -> new ClerkActionsPage());
	}

}
