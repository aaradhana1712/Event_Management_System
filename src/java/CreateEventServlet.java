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
 * Servlet for creating a new event.
 */
public class CreateEventServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String eventNumber = request.getParameter("eventNumber");
        String eventName = request.getParameter("eventName");
        String coordinator = request.getParameter("coordinator");
        String coordinatorContact = request.getParameter("coordinatorContact");
        String fee = request.getParameter("fee");
        String venue = request.getParameter("venue");
        String date = request.getParameter("date");

        Connection con = null;
        PreparedStatement pst = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost:3306/EventlyDB";
            String dbUsername = "root";
            String dbUserPassword = "abc";
            con = DriverManager.getConnection(conURL, dbUsername, dbUserPassword);

            String query = "INSERT INTO Event (Event_Number, Event_Name, Coordinator_Name, Coordinator_Number, Fee, Venue, Date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, eventNumber);
            pst.setString(2, eventName);
            pst.setString(3, coordinator);
            pst.setString(4, coordinatorContact);
            pst.setString(5, fee);
            pst.setString(6, venue);
            pst.setString(7, date);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<h1>Event Created Successfully!</h1>");
            } else {
                out.println("<h1>Failed to Create Event</h1>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
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
