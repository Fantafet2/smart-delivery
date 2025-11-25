package clerk;

import javax.swing.*;

import clientHandler.ClientConnection;
import shared.ClientRequest;
import shared.ServerResponse;
import shared.Shipment;
import shared.PendingShipment;
import shared.Driver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;


public class AssignShipment extends JFrame {

    private JButton getPendingBtn,  assignBtn;
    private JList<PendingShipment> pendingList;
    private JList<Driver> driverList;
    private DefaultListModel<PendingShipment> pendingModel;
    private DefaultListModel<Driver> driverModel;
    private JLabel dateCreatedLabel;
    private List<PendingShipment> pendingShipmentObjects;
    private List<Driver> DriverObjects;
    private Shipment selectedShipmentToAssign;

    public AssignShipment() {
        super("Assign Shipment");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 800);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // TITLE
        JLabel title = new JLabel("Assign Shipment");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));


        // DATE CREATED
        dateCreatedLabel = new JLabel("Date Created: " + LocalDate.now());
        mainPanel.add(dateCreatedLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- GET PENDING SHIPMENTS ---
        getPendingBtn = new JButton("Get Pending Shipments");
        getPendingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(getPendingBtn);

        pendingModel = new DefaultListModel<>();
        pendingList = new JList<>(pendingModel);
        pendingList.setBorder(BorderFactory.createTitledBorder("Pending Shipments"));

        JScrollPane pendingScroll = new JScrollPane(pendingList);
        pendingScroll.setPreferredSize(new Dimension(500, 200));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(pendingScroll);

       

        driverModel = new DefaultListModel<>();
        driverList = new JList<>(driverModel);
        driverList.setBorder(BorderFactory.createTitledBorder("Drivers in Zone"));

        JScrollPane driverScroll = new JScrollPane(driverList);
        driverScroll.setPreferredSize(new Dimension(500, 200));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(driverScroll);

        // --- ASSIGN BUTTON ---
        assignBtn = new JButton("Assign Shipment to Driver");
        assignBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(assignBtn);

        getPendingBtn.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
			@Override
            public void actionPerformed(ActionEvent e) {


            	new Thread(() -> {
            	    try {
            	        
            	    	ClientConnection conn = new ClientConnection();
            	        
            	    	ClientRequest request = new ClientRequest("GET_PENDING_SHIPMENTS_FOR_CLERK", null); 
            	        
            	    	conn.sendFunciton(request);
            	        
            	    	ServerResponse response = conn.receive();
            	        
            	    	conn.close();

            	        if (response.isSuccess()) {
            	            // Store the full objects in the instance variable
            	        	pendingShipmentObjects = (List<PendingShipment>) response.getData();

            	            SwingUtilities.invokeLater(() -> {
            	                pendingModel.clear(); // Clear the JList model
            	                for (PendingShipment s : pendingShipmentObjects) {
            	                	pendingModel.addElement(s);
            	                }
            	            });
            	        } else {
            	            SwingUtilities.invokeLater(() -> {
            	                JOptionPane.showMessageDialog(AssignShipment.this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            	            });
            	        }
            	    } catch (Exception ex) {
            	        ex.printStackTrace();
            	    }
            	}).start();
            }
        });
        
        pendingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                PendingShipment selected = pendingList.getSelectedValue();
                if (selected == null) return;

                int zone = selected.getZone();  

                System.out.println("Selected shipment zone = " + zone);

                loadDriversForZone(zone);
            }
        });
        
        driverList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Driver selectedDriver = driverList.getSelectedValue();
                PendingShipment selectedShipment = pendingList.getSelectedValue();

                if (selectedDriver != null && selectedShipment != null) {
                    dateCreatedLabel.setText("Ready to assign Shipment ID: " + selectedShipment.getTrackingNum() 
                                             + " to Driver: " + selectedDriver.getDriverName());
                    assignBtn.setEnabled(true);
                } else {
                    assignBtn.setEnabled(false);
                }
            }
        });
        
       
        assignBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				PendingShipment selectedShipment = pendingList.getSelectedValue();
				PendingShipment selectedsendersName = pendingList.getSelectedValue();
				PendingShipment selectedrecipientName = pendingList.getSelectedValue();
				PendingShipment selectedDestination = pendingList.getSelectedValue();
				PendingShipment selectedStatus = pendingList.getSelectedValue();
                
				Driver selectedDriverid = driverList.getSelectedValue();
				Driver selectedDrivername = driverList.getSelectedValue();
                
                int trackingNum = selectedShipment.getTrackingNum();
            	String sendersName = selectedsendersName.getSendersName();
            	String recipientName = selectedrecipientName.getRecipientName();
            	String Destination = selectedDestination.getDestination();
            	String DriverName = selectedDrivername.getDriverName() ;
            	int assignDriver = selectedDriverid.getDID();
                String shipmenetStatus = selectedStatus.getStatus();
            	int cost = 200;
            	LocalDate DateCreated = LocalDate.now();
            	LocalDate estimatedDeliveryDate = LocalDate.now();
                
            	int vehicle = 300;
            	int estimatedDistance = 44;
                
                
                new Thread(() -> {
				    try {
		                Shipment newShipment = new Shipment(trackingNum,sendersName,recipientName,Destination,DriverName,assignDriver,shipmenetStatus,cost,DateCreated,estimatedDeliveryDate);

		                ClientConnection conn = new ClientConnection();
		                
		                ClientRequest request = new ClientRequest("ASSIGN_SHIPMENT",newShipment);
		                
		                conn.sendFunciton(request);
		                
				        ServerResponse  response = conn.receive();

				        conn.close();
				        
				        SwingUtilities.invokeLater(() -> {
				            JOptionPane.showMessageDialog(null, "Server says: " + response.getMessage());
				        });
						
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
        });
        
        
        // Initial state
        assignBtn.setEnabled(false);
        

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void loadDriversForZone(int zone) {

        new Thread(() -> {
            try {
                ClientConnection conn = new ClientConnection();
                ClientRequest req = new ClientRequest("GET_DRIVERS_BY_ZONE", zone);

                conn.sendFunciton(req);
                ServerResponse response = conn.receive();
                conn.close();

                if (response.isSuccess()) {

                    DriverObjects = (List<Driver>) response.getData();

                    SwingUtilities.invokeLater(() -> {
                        driverModel.clear();
                        for (Driver d : DriverObjects) {
                            driverModel.addElement(d);
                        }
                    });

                } else {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

}
