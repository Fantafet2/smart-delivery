package main;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedGetInfo {

	/**
     * Collects data from a list of JTextFields and JComboBoxes.
     * @param components A list of the input components to process.
     * @return A Map where keys are component identifiers and values are the user inputs.
     */
    public static Map<String, String> collectInput(List<JComponent> components) {
        Map<String, String> data = new HashMap<>();

        for (JComponent component : components) {
            String componentId = component.getName(); // Crucial for identification!
            
            if (componentId == null || componentId.isEmpty()) {
                // Skip components without an ID, or assign a default/error ID
                System.err.println("Warning: Component without name found and skipped.");
                continue; 
            }
            
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                data.put(componentId, textField.getText());

            } else if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                Object selectedItem = comboBox.getSelectedItem();
                
                // Add the selected item, converting to String (or handling null)
                data.put(componentId, selectedItem != null ? selectedItem.toString() : "");
            }
            // Add other component types here (e.g., JCheckBox) if needed
        }
        return data;
    }
}
