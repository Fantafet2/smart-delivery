package vehicle;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import database.VehicleDAO;

public class AssignmentService {
	private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();

    public void assignVehicleToRoute(int vehicleID, LocalDateTime start, LocalDateTime end, String route,
                                     int totalPackages, double totalWeight) throws SQLException {

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleID);
        if (vehicle == null)
            throw new IllegalArgumentException("Vehicle not found!");

        if (totalPackages > vehicle.getQuantityCapacity())
            throw new IllegalStateException("Package quantity exceeds vehicle capacity.");

        if (totalWeight > vehicle.getWeightCapacity())
            throw new IllegalStateException("Total weight exceeds vehicle capacity.");

        if (scheduleDAO.hasOverlap(vehicleID, start, end))
            throw new IllegalStateException("Vehicle already has a schedule during this time!");

        scheduleDAO.createSchedule(vehicleID, start, end, route);
        vehicleDAO.updateVehicleStatus(vehicleID, "In Use");
        System.out.println("Vehicle successfully assigned to route: " + route);
    }

}