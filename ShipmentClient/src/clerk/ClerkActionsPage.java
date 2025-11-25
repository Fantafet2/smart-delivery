package clerk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkActionsPage extends JFrame{
	
	public ClerkActionsPage() {
		super("Clerk Page");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setLayout(new BorderLayout(10,10));
		
		JLabel clerkOperationLabel;
		JLabel shipReqLabel;
		JLabel assignPackageLabel;
		JLabel handelPaymentLabel;
		
		JButton shipReqbt;
		JButton assignPackagebt;
		JButton handlePaymentbt;
		
		JPanel clerkOperationPnl;
		
		clerkOperationPnl = new JPanel();
		clerkOperationPnl.setLayout(new BoxLayout(clerkOperationPnl, BoxLayout.Y_AXIS));
		clerkOperationPnl.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		add(clerkOperationPnl);		
		
		clerkOperationLabel = new JLabel("Chose a Operation");
		clerkOperationPnl.add(clerkOperationLabel);

		shipReqLabel = new JLabel("Process Shipment requests");
		shipReqbt = new JButton("Shipment Request");
		clerkOperationPnl.add(shipReqLabel);
		clerkOperationPnl.add(shipReqbt);

		assignPackageLabel = new JLabel("Assign a delivery Route");
		assignPackagebt = new JButton("Assign Route");
		clerkOperationPnl.add(assignPackageLabel);
		clerkOperationPnl.add(assignPackagebt);

		handelPaymentLabel = new JLabel("Handle Payments ");
		handlePaymentbt = new JButton("Handle Payment");
		clerkOperationPnl.add(handelPaymentLabel);
		clerkOperationPnl.add(handlePaymentbt);
		
		shipReqbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> new shipmentRequest());
			}
		});
		
		assignPackagebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> new AssignShipment());
			}
		});
		
		assignPackagebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> new handlePayment());
			}
		});
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
