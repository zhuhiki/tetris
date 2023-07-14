package demo1;

import demo1.Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginRegisterGUI extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;



    public LoginRegisterGUI() {
        setTitle("登录注册");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("宋体",Font.BOLD,24));
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("宋体",Font.BOLD,24));
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("宋体",Font.BOLD,24));
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("宋体",Font.BOLD,24));
        panel.add(passwordField);

        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("宋体",Font.BOLD,24));
        loginButton.addActionListener(this);
        panel.add(loginButton);

        JButton registerButton = new JButton("注册");
        registerButton.setFont(new Font("宋体",Font.BOLD,24));
        registerButton.addActionListener(this);
        panel.add(registerButton);

        JLabel use2 = new JLabel();
        usernameLabel.setFont(new Font("宋体",Font.BOLD,24));
        panel.add(use2);
        add(panel);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (e.getActionCommand().equals("登录")) {
            boolean success = login(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "登录成功！点击确认开始游戏");
                Tetris.opengame();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败，请检查用户名和密码！");
            }
        } else if (e.getActionCommand().equals("注册")) {
            boolean success = register(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "注册成功！");
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，该用户名已被使用！");
            }
        }

    }
    private boolean login(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/python_homework?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            String dbUsername = "root";
            String dbPassword = "Root";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM users WHERE name='" + username + "' AND pwd='" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean register(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/python_homework?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            String dbUsername = "root";
            String dbPassword = "Root";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM users WHERE name='" + username + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return false;
            } else {
                query = "INSERT INTO users (name, pwd) VALUES ('" + username + "', '" + password + "')";
                statement.executeUpdate(query);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}