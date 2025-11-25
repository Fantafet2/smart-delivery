package clerk;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import clientHandler.ClientConnection;
import shared.ClientRequest;
import shared.ServerResponse;
import shared.SharedCustomer;

public class shipmentRequest extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable requestTable;

    private final JButton btnPending;
    private final JButton btnAssigned;
    private final JButton btnDelivered;
    private final JButton btnAll;

    private final JComboBox<String> zoneCombo;
    private final JButton btnFilterZone;

    public shipmentRequest() {
        super("Shipment Requests");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLayout(new BorderLayout(10, 10));

        // ---------- TITLE ----------
        JLabel titleLabel = new JLabel("Shipment Requests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        String[] columns = {
                "Req ID", "Sender", "Receiver", "Address",
                "Drop Zone", "Pickup Zone", "Weight", "Dimensions", "Option"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        requestTable = new JTable(tableModel);
        requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        requestTable.getColumnModel().getColumn(0).setCellRenderer(center);
        requestTable.getColumnModel().getColumn(4).setCellRenderer(center);
        requestTable.getColumnModel().getColumn(5).setCellRenderer(center);
        requestTable.getColumnModel().getColumn(6).setCellRenderer(center);

        add(new JScrollPane(requestTable), BorderLayout.CENTER);

        // ---------- ZONE FILTER ----------
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel zonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        zoneCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        btnFilterZone = new JButton("Get Zone");

        zonePanel.add(new JLabel("Filter by Zone:"));
        zonePanel.add(zoneCombo);
        zonePanel.add(btnFilterZone);

        JLabel dateLabel = new JLabel("Date Created: " + LocalDate.now());
        dateLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 12));

        topPanel.add(zonePanel, BorderLayout.WEST);
        topPanel.add(dateLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ---------- BUTTONS ----------
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 10));

        btnPending = new JButton("Pending");
        btnAssigned = new JButton("Assigned");
        btnDelivered = new JButton("Delivered");
        btnAll = new JButton("All");

        bottomPanel.add(btnPending);
        bottomPanel.add(btnAssigned);
        bottomPanel.add(btnDelivered);
        bottomPanel.add(btnAll);

        add(bottomPanel, BorderLayout.SOUTH);

        // =====================================================
        // ACTION LISTENERS — your required format
        // =====================================================

        btnPending.addActionListener(e -> fetchShipments("GET_PENDING_SHIPMENTS", "Pending"));
        btnAssigned.addActionListener(e -> fetchShipments("GET_ASSIGNED_SHIPMENTS", "Assigned"));
        btnDelivered.addActionListener(e -> fetchShipments("GET_DELIVERED_SHIPMENTS", "Delivered"));
        btnAll.addActionListener(e -> fetchShipments("GET_ALL_SHIPMENTS", "All"));

        /*btnFilterZone.addActionListener(e -> {
            int zone = Integer.parseInt(zoneCombo.getSelectedItem().toString());
            fetchShipments("GET_SHIPMENTS_BY_ZONE", zone);
        });*/

        // ---------- DOUBLE CLICK ----------
        requestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = requestTable.getSelectedRow();
                    if (row >= 0) showRowDetails(row);
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void fetchShipments(String action, String param) {
        new Thread(() -> {
            try {
                ClientConnection conn = new ClientConnection();

                ClientRequest request = new ClientRequest(action, param);
                conn.sendFunciton(request);

                ServerResponse response = conn.receive();
                conn.close();

                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccess()) {
                        List<?> list = (List<?>) response.getData();
                        tableModel.setRowCount(0);

                        if (list != null) {
                            for (Object o : list) {
                                SharedCustomer sc = (SharedCustomer) o;

                                tableModel.addRow(new Object[]{
                                        sc.reqID,
                                        sc.senderName,
                                        sc.receivername,
                                        sc.address,
                                        sc.dropoffZone,
                                        sc.pickupZone,
                                        sc.weight,
                                        sc.demessions,
                                        sc.deliveryOption
                                });
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                response.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });

            } catch (Exception ex) {
                handleException(ex);
            }
        }).start();
    }

    
    private void handleException(Exception ex) {
        ex.printStackTrace();
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    "Error Type: " + ex.getClass().getName() +
                            "\nDetails: " + (ex.getMessage() == null ? "No details" : ex.getMessage()),
                    "Communication Error",
                    JOptionPane.ERROR_MESSAGE
            );
        });
    }

   
    private void showRowDetails(int row) {

        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < tableModel.getColumnCount(); c++) {
            sb.append(tableModel.getColumnName(c))
                    .append(": ")
                    .append(tableModel.getValueAt(row, c))
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Shipment Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
