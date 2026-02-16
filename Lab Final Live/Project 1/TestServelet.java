import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceClass extends DBConnection {

    public boolean insertDB(String name, int numberOfStudents) {
        this.getConnection();
        String sql = "INSERT INTO MYDEPARTMENT(NAME, NUMBER_OF_STUDENTS) VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, numberOfStudents);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return false;
    }

    public List<String> viewDB() {
        List<String> result = new ArrayList<>();
        this.getConnection();
        String sql = "SELECT NAME, NUMBER_OF_STUDENTS FROM MYDEPARTMENT";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add("Name: " + rs.getString("NAME") + ", Number of Students: " + rs.getInt("NUMBER_OF_STUDENTS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return result;
    }

    public boolean updateDB(String name, int numberOfStudents) {
        this.getConnection();
        String sql = "UPDATE MYDEPARTMENT SET NUMBER_OF_STUDENTS = ? WHERE NAME = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numberOfStudents);
            ps.setString(2, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return false;
    }

    public boolean deleteDB(String name) {
        this.getConnection();
        String sql = "DELETE FROM MYDEPARTMENT WHERE NAME = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return false;
    }
}
