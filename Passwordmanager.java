import java.security.SecureRandom;
import java.sql.*;
public class Passwordmanager {
public static void main(String[] args) {
Connection conn;
               try {
            		Class.forName("com.mysql.jdbc.Driver");
conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/java?characterEncoding=utf8", "root", "");
boolean exit = false;
            		while (!exit) {
                		System.out.println("1. Generate Password and Save");
                		System.out.println("2. Save a new Password");
                		System.out.println("3. Retrieve Passwords");
                		System.out.println("4. Update the password");
			System.out.println("5. exit");		
                		System.out.print("Enter your choice: ");
                		int choice = Integer.parseInt(System.console().readLine());
			switch (choice) {
                  		case 1:
    			while (true) {
        				System.out.print("Enter length of the password: ");
        				int length = Integer.parseInt(System.console().readLine());
        				String generatedPassword = generatePassword(length);
        				System.out.println("Generated Password: " + generatedPassword);
        				System.out.println("Password Strength: " +                       checkPasswordStrength(generatedPassword));
				System.out.print("Do you want to save this password? (yes/no): ");
        				String confirm = System.console().readLine().trim().toLowerCase();
        				if (confirm.equals("yes")) {
           				while (true) {
                				System.out.print("Enter label/name for the password: ");
                				String label = System.console().readLine();
                				if (labelExists(conn, label)) {
                    				System.out.println("A password with the label '" + label + "' already exists. Please enter a different label.");
               				 } else {
                    			              savePassword(conn, label, generatedPassword);
break;
                				}
           				 }
break;
        				} else if (confirm.equals("no")) {
            				continue;
        				} else {        				
System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        			}
    		}
    		break;
                    	case 2:			
    		while (true) {
        		System.out.print("Enter label/name for the password: ");
        		String label = System.console().readLine();
        		if (labelExists(conn, label)) {
            		System.out.println("A password with the label '" + label + "' already exists. Please enter a different label.");
        		 }else {
            		 System.out.print("Enter password: ");
            		String newPassword = System.console().readLine();
            		savePassword(conn, label, newPassword);
            		break;
        		}
    	}
   	 break;
                case 3:
                        System.out.print("Enter label/name to retrieve password: ");
                        String retrieveLabel = System.console().readLine();
                        String retrievedPassword = retrievePassword(conn, retrieveLabel);
                        if (retrievedPassword != null) {
                        	System.out.println("Retrieved Password: " + retrievedPassword);
                        } else {
                            System.out.println("No password found for the provided label.");
                        }
                        break;
	case 4:
    		System.out.print("Enter label/name of the password to update: ");
    		String updateLabel = System.console().readLine();
    		if (!labelExists(conn, updateLabel)) {
    		System.out.println("No password found for the provided label.");
    		} else {
    		System.out.print("Enter the new password: ");
        		String newPassword = System.console().readLine();
        		updatePassword(conn, updateLabel, newPassword);
        		System.out.println("Password updated successfully.");
    		}
   		 break;
                    case 5:
                        	exit = true;
System.out.println("Exiting program...");
		break;
                    	default:
                        	System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                	}
            	}
	conn.close();
} catch (ClassNotFoundException e) {
            	System.err.println("Error loading MySQL JDBC driver: " + e.getMessage());
        	} catch (SQLException e) {
            	System.err.println("Error: " + e.getMessage());
        	}
    }
     private static String generatePassword(int length) {
     String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
private static void savePassword(Connection conn, String label, String password) throws SQLException {
    String sql = "INSERT INTO passwords (label, password) VALUES (?, ?)";
    PreparedStatement st = conn.prepareStatement(sql);
    st.setString(1, label);
    st.setString(2, password);
    st.executeUpdate();
    System.out.println("Password saved successfully.");
    st.close();
}
private static boolean labelExists(Connection conn, String label) throws SQLException {
    String sql = "SELECT COUNT(*) AS count FROM passwords WHERE label = ?";
    PreparedStatement st = conn.prepareStatement(sql);
    st.setString(1, label);
    ResultSet rs = st.executeQuery();
    rs.next();
    int count = rs.getInt("count");
    rs.close();
    st.close();
    return count > 0;
}
private static String retrievePassword(Connection conn, String label) throws SQLException {
        String sql = "SELECT password FROM passwords WHERE label = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, label);
        ResultSet rs = st.executeQuery();
        String password = null;
        if (rs.next()) {
            password = rs.getString("password");
        }
        rs.close();
        st.close();
        return password;
    }
    private static String checkPasswordStrength(String password) {
   int length = password.length();
   if (length < 8) {
            return "Weak : Length is too small.";
   } else if (length < 12) {
            return "Moderate : Increase the length of your password, to strengthen it";
   } else {
            return "Strong Password.";
      }
   }
   private static void updatePassword(Connection conn, String label, String newPassword) throws   SQLException {
   String sql = "UPDATE passwords SET password = ? WHERE label = ?";
    PreparedStatement st = conn.prepareStatement(sql);
    st.setString(1, newPassword);
    st.setString(2, label);
    st.executeUpdate();
    st.close();
    }
}
