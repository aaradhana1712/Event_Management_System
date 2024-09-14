import com.mysql.cj.Session;
import com.mysql.cj.protocol.Message;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import jdk.internal.net.http.websocket.Transport;

@WebServlet("/SendReminder")
public class SendReminder extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String eventNumber = request.getParameter("eventNumber");
        String reminderMessage = request.getParameter("reminderMessage");

        // Database connection details
        String dbURL = "jdbc:mysql://localhost:3306/EventlyDB";
        String dbUser = "root";
        String dbPassword = "Aaru@1712";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load the JDBC driver and establish a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            // Query to get the coordinator's email address based on the event number
            String eventQuery = "SELECT Coordinator_Email FROM Event WHERE Event_Number = ?";
            pst = con.prepareStatement(eventQuery);
            pst.setString(1, eventNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                String coordinatorEmail = rs.getString("Coordinator_Email");

                // Email properties and session setup
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.example.com");  // Replace with your SMTP server
                props.put("mail.smtp.port", "587");  // Replace with your SMTP server port
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Create a session with an authenticator
                Session mailSession = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("aaradhana1712@gmail.com", "Aaru@1712"); // Replace with your email credentials
                    }
                });

                // Create and send the email
                Message message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress("your-email@example.com")); // Replace with your email address
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(coordinatorEmail));
                message.setSubject("Reminder for Event: " + eventNumber);
                message.setText(reminderMessage);
                
                // Send the email
                Transport.send(message);

                // Send success response
                response.setContentType("text/html");
                response.getWriter().println("<h1 style='text-align: center;'>Reminder Sent Successfully!</h1>");
            } else {
                // Event not found
                response.setContentType("text/html");
                response.getWriter().println("<h1 style='text-align: center;'>Event Not Found</h1>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h1 style='text-align: center;'>Database Error</h1>");
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
