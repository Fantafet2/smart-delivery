package admin;



import javax.swing.*;

import javax.swing.SwingUtilities;

import admin.createAdminAccount;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.*;



public class manegerActionPage extends JFrame {

    // Update this path if your file is located elsewhere

    private static final String DATA_FILE_PATH = "src/admin/admin_login_info.txt";



    private final String adminSubtype; // "Manager" or "Clerk"



    public manegerActionPage(String adminSubtype) {

        super(adminSubtype + " Login");

        this.adminSubtype = adminSubtype;

        initUI();

    }



    // Keep a zero-arg constructor for compatibility (defaults to Admin generic login)

    public manegerActionPage() {

        this("Admin");

    }



    private void initUI() {

        JTextField enterUsername;

        JPasswordField enterPassword;           // replaced JTextField with JPasswordField

        JLabel loginLabel;

        JLabel enterUsernameLabel;

        JLabel enterPasswordLabeel;

        JButton loginButton;

        JLabel createaccountLabel;

        JButton createAccount;



        JPanel userLoginPanel;



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(600, 800);

        setLayout(new BorderLayout(10, 10));



        loginLabel = new JLabel("Enter Your Email and Password Below: ");

        enterUsernameLabel = new JLabel("Username: ");

        enterUsername = new JTextField(20);

        enterPasswordLabeel = new JLabel("Password: ");

        enterPassword = new JPasswordField(20); // masked password field

        loginButton = new JButton("LOGIN");

        createaccountLabel = new JLabel("Don't have an account? Create one using the button below.");

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

        userLoginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        userLoginPanel.add(loginLabel);

        userLoginPanel.add(Box.createVerticalStrut(8));

        userLoginPanel.add(enterUsernameLabel);

        userLoginPanel.add(enterUsername);

        userLoginPanel.add(Box.createVerticalStrut(8));

        userLoginPanel.add(enterPasswordLabeel);

        userLoginPanel.add(enterPassword);

        userLoginPanel.add(Box.createVerticalStrut(12));

        userLoginPanel.add(loginButton);

        userLoginPanel.add(Box.createVerticalStrut(12));

        userLoginPanel.add(createaccountLabel);

        userLoginPanel.add(createAccount);



        add(userLoginPanel, BorderLayout.CENTER);



        // Login button behavior: reads values and calls the existing loginButtonClicked

        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String enteredusername = enterUsername.getText().trim();

                String enteredpassword = new String(enterPassword.getPassword()).trim();



                loginButtonClicked(enteredusername, enteredpassword);

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



        if (username == null || username.isEmpty()) {

            System.out.println("Username field cannot be empty");

            JOptionPane.showMessageDialog(this, "The username field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);

            return;

        }

        if (password == null || password.isEmpty()) {

            System.out.println("Password field cannot be empty");

            JOptionPane.showMessageDialog(this, "The password field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);

            return;

        }



        boolean ok = authenticateFromFile(username, password);

        if (ok) {

            JOptionPane.showMessageDialog(this, "Login successful as " + adminSubtype, "Success", JOptionPane.INFORMATION_MESSAGE);

            openDashboardForRole(username);

        } else {

            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);

        }

    }



   
    private boolean authenticateFromFile(String username, String password) {

        File file = new File(DATA_FILE_PATH);

        if (!file.exists()) {

            // fallback to package location

            file = new File("admin/admin_login_info.txt");

        }



        if (!file.exists()) {

            System.err.println("Admin credentials file not found. Checked: " + DATA_FILE_PATH + " and admin/admin_login_info.txt");

            return false;

        }



        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                // expecting format username'password (single quote separator)

                String[] parts = line.split("'", 2);

                if (parts.length == 2) {

                    String fileUser = parts[0].trim();

                    String filePass = parts[1].trim();

                    if (fileUser.equalsIgnoreCase(username) && filePass.equals(password)) {

                        return true;

                    }

                }

            }

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        return false;

    }


    private void openDashboardForRole(String username) {

        // Close login window

        this.dispose();



        if ("Manager".equalsIgnoreCase(adminSubtype)) {

            try{

                new maneger.manegerPage();

            } catch (Throwable ex) {

               

                System.err.println("Could not open maneger.manegerPage automatically: " + ex.getMessage());

                JFrame f = new JFrame("Manager Dashboard - " + username);

                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                f.setSize(600, 400);

                f.add(new JLabel("Welcome Manager: " + username, SwingConstants.CENTER));

                f.setLocationRelativeTo(null);

                f.setVisible(true);

            }

        } else if ("Clerk".equalsIgnoreCase(adminSubtype)) {

            try {

                new clerk.clerkPage();

            } catch (Throwable t) {

                // fallback if clerk page not present

                JFrame f = new JFrame("Clerk Dashboard - " + username);

                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                f.setSize(600, 400);

                f.add(new JLabel("Welcome Clerk: " + username, SwingConstants.CENTER));

                f.setLocationRelativeTo(null);

                f.setVisible(true);

            }

        } else {

            // Generic admin fallback

            JFrame f = new JFrame("Admin Dashboard - " + username);

            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            f.setSize(600, 400);

            f.add(new JLabel("Welcome " + adminSubtype + ": " + username, SwingConstants.CENTER));

            f.setLocationRelativeTo(null);

            f.setVisible(true);

        }

    }

}