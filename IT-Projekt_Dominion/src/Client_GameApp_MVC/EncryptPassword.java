package Client_GameApp_MVC;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptPassword {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String salt = getSalt();
		
		// The encrypted user password
		String correctEncryptedPassword;
		
		// The unencrypted user password
		String unencryptedPassword;

		// Encrypt a password
		unencryptedPassword = "password";
		correctEncryptedPassword = encryptPassword(unencryptedPassword, salt);
		// Alternative: ClassName.encryptPassword(unencryptedPassword, salt);
		System.out.println(correctEncryptedPassword);
		
		// Test Validate Password
		unencryptedPassword = "123";
		Boolean isPasswordCorrect = testValidatePassword(unencryptedPassword, salt);
		System.out.println(isPasswordCorrect);
		// System.out.println(EncryptPassword.testValidatePassword(unencryptedPassword, salt));
	}

	// Adrian
	// Encrypts a password using the secure hash algorithm (SHA-512) and returns it
	public static String encryptPassword(String unencryptedPassword, String salt) throws NoSuchAlgorithmException {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(unencryptedPassword.getBytes());
			StringBuilder sb = new StringBuilder();

			for(int i=0; i< bytes.length ;i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	// Add salt
	private static String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
	// Adrian
	// Validates the accuracy of the entered password by encrypting it and comparing it to the correct encrypted password.
	// Returns true, if the password is correct.
	
	// Zusätzlich Parameter "Benutzername DB" mitgeben, um die Korrektheit überprüfen zu können
	public static boolean validatePassword(String unencryptedPassword, String salt) throws NoSuchAlgorithmException {
		boolean passwordAccuracy = false;

		// Get the correct encrypted password out of the database?
		String correctEncryptedPassword = ""; // SELECT PASSWORD FROM USER WHERE USERNAME = "Benutzername DB";
		
		// Encrypt the entered password
		String encryptedPassword = encryptPassword(unencryptedPassword, salt);

		if (encryptedPassword.equals(correctEncryptedPassword)) {
			passwordAccuracy = true;
		} else {
			passwordAccuracy = false;
		}
		return passwordAccuracy;
	}

	// nur zum Testen und danach löschen (Hashwert für das Password "password" als correctEnctyptedPassword hinzugefügt:
	public static boolean testValidatePassword(String unencryptedPassword, String salt) throws NoSuchAlgorithmException {
		boolean passwordAccuracy = false;

		// Get the correct encrypted password out of the database?
		// Hashwert für "password": dedc0217953ccb8b929b284a32a9cc05ef160d36c999b2e0c1e8ea38f5d05f7a912dcde0861bd287f545a82eaf5f6eb38ddc56001605380198ecc9c08428b47e
		String correctEncryptedPassword = "dedc0217953ccb8b929b284a32a9cc05ef160d36c999b2e0c1e8ea38f5d05f7a912dcde0861bd287f545a82eaf5f6eb38ddc56001605380198ecc9c08428b47e";
		
		// Encrypt the entered password
		String encryptedPassword = encryptPassword(unencryptedPassword, salt);

		if (encryptedPassword.equals(correctEncryptedPassword)) {
			passwordAccuracy = true;
		} else {
			passwordAccuracy = false;
		}
		return passwordAccuracy;
	}
}