/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;



/**
 * Simple console launcher for the program. First, it tries to authenticate the user.
 * If it's the admin, it will let him in, otherwise it will exit the launcher.
 * Once the user is authenticated as an admin, he can do the following: create new identity,
 * modify existing one, delete or exit the program. Each of these options has several substeps, like
 * providing which identity to modify and new parameters for the identity.
 * @author ohajek
 *
 */
public class ConsoleLauncher {
	
	private static JDBCIdentityDAO dao;

	private static final String DASHES = 
            "\n----------------------------------------"
          + "----------------------------------------"
          + "----------------------------------------\n";
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("Hello, welcome to the IAM application");
		Scanner scanner = new Scanner(System.in);
		
		try {
			dao = new JDBCIdentityDAO();
		}
		catch(SQLException e) {
			System.out.println(DASHES + "ERROR::Cannot connect to the database!" + DASHES);
			scanner.close();
			return;
		}
		
		//authentication
		System.out.println("Please enter your login");
		String login = scanner.nextLine();
		System.out.println("Please enter your password");
		String password = scanner.nextLine();
		System.out.println(DASHES);
		
		while(!authenticate(login, password)){
			System.out.println(DASHES + "Wrong user name/password!" + DASHES);
			scanner.close();
			return;
		}
		
		// menu
		boolean shouldClose = false;
		while(!shouldClose) {
			String answer = menu(scanner);
			
			switch (answer) {
				case "a":
					// Creation
					createIdentity(scanner);
					break;
				case "b":
					// Modification
					modifyIdentity(scanner);
					break;
					// Deletion
				case "c":
					deleteIdentity(scanner);
					break;
					// Listing
				case "d":
					listIdentities();
					break;
					// Exiting
				case "e":
					shouldClose = true;
					break;
					
				default:
					System.out.println("This option is not recognized ("+ answer + ")");
					break;
			}
		}
		
		System.out.println("Exiting");
		scanner.close();
	}


	/**
	 * Creates new identity to ve written into the database. Prompts the user to enter all informations
	 * about the identity
	 * @param scanner		Scanner for getting user input
	 * @throws SQLException
	 */
	private static void createIdentity(Scanner scanner) throws SQLException {
		boolean isWrong = true;
		System.out.println("You've selected : Identity Creation");
		System.out.print(DASHES + "Please enter the Identity display name" + DASHES);
		String displayName = scanner.nextLine();
		System.out.print(DASHES + "Please enter password:" + DASHES);
		String password = scanner.nextLine();
		System.out.print(DASHES + "Please enter the Identity email" + DASHES);
		String email = scanner.nextLine();
		System.out.print(DASHES + "Please enter privilege type: a) Admin b) User" + DASHES);
		String privilege = scanner.nextLine();
		while(isWrong) {
			if("a".equals(privilege)) {
				privilege = "admin";
				isWrong = false;
			} else if("b".equals(privilege)) {
				privilege = "user";
				isWrong = false;
			} else {
				System.out.println("Please enter valid input.");
				privilege = scanner.nextLine();
			}
		}
			
		Identity newIdentity = new Identity(null, displayName, email, password, privilege);
		dao.writeIdentity(newIdentity);
		System.out.println("\nYou succesfully created this identity" + DASHES + newIdentity + DASHES);
	}

	/**
	 * Prompts user to choose identity to modify, then asks for the new information about it.
	 * At the end, it sends the identity to be written into the database
	 * @param scanner		Scanner for getting user input
	 * @throws SQLException
	 */
	private static void modifyIdentity(Scanner scanner) throws SQLException {
		Identity chosenIdentity = identitySelector(scanner, "modify");
		
		boolean isWrong = true;
		System.out.println("You've selected : Identity Creation");
		System.out.print(DASHES + "Please enter the Identity display name" + DASHES);
		String displayName = scanner.nextLine();
		System.out.print(DASHES + "Please enter password:" + DASHES);
		String password = scanner.nextLine();
		System.out.print(DASHES + "Please enter the Identity email" + DASHES);
		String email = scanner.nextLine();
		System.out.print(DASHES + "Please enter privilege type: a) Admin b) User" + DASHES);
		String privilege = scanner.nextLine();
		while(isWrong) {
			if("a".equals(privilege)) {
				privilege = "admin";
				isWrong = false;
			} else if("b".equals(privilege)) {
				privilege = "user";
				isWrong = false;
			} else {
				System.out.println("Please enter valid input.");
				privilege = scanner.nextLine();
			}
		}
			
		chosenIdentity.setDisplayName(displayName);
		chosenIdentity.setEmail(email);
		chosenIdentity.setPassword(password);
		chosenIdentity.setPrivilege(privilege);
		
		dao.modifyIdentity(chosenIdentity);
	}
	
	/**
	 * Prints out all identities from the database.
	 * @throws SQLException
	 */
	private static void listIdentities() throws SQLException {
		List<Identity> list = getAllIdentities();
		
		if(list == null) {
			return;
		}
		
		System.out.println(DASHES + "This is the list of all identities in the system" + DASHES);
		for(int i = 0; i < list.size(); i++){
			System.out.println( i+ ". " + list.get(i));
		}
		System.out.println(DASHES);
		
		return;
	}

	/**
	 * Prompts the user to select identity to be deleted. Then it sends its UID to DAO
	 * @param scanner
	 * @throws SQLException
	 */
	private static void deleteIdentity(Scanner scanner) throws SQLException {
		Identity chosenIdentity = identitySelector(scanner, "delete");
		dao.deleteIdentity(chosenIdentity.getUid());
	}
	
	/**
	 * Prints out menu for the user with all the options he can do and prompts his choice.
	 * @param scanner	Scanner for user input
	 * @return			Users choice as string
	 */
	private static String menu(Scanner scanner) {
		System.out.println("You're authenticated");
		System.out.println("Here are the actions you can perform :");
		System.out.println("a. Create an Identity");
		System.out.println("b. Modify an Identity");
		System.out.println("c. Delete an Identity");
		System.out.println("d. List Identities");
		System.out.println("e. quit");
		System.out.println("your choice (a|b|c|d|e) ? : ");
		return scanner.nextLine();
	}


	//------------------
	// Utility functions
	//------------------
	
	/**
	 * Gets all identities from the database and returns them as a list
	 * @return					List of all identities in the database
	 * @throws SQLException
	 */
	private static List<Identity> getAllIdentities() throws SQLException {
		return dao.readAll();
	}
	
	/**
	 * Prints out all identities prompts user to select one.
	 * @param scanner			Scanner for user input
	 * @param selectPurpose		Purpose of selection as string to be printed out (ie. "deleted")
	 * @return					Chosen identity object
	 * @throws SQLException
	 */
	private static Identity identitySelector(Scanner scanner, String selectPurpose) throws SQLException {
		List<Identity> identityList = getAllIdentities();
		
		System.out.println("\nChoose identity to " + selectPurpose + "" + DASHES);
		for(int i = 0; i < identityList.size(); i++){
			System.out.println( i+ "." + identityList.get(i));
		}
		System.out.println(DASHES);
		
		int answer = 0;
		boolean shouldClose = false;
		while(!shouldClose) {
			while (!scanner.hasNextInt()) {
				   System.out.println("Enter integer representing ID of indentity");
				   scanner.nextLine();
				}
			answer = scanner.nextInt();
			if(answer < 0 || answer > identityList.size()) {
				System.out.println("Choose a valid identity");
			} else {
				System.out.println(DASHES + "Identity " + answer + " chosen to " + selectPurpose + DASHES);
				shouldClose = true;			
			}
		}
		scanner.nextLine();
		return identityList.get(answer);
	}

	
	/**
	 * Sends users credentials to DAO for authentication, returns its response
	 * @param login			Name of the user logging in
	 * @param password		Password of the user logging in
	 * @return				True, if the user is in the database and has admin privilege
	 * @throws SQLException
	 */
	private static boolean authenticate(String login, String password) throws SQLException {
		return dao.authenticate(login, password);
	}

}
