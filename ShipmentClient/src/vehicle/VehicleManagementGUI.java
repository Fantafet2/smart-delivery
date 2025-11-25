package vehicle;

import clientHandler.ClientConnection;
import shared.ClientRequest;
import shared.ServerResponse;
import shared.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehicleManagementGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    private JTextField txtBrand, txtModel, txtYear, txtWeight, txtQuantity, txtStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public VehicleManagementGUI() {
        setTitle("Vehicle Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Fleet Vehicle Management", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] cols = {"ID", "Brand", "Model", "Year", "Weight Cap", "Qty Cap", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input panel
        JPanel panel = new JPanel(new GridLayout(3, 6, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle Details"));

        txtBrand = new JTextField(); txtModel = new JTextField(); txtYear = new JTextField();
        txtWeight = new JTextField(); txtQuantity = new JTextField(); txtStatus = new JTextField();

        panel.add(new JLabel("Brand:")); panel.add(txtBrand);
        panel.add(new JLabel("Model:")); panel.add(txtModel);
        panel.add(new JLabel("Year:")); panel.add(txtYear);

        panel.add(new JLabel("Weight Capacity:")); panel.add(txtWeight);
        panel.add(new JLabel("Quantity Capacity:")); panel.add(txtQuantity);
        panel.add(new JLabel("Status:")); panel.add(txtStatus);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        panel.add(btnAdd); panel.add(btnUpdate); panel.add(btnDelete); panel.add(btnRefresh);

        add(panel, BorderLayout.SOUTH);

        // Event handlers
        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        /*btnDelete.addActionListener(e -> deleteVehicle());
        btnRefresh.addActionListener(e -> loadVehicles());
*/
        loadVehicles();
        setVisible(true);
    }
    
    

    private void loadVehicles() {
        model.setRowCount(0);
        new Thread(() -> {
            try {
            	System.out.println("this is ........1");
                ClientConnection conn = new ClientConnection();
            	System.out.println("this is ........2");

		        ClientRequest request = new ClientRequest("GET_VEHICLES");

		        conn.sendFunciton(request);

		        ServerResponse  response = conn.receive();  

		        conn.close();

                if(response.getData() instanceof List<?> vehicles) {

                    SwingUtilities.invokeLater(() -> {
                        for(Object obj : vehicles) {
                            if(obj instanceof Vehicle v) {
                                model.addRow(new Object[]{
                                        v.getVehicleID(), v.getBrand(), v.getModel(),
                                        v.getYear(), v.getWeightCapacity(),
                                        v.getQuantityCapacity(), v.getStatus()
                                });
                            }
                        }
                    });
                }

            } catch(Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error loading vehicles: " + ex.getMessage()));
            }
        }).start();
    }

    private void addVehicle() {
        try {
            

            new Thread(() -> {
                try {
                	Vehicle v = new Vehicle(
                            txtBrand.getText().trim(),
                            txtModel.getText().trim(),
                            Integer.parseInt(txtYear.getText().trim()),
                            Double.parseDouble(txtWeight.getText().trim()),
                            Integer.parseInt(txtQuantity.getText().trim()),
                            txtStatus.getText().trim()
                    );
                	
                    ClientConnection conn = new ClientConnection();
                    
			        ClientRequest request = new ClientRequest("ADD_VEHICLE",v);
                    
			        conn.sendFunciton(request);
			        
			        ServerResponse  response = conn.receive();
			        
                    conn.close();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, request.getData());
                       loadVehicles();
                    });

                } catch(Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()));
                }
            }).start();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
        }
    }

    private void updateVehicle() {
        int row = table.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Select a vehicle to update."); return; }

        try {

            new Thread(() -> {
                try {
                	
                	Vehicle v = new Vehicle(
                            (int) model.getValueAt(row, 0),
                            txtBrand.getText().trim(),
                            txtModel.getText().trim(),
                            Integer.parseInt(txtYear.getText().trim()),
                            Double.parseDouble(txtWeight.getText().trim()),
                            Integer.parseInt(txtQuantity.getText().trim()),
                            txtStatus.getText().trim()
                    );
                	
                    ClientConnection conn = new ClientConnection();
                    
			        ClientRequest request = new ClientRequest("UPDATE_VEHICLE",v);
			        
			        conn.sendFunciton(request);
			        
			        ServerResponse  response = conn.receive();
                   
                   conn.close();
                   
                   SwingUtilities.invokeLater(() -> {
                       JOptionPane.showMessageDialog(this, request.getData());
                        loadVehicles();
                    });
                } catch(Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()));
                }
            }).start();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
        }
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Select a vehicle to delete."); return; }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this vehicle?", "Confirm", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                ClientConnection conn = new ClientConnection();
                
		        ClientRequest request = new ClientRequest("DELETE_VEHICLE",id);
                
                
		        conn.sendFunciton(request);
		        
		        ServerResponse  response = conn.receive();
		        
                conn.close();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, request.getData());
                    loadVehicles();
                });
            } catch(Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()));
            }
        }).start();
    }



	public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleManagementGUI::new);
    }
}