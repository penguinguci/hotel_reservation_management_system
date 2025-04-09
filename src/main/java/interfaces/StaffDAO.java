package interfaces;

public interface StaffDAO {
    long countByPrefix(String prefix);
    boolean isUsernameExists(String username);
}
