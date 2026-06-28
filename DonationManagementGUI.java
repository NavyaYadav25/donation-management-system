import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DonationManagementGUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Donation Management System");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Donation Management System (NGO - Donor)", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 153));
        title.setPreferredSize(new Dimension(100, 50));
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBackground(new Color(230, 240, 250));

        JTextField donorName = new JTextField();
        JTextField donorContact = new JTextField();
        JTextField quantity = new JTextField();
        JTextField itemDetails = new JTextField();

        JLabel itemLabel = new JLabel("What You Want to Donate");

        // Hostel Dropdown
        String[] hostels = {"6S2", "7S2", "7S3", "11S2", "13S1", "13S2", "13S3", "14S1", "15S1"};
        JComboBox<String> hostelBox = new JComboBox<>(hostels);

        // Donation Type
        String[] types = {"Food", "Clothes", "Books", "Others"};
        JComboBox<String> donationType = new JComboBox<>(types);

        // NGO
        JComboBox<String> ngoName = new JComboBox<>();
        ngoName.addItem("Akshaya Patra");
        ngoName.addItem("Smile Foundation");
        ngoName.addItem("HelpAge India");
        ngoName.addItem("CRY India");
        ngoName.addItem("Goonj");

        // Add components
        panel.add(new JLabel("Donor Name"));
        panel.add(donorName);

        panel.add(new JLabel("Contact"));
        panel.add(donorContact);

        panel.add(new JLabel("Hostel"));
        panel.add(hostelBox);

        panel.add(new JLabel("Donation Type"));
        panel.add(donationType);

        panel.add(new JLabel("NGO Name"));
        panel.add(ngoName);

        // Item field (initially hidden)
        panel.add(itemLabel);
        panel.add(itemDetails);
        itemLabel.setVisible(false);
        itemDetails.setVisible(false);

        panel.add(new JLabel("Quantity"));
        panel.add(quantity);

        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");

        submit.setBackground(new Color(0, 153, 76));
        submit.setForeground(Color.WHITE);

        clear.setBackground(new Color(204, 0, 0));
        clear.setForeground(Color.WHITE);

        panel.add(submit);
        panel.add(clear);

        frame.add(panel, BorderLayout.WEST);

        // Table
        String[] columns = {"Name", "Contact", "Hostel", "NGO", "Type", "Item", "Qty"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setBackground(new Color(245, 245, 245));
        JScrollPane scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);

        // Donation Type change logic
        donationType.addActionListener(e -> {
            String type = (String) donationType.getSelectedItem();

            // Show/hide item field
            if (type.equals("Others")) {
                itemLabel.setVisible(true);
                itemDetails.setVisible(true);
            } else {
                itemLabel.setVisible(false);
                itemDetails.setVisible(false);
                itemDetails.setText("");
            }

            // NGO filtering
            ngoName.removeAllItems();
            switch (type) {
                case "Food":
                    ngoName.addItem("Akshaya Patra");
                    ngoName.addItem("Smile Foundation");
                    break;
                case "Books":
                    ngoName.addItem("HelpAge India");
                    ngoName.addItem("CRY India");
                    break;
                case "Clothes":
                    ngoName.addItem("Goonj");
                    ngoName.addItem("HelpAge India");
                    break;
                default:
                    ngoName.addItem("Akshaya Patra");
                    ngoName.addItem("Smile Foundation");
                    ngoName.addItem("HelpAge India");
                    ngoName.addItem("CRY India");
                    ngoName.addItem("Goonj");
            }

            panel.revalidate();
            panel.repaint();
        });

        // Submit
        submit.addActionListener(e -> {
            String name = donorName.getText();
            String contact = donorContact.getText();
            String hostel = (String) hostelBox.getSelectedItem();
            String ngo = (String) ngoName.getSelectedItem();
            String type = (String) donationType.getSelectedItem();
            String item = itemDetails.getText();
            String qtyStr = quantity.getText();

            if (name.isEmpty() || contact.isEmpty() || qtyStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all details");
                return;
            }

            if (type.equals("Others") && item.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please specify item for Others");
                return;
            }

            int qty = Integer.parseInt(qtyStr);

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");

                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:XE",
                        "sys as sysdba",
                        "123");

                con.setAutoCommit(false);

                int donorId = (int)(Math.random() * 1000);
                PreparedStatement ps1 = con.prepareStatement(
                        "INSERT INTO Donor VALUES(?, ?, ?, ?)");
                ps1.setInt(1, donorId);
                ps1.setString(2, name);
                ps1.setString(3, contact);
                ps1.setString(4, hostel);
                ps1.executeUpdate();

                int ngoId = switch (ngo) {
                    case "Akshaya Patra" -> 1;
                    case "Smile Foundation" -> 2;
                    case "HelpAge India" -> 3;
                    case "CRY India" -> 4;
                    case "Goonj" -> 5;
                    default -> 0;
                };

                int typeId = switch (type) {
                    case "Food" -> 1;
                    case "Clothes" -> 2;
                    case "Books" -> 3;
                    default -> 4;
                };

                int donationId = (int)(Math.random() * 1000);
                PreparedStatement ps2 = con.prepareStatement(
                        "INSERT INTO Donation VALUES(?, ?, ?, ?, ?)");
                ps2.setInt(1, donationId);
                ps2.setInt(2, donorId);
                ps2.setInt(3, ngoId);
                ps2.setInt(4, typeId);
                ps2.setString(5, type.equals("Others") ? item : "N/A");
                ps2.executeUpdate();

                int logId = (int)(Math.random() * 1000);
                PreparedStatement ps3 = con.prepareStatement(
                        "INSERT INTO Quantity_Log VALUES(?, ?, ?)");
                ps3.setInt(1, logId);
                ps3.setInt(2, donationId);
                ps3.setInt(3, qty);
                ps3.executeUpdate();

                con.commit();
                con.close();

                model.addRow(new Object[]{
                        name, contact, hostel, ngo, type,
                        type.equals("Others") ? item : "N/A", qty
                });

                JOptionPane.showMessageDialog(frame, "Donation Submitted Successfully");

                donorName.setText("");
                donorContact.setText("");
                itemDetails.setText("");
                quantity.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Clear
        clear.addActionListener(e -> {
            donorName.setText("");
            donorContact.setText("");
            itemDetails.setText("");
            quantity.setText("");
        });

        frame.setVisible(true);
    }
}