package admin;

import javax.swing.*;
import javax.swing.SwingUtilities;

import driver.DriverActionPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame{
	public AdminPage() {
		super("user Login");
		
		JTextField enterUsername;
		JTextField enterPassword;
		JLabel loginLabel;
		JLabel enterUsernameLabel;
		JLabel enterPasswordLabeel;
		JButton loginButton;
		JLabel createaccountLabel;
		JButton createAccount;
		
		JPanel userLoginPanel;
		
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
		
		userLoginPanel = new JPanel();
		userLoginPanel.setLayout(new BoxLayout(userLoginPanel, BoxLayout.Y_AXIS));
		userLoginPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		userLoginPanel.add(loginLabel);
		userLoginPanel.add(enterUsernameLabel);
		userLoginPanel.add(enterUsername);
		userLoginPanel.add(enterPasswordLabeel);
		userLoginPanel.add(enterPassword);
		userLoginPanel.add(loginButton);
		userLoginPanel.add(createaccountLabel);
		userLoginPanel.add(createAccount);
		
        add(userLoginPanel, BorderLayout.CENTER);
        
        loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String enteredusername = enterUsername.getText();
        		String enteredpassword = enterPassword.getText();


        		
        		loginButtonClicked(enteredusername,enteredpassword);
        	}
        });
        
        createAccount.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {	
            		SwingUtilities.invokeLater(() -> new createAdminAccount());
            	}
        });

		
	    setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void loginButtonClicked(String username, String password) {
		SwingUtilities.invokeLater(() -> new manegerActionPage());

		/*if(username.isEmpty()) {
			System.out.println("Username field cannot be empty");
			JOptionPane.showMessageDialog(null, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		if(password.isEmpty()) {
			System.out.println("Password field cannot be empty");
			JOptionPane.showMessageDialog(null, "The password field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}*/
	}
	

}
