package ultilities;

import dao.StaffDAOImpl;
import interfaces.StaffDAO;

import java.time.LocalDate;

public class GenerateString {
    public static String generateStaffId() {
        String prefix = "NV" + LocalDate.now().getYear() +
                String.format("%02d", LocalDate.now().getMonthValue());

        StaffDAO staffDao = new StaffDAOImpl();
        long count = staffDao.countByPrefix(prefix);

        return prefix + String.format("%03d", count + 1);
    }

    public static String generateUserName(String name) {
        String[] parts = name.split(" ");
        StringBuilder userName = new StringBuilder();
        for (String part : parts) {
            userName.append(part.charAt(0));
        }
        userName.append(System.currentTimeMillis() % 10000);
        return userName.toString();
    }
}
