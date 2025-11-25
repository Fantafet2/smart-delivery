package customer;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class createCustomerAccount extends JFrame{
	
	private static final String FILE_NAME = "customer_login_info.txt";
	public createCustomerAccount() {
		super("Create customer Account");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10));
		
		JLabel createaccountLabel;
		
		JTextField createUsername;
		JTextField createPassword;
		
		JLabel createPasswordLabel;
		JLabel createUsernameLabel;
		
		JLabel usernameRequirments;
		
		JButton createAccount;
		JPanel createcustomerAccountPanel;
		
		createcustomerAccountPanel= new JPanel();
		createcustomerAccountPanel.setLayout(new BoxLayout(createcustomerAccountPanel, BoxLayout.Y_AXIS));
		createcustomerAccountPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		
		createaccountLabel = new JLabel("Create an customer Username and Password Below ");
		createcustomerAccountPanel.add(createaccountLabel);
		
		createUsernameLabel = new JLabel("Create customer Username: ");
		createUsernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createcustomerAccountPanel.add(createUsernameLabel);

		createUsername = new JTextField(20);
		createUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension usernameSize = createUsername.getPreferredSize();
		createUsername.setMaximumSize(usernameSize);
		createcustomerAccountPanel.add(createUsername);
		
		createPasswordLabel = new JLabel("Create customer Password: ");
		createPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createcustomerAccountPanel.add(createPasswordLabel);
		
		createPassword = new JTextField(20);
		createPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension passwordsameSize = createPassword.getPreferredSize();
		createPassword.setMaximumSize(passwordsameSize);
		createcustomerAccountPanel.add(createPassword);

		usernameRequirments = new JLabel("Username should contain one @ character");
		usernameRequirments.setAlignmentX(Component.CENTER_ALIGNMENT);
		createcustomerAccountPanel.add(usernameRequirments);

		createAccount = new JButton("Create Account");
		createAccount.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		createcustomerAccountPanel.add(createAccount);
		
        add(createcustomerAccountPanel, BorderLayout.CENTER);
        
        createAccount.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String entereusername = createUsername.getText();
        		String enteredpassword = createPassword.getText();

        		createcustomerButtonClicked(entereusername,enteredpassword);
        		createUsername.setText("");
        		createPassword.setText("");
        	}
        });

		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void createcustomerButtonClicked(String entereusername, String enteredpassword) {	
		
		if(enteredpassword.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The password feild cannot be empty","Validation Error", JOptionPane.PLAIN_MESSAGE);
		}
		
		if(entereusername.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		else if (entereusername.contains("@")) {
			writeUserToFile( entereusername, enteredpassword);
			
		}
		else {
			JOptionPane.showMessageDialog(null, "Invalid customer Username", "Validation Error", JOptionPane.PLAIN_MESSAGE  );
		}
		

	}
	
	public void writeUserToFile(String username, String password) {
		try(BufferedWriter  writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){
			writer.write(username + "'" + password);
			writer.newLine();
			 JOptionPane.showMessageDialog(this, "Account saved!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
