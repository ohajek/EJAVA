/**
 * 
 */
package fr.epita.iam.datamodel;

/**
 * @author ohajek
 *
 */
public class Identity {
	
	private String uid;
	private String displayName;
	private String email;
	private String password;
	private String privilege;
	
	
	/**
	 * Implicit constructor for class Identity with all required parameters
	 * @param uid			Unique identity number of the identity
	 * @param displayName	Name of the identity
	 * @param email			Email contact of the identity
	 * @param password		Password for the identity
	 * @param privilege		Privilege for the identity to manipulate with the database
	 */
	public Identity(String uid, String displayName, String email, String password, String privilege) {
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
		this.password = password;
		this.privilege = privilege;
	}
	
	/**
	 * Constructor used for displaying Identities that leaves password and privilege on purpose (not showing it to user)
	 * @param uid			Unique identity number of the identity
	 * @param displayName	Name of the identity
	 * @param email			Email contact of the identity
	 */
	public Identity(String uid, String displayName, String email) {
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
		this.password = "";
		this.privilege = "user";
	}
	
	/**
	 * 
	 * @return Identity UID
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * 
	 * @param uid	UID to be set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 
	 * @return	Name of the identity
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * 
	 * @param displayName Name of the identity to be set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * 
	 * @return Email of the identity
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @param email Email to be set for the identity
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return Password of the identity
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password Password to be set for the identitys
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @return	Identity privilege
	 */
	public String getPrivilege() {
		return privilege;
	}

	/**
	 * 
	 * @param privilege Privilege to be set for the identity
	 */
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	/**
	 * Overriden method to print out the Identity object information
	 */
	@Override
	public String toString() {
		return "NAME: " + displayName + "; EMAIL: " + email + ";";
	}
	
	

}
