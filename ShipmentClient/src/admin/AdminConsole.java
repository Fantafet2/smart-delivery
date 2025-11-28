package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

class Shipment { public int id; public String trackingNumber; public String status; public double weight; public int zone; public double cost; public String customerName; }
class Vehicle { public int id; public String plate; public int quantityCapacity; public double weightCapacity; public String status; }
class Schedule { public int id; public int vehicleId; public LocalDateTime start; public LocalDateTime end; public String route; }
class Invoice { public int id; public int shipmentId; public double amount; public String status; }

// DAO interfaces 
interface ShipmentDAO { List<Shipment> getAllShipments(); Shipment getShipmentById(int id); void updateShipment(Shipment s); void deleteShipment(int id); }
interface VehicleDAO { List<Vehicle> getAllVehicles(); Vehicle getVehicleById(int id); void updateVehicle(Vehicle v); void deleteVehicle(int id); }
interface ScheduleDAO { List<Schedule> getAllSchedules(); Schedule getScheduleById(int id); void createSchedule(Schedule s); void updateSchedule(Schedule s); void deleteSchedule(int id); boolean hasOverlap(int vehicleId, LocalDateTime start, LocalDateTime end); }
interface InvoiceDAO { List<Invoice> getAllInvoices(); Invoice getInvoiceById(int id); void updateInvoice(Invoice i); }

public class AdminConsole extends JFrame {

    private final ShipmentDAO shipmentDAO;
    private final VehicleDAO vehicleDAO;
    private final ScheduleDAO scheduleDAO;
    private final InvoiceDAO invoiceDAO;

    private final JTabbedPane tabs = new JTabbedPane();

    private final DefaultTableModel shipmentModel = new DefaultTableModel();
    private final DefaultTableModel vehicleModel = new DefaultTableModel();
    private final DefaultTableModel scheduleModel = new DefaultTableModel();
    private final DefaultTableModel invoiceModel = new DefaultTableModel();

    public AdminConsole(ShipmentDAO shipmentDAO, VehicleDAO vehicleDAO, ScheduleDAO scheduleDAO, InvoiceDAO invoiceDAO) {
        super("SmartShip - Admin Console");

        this.shipmentDAO = shipmentDAO;
        this.vehicleDAO = vehicleDAO;
        this.scheduleDAO = scheduleDAO;
        this.invoiceDAO = invoiceDAO;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        initUI();
        loadAllData();
    }

    private void initUI() {

        // Shipments tab
        JPanel shipmentPanel = new JPanel(new BorderLayout());
        JTable shipmentTable = new JTable(shipmentModel);
        shipmentModel.setColumnIdentifiers(new String[]{"ID", "Tracking#", "Status", "Weight", "Zone", "Cost", "Customer"});
        shipmentPanel.add(new JScrollPane(shipmentTable), BorderLayout.CENTER);
        shipmentPanel.add(createEntityToolbar(() -> refreshShipments(), () -> editSelectedShipment(shipmentTable), () -> deleteSelectedShipment(shipmentTable)), BorderLayout.NORTH);

        // Vehicles tab
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        JTable vehicleTable = new JTable(vehicleModel);
        vehicleModel.setColumnIdentifiers(new String[]{"ID", "Plate", "QtyCap", "WeightCap", "Status"});
        vehiclePanel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);
        vehiclePanel.add(createEntityToolbar(() -> refreshVehicles(), () -> editSelectedVehicle(vehicleTable), () -> deleteSelectedVehicle(vehicleTable)), BorderLayout.NORTH);

        // Schedules tab
        JPanel schedulePanel = new JPanel(new BorderLayout());
        JTable scheduleTable = new JTable(scheduleModel);
        scheduleModel.setColumnIdentifiers(new String[]{"ID", "VehicleID", "Start", "End", "Route"});
        schedulePanel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        schedulePanel.add(createEntityToolbar(() -> refreshSchedules(), () -> editSelectedSchedule(scheduleTable), () -> deleteSelectedSchedule(scheduleTable)), BorderLayout.NORTH);

        // Invoices tab
        JPanel invoicePanel = new JPanel(new BorderLayout());
        JTable invoiceTable = new JTable(invoiceModel);
        invoiceModel.setColumnIdentifiers(new String[]{"ID", "ShipmentID", "Amount", "Status"});
        invoicePanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);
        invoicePanel.add(createEntityToolbar(() -> refreshInvoices(), () -> editSelectedInvoice(invoiceTable), null), BorderLayout.NORTH);

        // Reports tab
        JPanel reportPanel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        JButton genReport = new JButton("Generate Simple Summary Report");
        genReport.addActionListener(e -> SwingUtilities.invokeLater(() -> reportArea.setText(generateSummaryReport())));
        reportPanel.add(genReport, BorderLayout.NORTH);

        
        tabs.addTab("Shipments", shipmentPanel);
        tabs.addTab("Vehicles", vehiclePanel);
        tabs.addTab("Schedules", schedulePanel);
        tabs.addTab("Invoices", invoicePanel);
        tabs.addTab("Reports", reportPanel);

        add(tabs);
    }

    private JToolBar createEntityToolbar(Runnable refreshAction, Runnable editAction, Runnable deleteAction) {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshAction.run());
        tb.add(refresh);

        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> editAction.run());
        tb.add(edit);

        if (deleteAction != null) {
            JButton del = new JButton("Delete");
            del.addActionListener(e -> deleteAction.run());
            tb.add(del);
        }

        return tb;
    }

    //Refresh methods 
    private void loadAllData() {
        refreshShipments();
        refreshVehicles();
        refreshSchedules();
        refreshInvoices();
    }

    private void refreshShipments() {
        new SwingWorker<List<Shipment>, Void>() {
            protected List<Shipment> doInBackground() { return shipmentDAO.getAllShipments(); }
            protected void done() {
                try {
                    List<Shipment> rows = get();
                    shipmentModel.setRowCount(0);
                    for (Shipment s : rows)
                        shipmentModel.addRow(new Object[]{s.id, s.trackingNumber, s.status, s.weight, s.zone, s.cost, s.customerName});
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void refreshVehicles() {
        new SwingWorker<List<Vehicle>, Void>() {
            protected List<Vehicle> doInBackground() { return vehicleDAO.getAllVehicles(); }
            protected void done() {
                try {
                    List<Vehicle> rows = get();
                    vehicleModel.setRowCount(0);
                    for (Vehicle v : rows)
                        vehicleModel.addRow(new Object[]{v.id, v.plate, v.quantityCapacity, v.weightCapacity, v.status});
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void refreshSchedules() {
        new SwingWorker<List<Schedule>, Void>() {
            protected List<Schedule> doInBackground() { return scheduleDAO.getAllSchedules(); }
            protected void done() {
                try {
                    List<Schedule> rows = get();
                    scheduleModel.setRowCount(0);
                    for (Schedule s : rows)
                        scheduleModel.addRow(new Object[]{s.id, s.vehicleId, s.start, s.end, s.route});
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void refreshInvoices() {
        new SwingWorker<List<Invoice>, Void>() {
            protected List<Invoice> doInBackground() { return invoiceDAO.getAllInvoices(); }
            protected void done() {
                try {
                    List<Invoice> rows = get();
                    invoiceModel.setRowCount(0);
                    for (Invoice i : rows)
                        invoiceModel.addRow(new Object[]{i.id, i.shipmentId, i.amount, i.status});
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    // ---------- Edit/Delete ----------
    private void editSelectedShipment(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a shipment to edit."); return; }
        int id = (int) table.getValueAt(r, 0);

        new SwingWorker<Shipment, Void>() {
            protected Shipment doInBackground() { return shipmentDAO.getShipmentById(id); }
            protected void done() {
                try {
                    Shipment s = get();
                    if (s == null) { showError(new Exception("Shipment not found")); return; }
                    Shipment edited = showShipmentEditor(s);
                    if (edited != null) { shipmentDAO.updateShipment(edited); refreshShipments(); }
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private Shipment showShipmentEditor(Shipment s) {
        JTextField status = new JTextField(s.status);
        JTextField weight = new JTextField(String.valueOf(s.weight));
        JTextField zone = new JTextField(String.valueOf(s.zone));
        JTextField cost = new JTextField(String.valueOf(s.cost));

        Object[] fields = {"Status", status, "Weight", weight, "Zone", zone, "Cost", cost};

        int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Shipment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            s.status = status.getText().trim();
            s.weight = Double.parseDouble(weight.getText().trim());
            s.zone = Integer.parseInt(zone.getText().trim());
            s.cost = Double.parseDouble(cost.getText().trim());
            return s;
        }
        return null;
    }

    private void deleteSelectedShipment(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a shipment to delete."); return; }
        int id = (int) table.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete shipment ID=" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        new SwingWorker<Void, Void>() {
            protected Void doInBackground() { shipmentDAO.deleteShipment(id); return null; }
            protected void done() { refreshShipments(); }
        }.execute();
    }

    private void editSelectedVehicle(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a vehicle to edit."); return; }
        int id = (int) table.getValueAt(r, 0);

        new SwingWorker<Vehicle, Void>() {
            protected Vehicle doInBackground() { return vehicleDAO.getVehicleById(id); }
            protected void done() {
                try {
                    Vehicle v = get();
                    if (v == null) { showError(new Exception("Vehicle not found")); return; }
                    Vehicle edited = showVehicleEditor(v);
                    if (edited != null) { vehicleDAO.updateVehicle(edited); refreshVehicles(); }
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private Vehicle showVehicleEditor(Vehicle v) {
        JTextField plate = new JTextField(v.plate);
        JTextField qty = new JTextField(String.valueOf(v.quantityCapacity));
        JTextField wt = new JTextField(String.valueOf(v.weightCapacity));
        JTextField status = new JTextField(v.status);

        Object[] fields = {"Plate", plate, "Quantity Capacity", qty, "Weight Capacity", wt, "Status", status};

        int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Vehicle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            v.plate = plate.getText().trim();
            v.quantityCapacity = Integer.parseInt(qty.getText().trim());
            v.weightCapacity = Double.parseDouble(wt.getText().trim());
            v.status = status.getText().trim();
            return v;
        }
        return null;
    }

    private void deleteSelectedVehicle(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a vehicle to delete."); return; }
        int id = (int) table.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete vehicle ID=" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        new SwingWorker<Void, Void>() {
            protected Void doInBackground() { vehicleDAO.deleteVehicle(id); return null; }
            protected void done() { refreshVehicles(); }
        }.execute();
    }

    private void editSelectedSchedule(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a schedule to edit."); return; }
        int id = (int) table.getValueAt(r, 0);

        new SwingWorker<Schedule, Void>() {
            protected Schedule doInBackground() { return scheduleDAO.getScheduleById(id); }
            protected void done() {
                try {
                    Schedule s = get();
                    if (s == null) { showError(new Exception("Schedule not found")); return; }
                    Schedule edited = showScheduleEditor(s);

                    if (edited != null) {
                        if (scheduleDAO.hasOverlap(edited.vehicleId, edited.start, edited.end)) {
                            showError(new Exception("Schedule overlaps existing schedule for this vehicle."));
                        } else {
                            scheduleDAO.updateSchedule(edited);
                            refreshSchedules();
                        }
                    }
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private Schedule showScheduleEditor(Schedule s) {
        JTextField vehicleId = new JTextField(String.valueOf(s.vehicleId));
        JTextField start = new JTextField(s.start.toString());
        JTextField end = new JTextField(s.end.toString());
        JTextField route = new JTextField(s.route);

        Object[] fields = {
                "Vehicle ID", vehicleId,
                "Start (YYYY-MM-DDTHH:MM)", start,
                "End (YYYY-MM-DDTHH:MM)", end,
                "Route", route
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Schedule", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            s.vehicleId = Integer.parseInt(vehicleId.getText().trim());
            s.start = LocalDateTime.parse(start.getText().trim());
            s.end = LocalDateTime.parse(end.getText().trim());
            s.route = route.getText().trim();
            return s;
        }
        return null;
    }

    private void deleteSelectedSchedule(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select a schedule to delete."); return; }
        int id = (int) table.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete schedule ID=" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        new SwingWorker<Void, Void>() {
            protected Void doInBackground() { scheduleDAO.deleteSchedule(id); return null; }
            protected void done() { refreshSchedules(); }
        }.execute();
    }

    private void editSelectedInvoice(JTable table) {
        int r = table.getSelectedRow();
        if (r < 0) { showInfo("Select an invoice to edit."); return; }
        int id = (int) table.getValueAt(r, 0);

        new SwingWorker<Invoice, Void>() {
            protected Invoice doInBackground() { return invoiceDAO.getInvoiceById(id); }
            protected void done() {
                try {
                    Invoice i = get();
                    if (i == null) { showError(new Exception("Invoice not found")); return; }
                    Invoice edited = showInvoiceEditor(i);
                    if (edited != null) { invoiceDAO.updateInvoice(edited); refreshInvoices(); }
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private Invoice showInvoiceEditor(Invoice i) {
        JTextField amt = new JTextField(String.valueOf(i.amount));
        JTextField status = new JTextField(i.status);

        Object[] fields = {"Amount", amt, "Status (Paid/Partially Paid/Unpaid)", status};

        int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Invoice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            i.amount = Double.parseDouble(amt.getText().trim());
            i.status = status.getText().trim();
            return i;
        }
        return null;
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private String generateSummaryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("SmartShip Summary Report\n");
        sb.append("Generated: ").append(LocalDateTime.now()).append("\n\n");

        try {
            sb.append("Total shipments: ").append(shipmentDAO.getAllShipments().size()).append("\n");
            sb.append("Total vehicles: ").append(vehicleDAO.getAllVehicles().size()).append("\n");
            sb.append("Total schedules: ").append(scheduleDAO.getAllSchedules().size()).append("\n");

            double totalRevenue = invoiceDAO.getAllInvoices().stream().mapToDouble(x -> x.amount).sum();
            sb.append("Total revenue: ").append(String.format("%.2f", totalRevenue)).append("\n");

        } catch (Exception ex) {
            sb.append("Failed to generate some statistics: ").append(ex.getMessage());
        }
        return sb.toString();
    }

    // Main demo
    public static void main(String[] args) {

        ShipmentDAO shipmentDAO = new ShipmentDAO() {
            public List<Shipment> getAllShipments() { return java.util.Collections.emptyList(); }
            public Shipment getShipmentById(int id) { return null; }
            public void updateShipment(Shipment s) {}
            public void deleteShipment(int id) {}
        };

        VehicleDAO vehicleDAO = new VehicleDAO() {
            public List<Vehicle> getAllVehicles() { return java.util.Collections.emptyList(); }
            public Vehicle getVehicleById(int id) { return null; }
            public void updateVehicle(Vehicle v) {}
            public void deleteVehicle(int id) {}
        };

        ScheduleDAO scheduleDAO = new ScheduleDAO() {
            public List<Schedule> getAllSchedules() { return java.util.Collections.emptyList(); }
            public Schedule getScheduleById(int id) { return null; }
            public void createSchedule(Schedule s) {}
            public void updateSchedule(Schedule s) {}
            public void deleteSchedule(int id) {}
            public boolean hasOverlap(int vehicleId, LocalDateTime start, LocalDateTime end) { return false; }
        };

        InvoiceDAO invoiceDAO = new InvoiceDAO() {
            public List<Invoice> getAllInvoices() { return java.util.Collections.emptyList(); }
            public Invoice getInvoiceById(int id) { return null; }
            public void updateInvoice(Invoice i) {}
        };

        SwingUtilities.invokeLater(() -> {
            AdminConsole a = new AdminConsole(shipmentDAO, vehicleDAO, scheduleDAO, invoiceDAO);
            a.setVisible(true);
        });
    }
}
