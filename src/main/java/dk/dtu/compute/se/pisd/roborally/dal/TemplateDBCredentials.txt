package dk.dtu.compute.se.pisd.roborally.dal;

/**
 * Template class to insert credentials for your DB. Not secure yet, probably needs to be fixed.
 * 
 * How to use:
 * Create a file called DBCredentials.java in this location (dal folder) 'src/main/java/dk/dtu/compute/se/pisd/roborally/dal/DBCredentials.java'
 * Copy the content of this template file into DBCredentials.java and fill out your username and password in DBCredentials.java.
 * Then save to your local repo and you are done. 
 * @author s235444
 */

public class DBCredentials {
    private static final String username = "insert your username";
    private static final String password = "insert your password";

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
