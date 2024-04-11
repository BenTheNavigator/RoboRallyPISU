package dk.dtu.compute.se.pisd.roborally.dal;

/**
 * Template class to insert credentials for your DB. Not secure yet, probably needs to be fixed.
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
