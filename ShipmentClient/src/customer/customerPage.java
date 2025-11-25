package customer;

import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class customerPage extends JFrame{
	public customerPage() {
		super("customer Login");
		
		JTextField entercustomername;
		JTextField enterPassword;
		JLabel loginLabel;
		JLabel entercustomernameLabel;
		JLabel enterPasswordLabeel;
		JButton loginButton;
		JLabel createaccountLabel;
		JButton createAccount;
		
		JPanel customerLoginPanel;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10));
		
		loginLabel = new JLabel("Eneter Your Email and Password Below: ");
		entercustomernameLabel = new JLabel("customername: ");
		entercustomername = new JTextField(20);
		enterPasswordLabeel = new JLabel("Password: ");
		enterPassword = new JTextField(20);
		loginButton = new JButton("LOGIN");
		createaccountLabel = new JLabel("Don't have an account, create one useing the button below.");
		createAccount = new JButton("Create Account");
		
		loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		entercustomernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		entercustomername.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPasswordLabeel.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createaccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Dimension customernameSize = entercustomername.getPreferredSize();
		Dimension passwordSize = enterPassword.getPreferredSize();
		entercustomername.setMaximumSize(customernameSize);
		enterPassword.setMaximumSize(passwordSize);
		
		customerLoginPanel = new JPanel();
		customerLoginPanel.setLayout(new BoxLayout(customerLoginPanel, BoxLayout.Y_AXIS));
		customerLoginPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		customerLoginPanel.add(loginLabel);
		customerLoginPanel.add(entercustomernameLabel);
		customerLoginPanel.add(entercustomername);
		customerLoginPanel.add(enterPasswordLabeel);
		customerLoginPanel.add(enterPassword);
		customerLoginPanel.add(loginButton);
		customerLoginPanel.add(createaccountLabel);
		customerLoginPanel.add(createAccount);
		
        add(customerLoginPanel, BorderLayout.CENTER);
        
        loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String enteredcustomername = entercustomername.getText();
        		String enteredpassword = enterPassword.getText();


        		
        		loginButtonClicked(enteredcustomername,enteredpassword);
        	}
        });
        
        createAccount.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {	
            		SwingUtilities.invokeLater(() -> new createCustomerAccount());
            	}
        });

		
	    setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void loginButtonClicked(String customername, String password) {

		/*if(customername.isEmpty()) {
			System.out.println("customername field cannot be empty");
			JOptionPane.showMessageDialog(null, "The customername field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}
		if(password.isEmpty()) {
			System.out.println("Password field cannot be empty");
			JOptionPane.showMessageDialog(null, "The password field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE  );
		}*/
		
		SwingUtilities.invokeLater(()  -> new customerActionsPage());
	}
	

}
