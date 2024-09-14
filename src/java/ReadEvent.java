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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for reading event details.
 */
public class ReadEvent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String eventNumber = request.getParameter("eventNumber");
        
        out.println("<html>");
        out.println("<head><title>Event Details</title></head>");
        out.println("<body>");
        out.println("<h1>Event Details</h1>");
        
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost:3306/EventlyDB";
            String dbUsername = "root";
            String dbUserPassword = "abc";
            con = DriverManager.getConnection(conURL, dbUsername, dbUserPassword);
            
            String query = "SELECT * FROM Event WHERE Event_Number = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, eventNumber);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                String eventName = rs.getString("Event_Name");
                String coordinator = rs.getString("Coordinator_Name");
                String coordinatorContact = rs.getString("Coordinator_Number");
                String fee = rs.getString("Fee");
                String venue = rs.getString("Venue");
                String date = rs.getString("Date");
                
                out.println("<p>Event Number: " + eventNumber + "</p>");
                out.println("<p>Event Name: " + eventName + "</p>");
                out.println("<p>Coordinator: " + coordinator + "</p>");
                out.println("<p>Coordinator Contact: " + coordinatorContact + "</p>");
                out.println("<p>Fee: " + fee + "</p>");
                out.println("<p>Venue: " + venue + "</p>");
                out.println("<p>Date: " + date + "</p>");
            } else {
                out.println("<p>No event found with the specified number.</p>");
            }
            
            out.println("<a href='ListEvents'>Back to Event List</a>");
            out.println("</body></html>");
            
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<html><body><h2>Error retrieving event details.</h2></body></html>");
            e.printStackTrace(out);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }
}
