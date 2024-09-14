/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hp
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendeeServlet")
public class AttendeeServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EventlyDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Aaru@1712";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if ("add".equals(action)) {
                addAttendee(request, con);
            } else if ("update".equals(action)) {
                updateAttendee(request, con);
            } else if ("delete".equals(action)) {
                deleteAttendee(request, con);
            }

            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String eventNumber = request.getParameter("eventNumber");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Attendees WHERE Event_Number = " + eventNumber;
            ResultSet rs = stmt.executeQuery(sql);

            out.println("<html><body>");
            out.println("<h1>Attendees List for Event Number: " + eventNumber + "</h1>");
            out.println("<table border='1'><tr><th>Attendee ID</th><th>Name</th><th>Email</th><th>Phone</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("Attendee_ID") + "</td>");
                out.println("<td>" + rs.getString("Attendee_Name") + "</td>");
                out.println("<td>" + rs.getString("Attendee_Email") + "</td>");
                out.println("<td>" + rs.getString("Attendee_Phone") + "</td></tr>");
            }

            out.println("</table></body></html>");
            rs.close();
            stmt.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAttendee(HttpServletRequest request, Connection con) throws SQLException {
        String eventNumber = request.getParameter("eventNumber");
        String attendeeName = request.getParameter("attendeeName");
        String attendeeEmail = request.getParameter("attendeeEmail");
        String attendeePhone = request.getParameter("attendeePhone");

        String sql = "INSERT INTO Attendees (Event_Number, Attendee_Name, Attendee_Email, Attendee_Phone) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(eventNumber));
        pst.setString(2, attendeeName);
        pst.setString(3, attendeeEmail);
        pst.setString(4, attendeePhone);
        pst.executeUpdate();
        pst.close();
    }

    private void updateAttendee(HttpServletRequest request, Connection con) throws SQLException {
        String attendeeId = request.getParameter("attendeeId");
        String attendeeName = request.getParameter("attendeeName");
        String attendeeEmail = request.getParameter("attendeeEmail");
        String attendeePhone = request.getParameter("attendeePhone");

        String sql = "UPDATE Attendees SET Attendee_Name = ?, Attendee_Email = ?, Attendee_Phone = ? WHERE Attendee_ID = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, attendeeName);
        pst.setString(2, attendeeEmail);
        pst.setString(3, attendeePhone);
        pst.setInt(4, Integer.parseInt(attendeeId));
        pst.executeUpdate();
        pst.close();
    }

    private void deleteAttendee(HttpServletRequest request, Connection con) throws SQLException {
        String attendeeId = request.getParameter("attendeeId");

        String sql = "DELETE FROM Attendees WHERE Attendee_ID = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(attendeeId));
        pst.executeUpdate();
        pst.close();
    }
}
