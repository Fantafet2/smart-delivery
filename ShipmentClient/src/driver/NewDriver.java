package driver;

import javax.swing.*;

import main.SharedGetInfo;
import clientHandler.ClientConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import shared.ClientRequest;
import shared.Driver;
import shared.ServerResponse;
import shared.Vehicle;


public class NewDriver extends JFrame {

	public NewDriver() {
		super("Driver actions");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10));
		 
		JLabel driverinfoInputlb;
		
		JLabel drivernamelb;
		JTextField driverNametf;
		
		JLabel driverZonelb;
		JTextField driverZonetf;
		
		JButton createDriverbtn;
		
		JPanel driverpnl;
		
		driverpnl = new JPanel();
		driverpnl.setLayout(new BoxLayout(driverpnl, BoxLayout.Y_AXIS));
		driverpnl.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		add(driverpnl);
	 	
		driverinfoInputlb = new JLabel("Enter Drivers Information below");
		driverinfoInputlb.setAlignmentX(Component.CENTER_ALIGNMENT);
		driverpnl.add(driverinfoInputlb);
		
		drivernamelb = new JLabel("Enter Driver Name");
		drivernamelb.setAlignmentX(Component.CENTER_ALIGNMENT);
		driverpnl.add(drivernamelb);
		
		driverNametf = new JTextField(20);
		driverNametf.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension driverNameSize = driverNametf.getPreferredSize();
		driverNametf.setMaximumSize(driverNameSize);
		driverNametf.setName("driverName");
		driverpnl.add(driverNametf);

		driverZonelb = new JLabel("Enter Driver Zone");
		driverZonelb.setAlignmentX(Component.CENTER_ALIGNMENT);
		driverpnl.add(driverZonelb);
		
		driverZonetf = new JTextField(20);
		driverZonetf.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension driverZoneSize = driverZonetf.getPreferredSize();
		driverZonetf.setMaximumSize(driverZoneSize);
		driverZonetf.setName("driverZone");
		driverpnl.add(driverZonetf);

		createDriverbtn = new JButton("Create Driver");
		driverpnl.add(createDriverbtn);

		createDriverbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				List<JComponent> addNewDriver = List.of(driverNametf, driverZonetf);				
				Map<String, String> driverData = SharedGetInfo.collectInput(addNewDriver);
			
				String driverZoneString = driverData.get("driverZone");
				int driverZone = Integer.parseInt(driverZoneString.trim());
				new Thread(() -> {
				    try {
				    	
						Driver newDriver = new Driver(driverData.get("driverName"),driverZone);		

				        ClientConnection conn = new ClientConnection();
				        
				        ClientRequest request = new ClientRequest("ADD_DRIVER",newDriver);
				        
				        //Sends the object and the name of the function to clientRequest 
				        conn.sendFunciton(request);
				        
				        //Gets back a response from the server (pass or fail)
				        ServerResponse  response = conn.receive();

				        conn.close();

				        SwingUtilities.invokeLater(() -> {
				            JOptionPane.showMessageDialog(null, "Server says: " + response.getMessage());
				        });

				    } catch (Exception e1) {
				        e1.printStackTrace();
				        SwingUtilities.invokeLater(() -> {
				            JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage());
				        });
				    } 
				}).start();

			}
		});
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
