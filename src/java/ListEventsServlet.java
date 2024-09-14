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
 * Servlet for listing all events.
 */
public class ListEventsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>List of Events</title>");
        out.println("<link rel=\"stylesheet\" href=\"total.css\">");
        out.println("<link href=\"https://fonts.googleapis.com/css2?family=Balsamiq+Sans&display=swap\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost:3306/EventlyDB";
            String dbUsername = "root";
            String dbUserPassword = "Aaru@1712";
            con = DriverManager.getConnection(conURL, dbUsername, dbUserPassword);

            String query = "SELECT * FROM Event";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            out.println("<h1 style=\"text-align: center\">List of Events</h1>");
            out.println("<center>");
            out.println("<table border=\"1\" width=\"80%\">");
            out.println("<tr><th>Event Number</th><th>Event Name</th><th>Coordinator</th><th>Coordinator Contact</th><th>Fees</th><th>Venue</th><th>Date</th><th>Actions</th></tr>");

            while (rs.next()) {
                String eventNumber = rs.getString("Event_Number");
                String eventName = rs.getString("Event_Name");
                String coordinator = rs.getString("Coordinator_Name");
                String coordinatorContact = rs.getString("Coordinator_Number");
                String fee = rs.getString("Fee");
                String venue = rs.getString("Venue");
                String date = rs.getString("Date");

                out.println("<tr><td>" + eventNumber + "</td><td>" + eventName + "</td><td>" + coordinator + "</td><td>" + coordinatorContact + "</td><td>" + fee + "</td><td>" + venue + "</td><td>" + date + "</td>");
                out.println("<td><a href='DeleteEventServlet?eventNumber=" + eventNumber + "'>Delete</a></td></tr>");
            }

            out.println("</table>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");

        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
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
