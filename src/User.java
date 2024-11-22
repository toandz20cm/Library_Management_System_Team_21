import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the library system.
 * Stores user details and manages borrowed documents.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String displayName;
    private String birthDate;
    private String phoneNumber;
    private List<Document> borrowedDocuments;

    /**
     * Constructs a new User instance.
     *
     * @param username    the username of the user
     * @param password    the password of the user
     * @param displayName the display name of the user
     * @param birthDate   the birth date of the user
     * @param phoneNumber the phone number of the user
     */
    public User(final String username, final String password, final String displayName,
                final String birthDate, final String phoneNumber) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.borrowedDocuments = new ArrayList<>();
    }

    // Getter and Setter methods

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Document> getBorrowedDocuments() {
        return borrowedDocuments;
    }

    // Functional methods

    /**
     * Adds a document to the list of borrowed documents.
     *
     * @param document the document to be borrowed
     */
    public void borrowDocument(final Document document) {
        borrowedDocuments.add(document);
    }

    /**
     * Removes a document from the list of borrowed documents.
     *
     * @param document the document to be returned
     */
    public void returnDocument(final Document document) {
        borrowedDocuments.remove(document);
    }

    @Override
    public String toString() {
        return "Username: " + username +
               ", Display Name: " + displayName +
               ", Birth Date: " + birthDate +
               ", Phone Number: " + phoneNumber;
    }
}
