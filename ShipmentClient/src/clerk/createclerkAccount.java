package clerk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class createclerkAccount extends JFrame{
	
	public createclerkAccount() {
super("Create an Admin Account");
		
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
		JPanel createAdminAccountPanel;
		
		createAdminAccountPanel= new JPanel();
		createAdminAccountPanel.setLayout(new BoxLayout(createAdminAccountPanel, BoxLayout.Y_AXIS));
		createAdminAccountPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		
		createaccountLabel = new JLabel("Create an Admin Username and Password Below ");
		createAdminAccountPanel.add(createaccountLabel);
		
		createUsernameLabel = new JLabel("Create Admin Username: ");
		createUsernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAdminAccountPanel.add(createUsernameLabel);

		createUsername = new JTextField(20);
		createUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension usernameSize = createUsername.getPreferredSize();
		createUsername.setMaximumSize(usernameSize);
		createAdminAccountPanel.add(createUsername);
		
		createPasswordLabel = new JLabel("Create Admin Password: ");
		createPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAdminAccountPanel.add(createPasswordLabel);
		
		createPassword = new JTextField(20);
		createPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension passwordsameSize = createPassword.getPreferredSize();
		createPassword.setMaximumSize(passwordsameSize);
		createAdminAccountPanel.add(createPassword);

		usernameRequirments = new JLabel("Username should contain one # character");
		usernameRequirments.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAdminAccountPanel.add(usernameRequirments);

		createAccount = new JButton("Create Account");
		createAccount.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		createAdminAccountPanel.add(createAccount);
		
        add(createAdminAccountPanel, BorderLayout.CENTER);
        
        createAccount.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String entereusername = createUsername.getText();
        		String enteredpassword = createPassword.getText();

        		createAdminButtonClicked(entereusername,enteredpassword);
        	}
			
        });

		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void createAdminButtonClicked(String entereusername, String enteredpassword) {	
		checkUsernameRequirement(entereusername);
		
	}
	
	public void checkUsernameRequirement(String username) {
		
		if(username.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		else if (username.contains("#")) {
			JOptionPane.showMessageDialog(null, "Username approved", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		else {
			JOptionPane.showMessageDialog(null, "Invalid Admin Username", "Validation Error", JOptionPane.PLAIN_MESSAGE  );
		}
	}

}


