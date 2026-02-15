package com.birthday;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class BirthdayController {

    @FXML private TextField nameField, searchField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TableView<Classmate> table;
    @FXML private TableColumn<Classmate, Integer> idColumn;
    @FXML private TableColumn<Classmate, String> nameColumn;
    @FXML private TableColumn<Classmate, LocalDate> dateColumn;
    @FXML private Label notificationLabel;

    private ObservableList<Classmate> list = FXCollections.observableArrayList();

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));

        loadData();
        showTodayNotification();
    }

    @FXML
    private void addBirthday() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO classmates(name, birthdate) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setDate(2, Date.valueOf(birthDatePicker.getValue()));
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        loadData();
    }

    @FXML
    private void deleteBirthday() {
        Classmate selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM classmates WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, selected.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        loadData();
    }

    @FXML
    private void updateBirthday() {
        Classmate selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE classmates SET name=?, birthdate=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setDate(2, Date.valueOf(birthDatePicker.getValue()));
            ps.setInt(3, selected.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        loadData();
    }

    @FXML
    private void search() {
        list.clear();
        String keyword = searchField.getText();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM classmates WHERE name LIKE ? OR MONTH(birthdate)=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            try {
                ps.setInt(2, Integer.parseInt(keyword));
            } catch (Exception e) {
                ps.setInt(2, 0);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Classmate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birthdate").toLocalDate()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }

        table.setItems(list);
    }

    private void loadData() {
        list.clear();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM classmates ORDER BY MONTH(birthdate), DAY(birthdate)";
            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(new Classmate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birthdate").toLocalDate()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }

        table.setItems(list);
    }

    private void showTodayNotification() {
        LocalDate today = LocalDate.now();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT name FROM classmates WHERE MONTH(birthdate)=? AND DAY(birthdate)=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, today.getMonthValue());
            ps.setInt(2, today.getDayOfMonth());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                notificationLabel.setText("🎉 Today is " + rs.getString("name") + "'s Birthday!");
            } else {
                notificationLabel.setText("No birthdays today.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
