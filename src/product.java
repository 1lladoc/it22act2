
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uxer
 */
public class product {
    conn con = new conn();
    public int addProduct(String product_name, int quantity, Object price){
        int r = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
            
            String sql = "insert into products values(null,?,?,?);";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            
            String np = price.toString();
            float newprice = Float.parseFloat(np);
                    
            pstmt.setString(1, product_name);
            pstmt.setInt(2, quantity);
            pstmt.setFloat(3, newprice);
            
            r = pstmt.executeUpdate();
            //System.out.println(pstmt);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
    public int deleteProduct(Object id){
        int r = 0;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
            
            String sql = "DELETE FROM products WHERE id = ?;";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            
            int newid = Integer.parseInt(id.toString());
            pstmt.setInt(1, newid);
            
            r = pstmt.executeUpdate();
            //System.out.println(pstmt);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return r;
    }

    public int editProduct(Object id, String product_name, Object price){
        int r = 0;
        try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
        
        String sql = "UPDATE products SET product_name = ?, price = ? WHERE id = ?;";
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        
        pstmt.setString(1, product_name);
        float newprice = Float.parseFloat(price.toString());
        pstmt.setFloat(2, newprice);
        String newid = (String) id;
        pstmt.setString(3, newid);
        
        //System.out.println(pstmt);
        r = pstmt.executeUpdate();
        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public int addQuantity(Object id, Object quantity){
        int r = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
        
            String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?;";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        
            int newqty = Integer.parseInt(quantity.toString());
            pstmt.setInt(1, newqty);
            String newid = id.toString();
            pstmt.setString(2, newid);
            
            r = pstmt.executeUpdate();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
