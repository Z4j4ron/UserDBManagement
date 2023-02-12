package pl.coderslab.workshop2.UserDBManagement;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinemas_ex?useSSL=false&characterEncoding=utf8&serverTimezone=UTC"; //bazkę warto zmienic :-)
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "coderslab";


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public String hashPassword(String password) {
        User user = new User();
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public User create(User user) {
        try(Connection connect = connect()) {
            PreparedStatement prepStmt = connect.prepareStatement("INSERT INTO user.user(name, password, email) " +
                    "VALUES (?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, user.getName());
            prepStmt.setString(2, hashPassword(user.getPassword()));
            prepStmt.setString(3, user.getEmail());
            prepStmt.executeUpdate();
            ResultSet generatedKeys = prepStmt.getGeneratedKeys();
            generatedKeys.next();
            int newId = generatedKeys.getInt(1);
            user.setId(newId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public User read(int userID) {
        try (Connection connect = connect()){
            PreparedStatement prepStmt = connect.prepareStatement("SELECT * FROM user.user WHERE id = ?");
            prepStmt.setInt(1, userID);
            ResultSet rs = prepStmt.executeQuery();
            User user = new User();
            while(rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
            }
            System.out.println(user.getId() + " " + user.getName() + " " + user.getPassword() + " " + user.getEmail());
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        return null;
    }
    public void update(User user) {
        try (Connection connect = connect()){
            PreparedStatement prepStmt = connect.prepareStatement("UPDATE user.user " +
                                                                "SET name = ?, password = ?, email = ? " +
                                                                "WHERE id = " + user.getId() + ";");
                prepStmt.setString(1, user.getName());
                prepStmt.setString(2, hashPassword(user.getPassword()));
                prepStmt.setString(3, user.getEmail());
                prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public User[] findAll() {
        User[] userArr = new User[0];
        try (Connection connect = connect()){
            PreparedStatement prepStmt = connect.prepareStatement("SELECT * " +
                    "FROM  user.user;");
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                userArr = Arrays.copyOf(userArr, userArr.length + 1);
                userArr[userArr.length - 1] = user;

            }
            System.out.println("userArr = " + Arrays.toString(userArr));
            return userArr;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(int userId) {
        try (Connection connect = connect()){
            PreparedStatement prepStmt = connect.prepareStatement("DELETE FROM user.user WHERE id = ?;");
            prepStmt.setInt(1, userId);
            prepStmt.executeUpdate();
            User user = new User();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
