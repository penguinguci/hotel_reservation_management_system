package interfaces;

import entities.Staff;

import java.util.List;

public interface StaffDAO extends GenericDAO<Staff, String> {
    long countByPrefix(String prefix);
    boolean isEmailExists(String email);
    boolean isPhoneExists(String phone);
    List<Staff> searchStaffAdvanced(String id, String name, String phone, Boolean gender);
}
