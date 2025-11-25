package driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import clientHandler.ClientConnection;
import shared.ClientRequest;
import shared.ServerResponse;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VeiwAssignedShipments extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private String clerkName;
    private int clerkId;

    public void ViewAssignedShipments(String name, int id) {
        this.clerkName = name;
        this.clerkId = id;

        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- TABLE SETUP ----------
        model = new DefaultTableModel(
                new String[]{"Shipment ID", "Sender", "Receiver", "Destination", "Weight"}, 0
        );
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ---------- CLICK EVENT ----------
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    showRowDetails(row);
                }
            }
        });

        // ---------- AUTO LOAD DATA ----------
        loadAssignedShipments(id);

        setVisible(true);
    }


    private void loadAssignedShipments(int id) {
        try {
        	 ClientConnection conn = new ClientConnection();

             ClientRequest request = new ClientRequest("GET_ORDER_ASSIGNED_TO_SPECIFIC_DRIVER", id);
             conn.sendFunciton(request);

             ServerResponse response = conn.receive();
             conn.close();

            if (Object o != null) {
                for (AssignedShipment s : shipments) {
                    model.addRow(new Object[]{
                            s.getShipmentId(),
                            s.getSenderName(),
                            s.getReceiverName(),
                            s.getDestination(),
                            s.getWeight()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "No shipments assigned.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading shipments: " + ex.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Show details when a row is clicked
    // ------------------------------------------------------------
    private void showRowDetails(int row) {
        String id = model.getValueAt(row, 0).toString();
        String sender = model.getValueAt(row, 1).toString();
        String receiver = model.getValueAt(row, 2).toString();
        String destination = model.getValueAt(row, 3).toString();
        String weight = model.getValueAt(row, 4).toString();

        JOptionPane.showMessageDialog(this,
                "Shipment ID: " + id +
                "\nSender: " + sender +
                "\nReceiver: " + receiver +
                "\nDestination: " + destination +
                "\nWeight: " + weight,
                "Shipment Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
