package com.example.scanbarcode.model;

import android.util.Log;

import com.example.scanbarcode.JDBC.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductModel {
    private JDBCController jdbcController = new JDBCController();
    private Connection connection;

    public ProductModel() {
        connection = jdbcController.ConnnectionData(); // Tạo kết nối tới database
    }

    public ArrayList<Product> getuserlist() throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select * from PRODUCT";
        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        // Mọi kết quả trả về sẽ được lưu trong ResultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Product(rs.getString("id"), rs.getString("name"),rs.getInt("amount"),rs.getString("urlimg"), rs.getString("barcode")));// Đọc dữ liệu từ ResultSet
        }
        connection.close();// Đóng kết nối
        return list;
    }

    public boolean Insert(Product objUser) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "insert into PRODUCT values(" + objUser.getId() + objUser.getName() + objUser.getAmount() + objUser.getUrlImg() + objUser.getBarCode()+ ")";
        if (statement.executeUpdate(sql) > 0) { // Dùng lệnh executeUpdate cho các lệnh CRUD
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }

    public boolean UpdateAmount(int amount, String id) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "UPDATE PRODUCT SET " +
                " amount = "+ amount +
                " WHERE id = " + id.trim();
        Log.i("sql", sql);
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean UpdateBarcode(String barcode, String id) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "UPDATE PRODUCT SET " +
                " barcode = "+ barcode +
                " WHERE id = " + id.trim();
        Log.i("sql", sql);
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Product objUser) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "delete from PRODUCT where id = " + objUser.getId();
        if (statement.executeUpdate(sql) > 0){
            connection.close();
            return true;
        }

        else
            connection.close();
        return false;
    }

    public String SelectPassword(String user) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "SELECT password FROM ACCOUNT WHERE username = '"+user+"'";
        ResultSet rs = statement.executeQuery(sql);
        byte[] password = new byte[1];
        while(rs.next()){
            //Display values
            return rs.getString("password");
        }
        rs.close();
        return null;
    }

    public boolean InsertAccount(String username,String password) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "INSERT INTO ACCOUNT VALUES ('" + username + "','" + password + "')";
        if (statement.executeUpdate(sql) > 0) { // Dùng lệnh executeUpdate cho các lệnh CRUD
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }


}
