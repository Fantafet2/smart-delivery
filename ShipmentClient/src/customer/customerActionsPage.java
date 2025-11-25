package customer;

import javax.swing.*;

import clientHandler.ClientConnection;
import main.SharedGetInfo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import shared.SharedCustomer;
import shared.ClientRequest;
import shared.ServerResponse;

public class customerActionsPage extends JFrame{
	public customerActionsPage() {
		super("Customer Actions");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10)); 
		
		JLabel reqShipmentlb;
		
		JLabel senderNamelb;
		JTextField sendersNametf;

		JLabel recipientNamelb;
		JTextField recipientNametf;

		JLabel packageWeightlb;
		JTextField packageWeighttf;

		JLabel packageDemesionslb;
		JTextField packageDemesionstf;

		JLabel deliveryTypelb;
		String[] deliveryOptions = {"Standard", "Express", "Fragile"};

		JLabel deliveryZonelb;
		JLabel pickupZonelb;
		String[] zoneOptions = {"1","2","3","4"};
		
		JLabel destinationlb;
		JTextField destinationtf;
		
		JButton subrequstbt;
		JButton getQuotebt;
		
		JPanel customerpnl;
		
		customerpnl = new JPanel();
		customerpnl.setLayout(new BoxLayout(customerpnl, BoxLayout.Y_AXIS));
		customerpnl.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		add(customerpnl);
		
		reqShipmentlb = new JLabel("Enter the information required for a new shipment request");
		customerpnl.add(reqShipmentlb);
		
		senderNamelb = new JLabel("Eneter senders name");
		customerpnl.add(senderNamelb);
		sendersNametf = new JTextField(20);
		sendersNametf.setName("sendingCustomersName");
		customerpnl.add(sendersNametf);
		
		recipientNamelb = new JLabel("Enter recipients name");
		customerpnl.add(recipientNamelb);
		recipientNametf = new JTextField(20);
		recipientNametf.setName("receivingCustomersName");
		customerpnl.add(recipientNametf);
		
		packageWeightlb = new JLabel("Enter the package weight");
		customerpnl.add(packageWeightlb);
		packageWeighttf = new JTextField(20);
		packageWeighttf.setName("packageWeight");
		customerpnl.add(packageWeighttf);
		
		packageDemesionslb = new JLabel("Enter the package dimensions (Height/Width/Length");
		customerpnl.add(packageDemesionslb);
		packageDemesionstf = new JTextField(20);
		packageDemesionstf.setName("packageDemenssion");
		customerpnl.add(packageDemesionstf);
		
		deliveryTypelb = new JLabel("Select the type of delivery for this shipment");
		customerpnl.add(deliveryTypelb);
		JComboBox<String>  deliveryOptionscmb = new JComboBox<>(deliveryOptions);
		deliveryOptionscmb.setName("deliveryType");
		customerpnl.add(deliveryOptionscmb);
		
		deliveryZonelb= new JLabel("Select the zone for this delivery");
		customerpnl.add(deliveryZonelb);
		JComboBox<String>  deliveryZonecmb = new JComboBox<>(zoneOptions);
		deliveryZonecmb.setName("deliveryzoneOption");
		customerpnl.add(deliveryZonecmb);
		
		pickupZonelb= new JLabel("Select the zone where the package is coming from");
		customerpnl.add(pickupZonelb);
		JComboBox<String>  pickupZonecmb = new JComboBox<>(zoneOptions);
		pickupZonecmb.setName("pickupzoneOption");
		customerpnl.add(pickupZonecmb);

		destinationlb = new JLabel("Enter the address for this delivery");
		customerpnl.add(destinationlb);
		destinationtf = new JTextField(20);
		destinationtf.setName("destination");
		customerpnl.add(destinationtf);
		
		subrequstbt = new JButton("Create Request");
		customerpnl.add(subrequstbt);

		getQuotebt = new JButton("Generate Invoice");
		customerpnl.add(getQuotebt);
		
		Dimension tfPreferredSize = sendersNametf.getPreferredSize();
		sendersNametf.getPreferredSize();
		recipientNametf.getPreferredSize();
		packageWeighttf.getPreferredSize();
		packageDemesionstf.getPreferredSize();
		destinationtf.getPreferredSize();
		deliveryOptionscmb.getPreferredSize();
		deliveryZonecmb.getPreferredSize();
		pickupZonecmb.getPreferredSize();
		
		sendersNametf.setMaximumSize(tfPreferredSize);
        recipientNametf.setMaximumSize(tfPreferredSize);
        packageWeighttf.setMaximumSize(tfPreferredSize);
        packageDemesionstf.setMaximumSize(tfPreferredSize);
        destinationtf.setMaximumSize(tfPreferredSize);
        deliveryOptionscmb.setMaximumSize(tfPreferredSize);
        deliveryZonecmb.setMaximumSize(tfPreferredSize);
        pickupZonecmb.setMaximumSize(tfPreferredSize);
        
        getQuotebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				List<JComponent> customerInputs = List.of(sendersNametf, recipientNametf,packageWeighttf,packageDemesionstf,destinationtf);				
				Map<String, String> customerData = SharedGetInfo.collectInput(customerInputs);
				
				
				String senderName = customerData.get("sendingCustomersName");
				String receivererName = customerData.get("receivingCustomersName");
				String packageweight = customerData.get("packageWeight");
				String packagedemesion = customerData.get("packageDemenssion");
				String destination = customerData.get("destination");
				
			
				List<JComponent> deliveryTypeInput = List.of(deliveryOptionscmb,deliveryZonecmb, pickupZonecmb);
				Map<String, String> deliveyTypeData = SharedGetInfo.collectInput(deliveryTypeInput);
				String deliveryoption = deliveyTypeData.get("deliveryType");
				String deliveryzoneoptions = deliveyTypeData.get("deliveryzoneOption");
				String pickupzoneoptions = deliveyTypeData.get("pickupzoneOption");
		        
				int weight =Integer.parseInt(packageweight);
				int dropzone = Integer.parseInt(deliveryzoneoptions);
				int pickupzone = Integer.parseInt(pickupzoneoptions);

				
				new Thread(() -> {
					try {
						
						SharedCustomer newCustomer = new SharedCustomer(senderName,receivererName, destination,dropzone,pickupzone,weight,packagedemesion,deliveryoption);
//						SharedInvoice customerInvoice = new SharedInvoice(senderName, receivererName, destination, deliveryzoneoptions,pickupzoneoptions, packageweight, packagedemesion, deliveryoption);
//				        customerInvoice.createInvoice();

						ClientConnection conn = new ClientConnection();
						
				        ClientRequest request = new ClientRequest("GET_QUOTE",newCustomer);

				        //Sends the object and the name of the function to clientRequest 
						conn.sendFunciton(request);
						
				        //Gets back a response from the server (pass or fail)
						ServerResponse  response1 = conn.receive();
						

				        conn.close();
				        
				        SwingUtilities.invokeLater(() -> {
				            JOptionPane.showMessageDialog(null, "Server says: " + response1.getMessage());
				        });
						
					}catch (Exception e1) {
				        e1.printStackTrace();
				        SwingUtilities.invokeLater(() -> {
				            JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage());
				        });
					}
				}).start();
				
				
		        
			}
        	
        });

        subrequstbt.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				List<JComponent> customerInputs = List.of(sendersNametf, recipientNametf,packageWeighttf,packageDemesionstf,destinationtf);				
				Map<String, String> customerData = SharedGetInfo.collectInput(customerInputs);
				
				String senderName = customerData.get("sendingCustomersName");
				String receivererName = customerData.get("receivingCustomersName");
				String packageweight = customerData.get("packageWeight");
				String packagedemesion = customerData.get("packageDemenssion");
				String destination = customerData.get("destination");
				
				List<JComponent> deliveryTypeInput = List.of(deliveryOptionscmb,deliveryZonecmb, pickupZonecmb);
				Map<String, String> deliveyTypeData = SharedGetInfo.collectInput(deliveryTypeInput);
				String deliveryoption = deliveyTypeData.get("deliveryType");
				String deliveryzoneoptions = deliveyTypeData.get("deliveryzoneOption");
				String pickupzoneoptions = deliveyTypeData.get("pickupzoneOption");

		        System.out.println("pickupzone option " + pickupzoneoptions);

				
				System.out.println("this is option " + deliveryoption);
				//SendToDatabase.sendRequestData(senderName, receivererName, packageweight, packagedemesion, deliveryoption,deliveryzoneoptions,pickupzoneoptions, destination);
			}
        	
        });
        
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*protected SharedCustomer SharedCustomer(String string, String string2, String string3, String string4,
			String string5, String string6, String string7) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
