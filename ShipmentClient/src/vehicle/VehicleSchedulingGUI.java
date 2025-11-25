package vehicle;

import shared.ClientRequest;
import shared.ServerResponse;
import shared.VehicleAssignmentRequest;
import shared.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VehicleSchedulingGUI extends JFrame {

    private DefaultTableModel vehicleTableModel;
    private JTable vehicleTable;

    private JTextField txtStartTime, txtEndTime, txtRoute, txtWeight, txtQuantity;
    private JButton btnAssign, btnRefresh, btnViewSchedules;

    public VehicleSchedulingGUI() {
        setTitle("Vehicle Scheduling & Assignment");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Title ---
        JLabel title = new JLabel("Vehicle Scheduling System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // --- Vehicle Table ---
        String[] columns = {"ID", "Brand", "Model", "Year", "Weight Cap", "Qty Cap", "Status"};
        vehicleTableModel = new DefaultTableModel(columns, 0);
        vehicleTable = new JTable(vehicleTableModel);
        add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Assign Vehicle to Route"));

        txtStartTime = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        txtEndTime = new JTextField(LocalDateTime.now().plusHours(8).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        txtRoute = new JTextField();
        txtWeight = new JTextField();
        txtQuantity = new JTextField();

        formPanel.add(new JLabel("Start Time (YYYY-MM-DDTHH:MM):"));
        formPanel.add(txtStartTime);
        formPanel.add(new JLabel("End Time (YYYY-MM-DDTHH:MM):"));
        formPanel.add(txtEndTime);

        formPanel.add(new JLabel("Route Description:"));
        formPanel.add(txtRoute);
        formPanel.add(new JLabel("Total Weight (kg):"));
        formPanel.add(txtWeight);

        formPanel.add(new JLabel("Total Packages:"));
        formPanel.add(txtQuantity);

        btnAssign = new JButton("Assign Vehicle");
        btnRefresh = new JButton("Refresh Vehicles");
        btnViewSchedules = new JButton("View Vehicle Schedules");

        formPanel.add(btnAssign);
        formPanel.add(btnRefresh);
        formPanel.add(btnViewSchedules);

        add(formPanel, BorderLayout.SOUTH);

        // --- Event Handlers ---
        btnAssign.addActionListener(e -> assignVehicle());
        btnRefresh.addActionListener(e -> refreshVehicleTable());
        btnViewSchedules.addActionListener(e -> viewSchedules());

        refreshVehicleTable();
        setVisible(true);
    }

    // --- Refresh vehicle table from server ---
    private void refreshVehicleTable() {
        vehicleTableModel.setRowCount(0);

        new Thread(() -> {
            try {
                ClientConnection conn = new ClientConnection();
                conn.sendFunciton(new ClientRequest("GET_VEHICLES", null));
                ServerResponse response = conn.receive();
                conn.close();

                if (response.isSuccess()) {
                    List<Vehicle> vehicles = (List<Vehicle>) response.getData();
                    SwingUtilities.invokeLater(() -> {
                        for (Vehicle v : vehicles) {
                            vehicleTableModel.addRow(new Object[]{
                                    v.getVehicleID(), v.getBrand(), v.getModel(), v.getYear(),
                                    v.getWeightCapacity(), v.getQuantityCapacity(), v.getStatus()
                            });
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error loading vehicles: " + e.getMessage()));
            }
        }).start();
    }

    // --- Assign vehicle via server ---
    private void assignVehicle() {
        int row = vehicleTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle first.");
            return;
        }

        try {
            int vehicleID = (int) vehicleTableModel.getValueAt(row, 0);
            double totalWeight = Double.parseDouble(txtWeight.getText().trim());
            int totalPackages = Integer.parseInt(txtQuantity.getText().trim());
            String route = txtRoute.getText().trim();

            LocalDateTime start = LocalDateTime.parse(txtStartTime.getText().trim());
            LocalDateTime end = LocalDateTime.parse(txtEndTime.getText().trim());

            VehicleAssignmentRequest requestData = new VehicleAssignmentRequest(
                    vehicleID, start, end, route, totalPackages, totalWeight
            );

            new Thread(() -> {
                try {
                    ClientConnection conn = new ClientConnection();
                    conn.sendFunciton(new ClientRequest("ASSIGN_VEHICLE", requestData));
                    ServerResponse response = conn.receive();
                    conn.close();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, response.getMessage());
                        refreshVehicleTable();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
                }
            }).start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }

    // --- View schedules for selected vehicle ---
    private void viewSchedules() {
        int row = vehicleTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle first.");
            return;
        }

        int vehicleID = (int) vehicleTableModel.getValueAt(row, 0);

        new Thread(() -> {
            try {
                ClientConnection conn = new ClientConnection();
                conn.sendFunciton(new ClientRequest("GET_SCHEDULES", vehicleID));
                ServerResponse response = conn.receive();
                conn.close();

                if (response.isSuccess()) {
                    List<String> schedules = (List<String>) response.getData();
                    SwingUtilities.invokeLater(() -> {
                        JTextArea area = new JTextArea(String.join("\n", schedules));
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        scroll.setPreferredSize(new Dimension(600, 400));
                        JOptionPane.showMessageDialog(this, scroll, "Vehicle Schedules", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, response.getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleSchedulingGUI::new);
    }
}