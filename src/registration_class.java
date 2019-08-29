
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
public class registration_class {
    
    conn con = new conn();
    
    public int register(String username, String password, String firstname, String lastname){
    //String sql = "insert into users values(null,'"+username+"',md5('"+password+"'),'"+lastname+"','"+firstname+"',0)";    
    //System.out.println(sql);
    int x = 0;
    
    try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = (Connection) DriverManager.getConnection(con.url, con.username, con.password);
        String sql = "insert into users values(null,?,md5(?),?,?,0)";
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, lastname);
        pstmt.setString(4, firstname);
        
        x = pstmt.executeUpdate();
        
        //System.out.println(x);
    }   catch (ClassNotFoundException ex) {
            Logger.getLogger(registration_class.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(registration_class.class.getName()).log(Level.SEVERE, null, ex);
        }
    return x;
    }
    
    public int confirmPassword(String password, String confirmpassword){
        int x = 0;
        
        if(password.equals(confirmpassword)){
            x = 1;
        }else{
            x = 0;
        }
        return x;
    }
    
    public int checkUsername(String username){
        int x = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url, con.username, con.password);
        
            String sql = "SELECT username FROM users WHERE username = ?;";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                x = 1;
            }else{
                x = 0;
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(registration_class.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(registration_class.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return x;
    }
    
}
