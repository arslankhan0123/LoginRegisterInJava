import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashboardForm(JFrame parent) {
//        super(parent);
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(450, 474));
        setSize(1200, 700);
//        setModal(true);
//        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        setVisible(true);
        boolean hasRegisteredUser = connectToDatabase();
        if (hasRegisteredUser) {
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;
            if (user != null) {
                lbAdmin.setText("user: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;
            if (user != null) {
                lbAdmin.setText("user: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "RegisteredS",
                            "Try again",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUser = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            // First connect to the database and create database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            statement.close();
            conn.close();

            // Second connect to the database and create table users if not created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            // check if we have users in a table
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                int numUsers =  resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegisteredUser =  true;
                }
            }

            statement.close();
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return hasRegisteredUser;
    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm(null);
    }
}
