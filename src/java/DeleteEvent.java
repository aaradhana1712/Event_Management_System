/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hp
 */
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for deleting an event.
 */
public class DeleteEvent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String eventNumber = request.getParameter("eventNumber");
        
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost:3306/EventlyDB";
            String dbUsername = "root";
            String dbUserPassword = "abc";
            con = DriverManager.getConnection(conURL, dbUsername, dbUserPassword);
            
            String query = "DELETE FROM Event WHERE Event_Number = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, eventNumber);
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                out.println("<html><body><h2>Event deleted successfully!</h2>");
            } else {
                out.println("<html><body><h2>No event found with the specified number.</h2>");
            }
            
            out.println("<a href='ListEvents'>Back to Event List</a>");
            out.println("</body></html>");
            
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<html><body><h2>Error deleting event.</h2></body></html>");
            e.printStackTrace(out);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }
}
