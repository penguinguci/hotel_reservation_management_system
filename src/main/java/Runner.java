import dao.StaffDAO;

import entities.Role;
import entities.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Runner {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb")
                .createEntityManager();

        StaffDAO staffDAO = new StaffDAO(em);

        // test chức năng create
//        DataGenerator dataGenerator = new DataGenerator();
//        staffDAO.create(dataGenerator.generateStaff());

        // test chức năng delete
//        staffDAO.delete("2296679c-0727-4381-89fc-4e7e0b80404a");

        // test chức năng update
//        Staff staff = new Staff();
//        staff.setStaffId("06a06abb-c60-4688-bee6-3b58be6464e5");
//        staff.setFirstName("Haywood");
//        staff.setLastName("Minhan");
//        staff.setGender(Gender.FEMALE);
//        staffDAO.update(staff);

        // test tìm kiếm danh sách nhân viên theo tên
 //       staffDAO.listStaffByName("Michael");
    }
}
