package dao;

import interfaces.AmountCustomerDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Lenovo
 */
public class AmountCustomerDAOImpl extends UnicastRemoteObject implements AmountCustomerDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;

    public AmountCustomerDAOImpl() throws RemoteException {
        super();
        this.em = AppUtil.getEntityManager();
        if (em == null) {
            throw new IllegalStateException("EntityManager không thể khởi tạo");
        }
    }

    @Override
    public List<Integer> getAvailableYears() {
        List<Integer> years = new ArrayList<>();
        String jpql = "SELECT DISTINCT FUNCTION('YEAR', o.checkInDate) FROM Orders o WHERE o.checkInDate IS NOT NULL ORDER BY FUNCTION('YEAR', o.checkInDate)";

        try {
            TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
            years = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return years;
    }

    @Override
    public List<String> getDateRangeLabels(Date startDate, Date endDate) {
        List<String> labels = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Tính số ngày giữa startDate và endDate
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 31) {
            // Nếu khoảng thời gian nhỏ hơn hoặc bằng 31 ngày, hiển thị theo ngày
            long currentTime = startDate.getTime();
            while (currentTime <= endDate.getTime()) {
                labels.add(sdf.format(new Date(currentTime)));
                currentTime += 1000 * 60 * 60 * 24; // Tăng thêm 1 ngày
            }
        } else if (diffInDays <= 365) {
            // Nếu khoảng thời gian nhỏ hơn hoặc bằng 1 năm, hiển thị theo tháng
            int startMonth = startDate.getMonth() + 1;
            int startYear = startDate.getYear() + 1900;
            int endMonth = endDate.getMonth() + 1;
            int endYear = endDate.getYear() + 1900;

            while (startYear < endYear || (startYear == endYear && startMonth <= endMonth)) {
                labels.add(String.format("Th%02d/%d", startMonth, startYear));
                startMonth++;
                if (startMonth > 12) {
                    startMonth = 1;
                    startYear++;
                }
            }
        } else {
            // Nếu khoảng thời gian lớn hơn 1 năm, hiển thị theo năm
            int startYear = startDate.getYear() + 1900;
            int endYear = endDate.getYear() + 1900;
            for (int year = startYear; year <= endYear; year++) {
                labels.add(String.valueOf(year));
            }
        }
        return labels;
    }

    @Override
    public List<Integer> getCustomerCountByDateRange(Date startDate, Date endDate) {
        List<Integer> customerCounts = new ArrayList<>();

        // Tính số ngày giữa startDate và endDate
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        try {
            if (diffInDays <= 31) {
                // Theo ngày
                long currentTime = startDate.getTime();
                while (currentTime <= endDate.getTime()) {
                    Date currentDate = new Date(currentTime);
                    Date nextDate = new Date(currentTime + 1000 * 60 * 60 * 24);

                    String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                            "FROM Orders o " +
                            "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                    Query query = em.createQuery(jpql);
                    query.setParameter("startDate", currentDate);
                    query.setParameter("endDate", nextDate);
                    Long count = (Long) query.getSingleResult();
                    customerCounts.add(count.intValue());

                    currentTime += 1000 * 60 * 60 * 24; // Tăng thêm 1 ngày
                }
            } else if (diffInDays <= 365) {
                // Theo tháng
                int startMonth = startDate.getMonth() + 1;
                int startYear = startDate.getYear() + 1900;
                int endMonth = endDate.getMonth() + 1;
                int endYear = endDate.getYear() + 1900;

                while (startYear < endYear || (startYear == endYear && startMonth <= endMonth)) {
                    Date monthStart = new Date(startYear - 1900, startMonth - 1, 1);
                    Date monthEnd = new Date(startYear - 1900, startMonth - 1, getLastDayOfMonth(startMonth, startYear));
                    monthEnd = new Date(monthEnd.getTime() + 1000 * 60 * 60 * 24); // Đến cuối ngày

                    String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                            "FROM Orders o " +
                            "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                    Query query = em.createQuery(jpql);
                    query.setParameter("startDate", monthStart);
                    query.setParameter("endDate", monthEnd);
                    Long count = (Long) query.getSingleResult();
                    customerCounts.add(count.intValue());

                    startMonth++;
                    if (startMonth > 12) {
                        startMonth = 1;
                        startYear++;
                    }
                }
            } else {
                // Theo năm
                int startYear = startDate.getYear() + 1900;
                int endYear = endDate.getYear() + 1900;

                for (int year = startYear; year <= endYear; year++) {
                    Date yearStart = new Date(year - 1900, 0, 1);
                    Date yearEnd = new Date(year - 1900, 11, 31);
                    yearEnd = new Date(yearEnd.getTime() + 1000 * 60 * 60 * 24);

                    String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                            "FROM Orders o " +
                            "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                    Query query = em.createQuery(jpql);
                    query.setParameter("startDate", yearStart);
                    query.setParameter("endDate", yearEnd);
                    Long count = (Long) query.getSingleResult();
                    customerCounts.add(count.intValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerCounts;
    }

    @Override
    public int getTotalCustomerCountByDateRange(Date startDate, Date endDate) {
        try {
            String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                    "FROM Orders o " +
                    "WHERE o.checkInDate >= :startDate AND o.checkInDate <= :endDate AND o.status = 1";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Integer> getMonthlyCustomerCount(int year) {
        List<Integer> monthlyCounts = new ArrayList<>();
        try {
            for (int month = 1; month <= 12; month++) {
                Date monthStart = new Date(year - 1900, month - 1, 1);
                Date monthEnd = new Date(year - 1900, month - 1, getLastDayOfMonth(month, year));
                monthEnd = new Date(monthEnd.getTime() + 1000 * 60 * 60 * 24); // Đến cuối ngày

                String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                        "FROM Orders o " +
                        "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                Query query = em.createQuery(jpql);
                query.setParameter("startDate", monthStart);
                query.setParameter("endDate", monthEnd);
                Long count = (Long) query.getSingleResult();
                monthlyCounts.add(count.intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthlyCounts;
    }

    @Override
    public List<Integer> getQuarterlyCustomerCount(int year) {
        List<Integer> quarterlyCounts = new ArrayList<>();
        try {
            for (int quarter = 1; quarter <= 4; quarter++) {
                Date quarterStart = new Date(year - 1900, (quarter - 1) * 3, 1);
                int endMonth = quarter * 3;
                int endDay = getLastDayOfMonth(endMonth, year);
                Date quarterEnd = new Date(year - 1900, endMonth - 1, endDay);
                quarterEnd = new Date(quarterEnd.getTime() + 1000 * 60 * 60 * 24);

                String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                        "FROM Orders o " +
                        "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                Query query = em.createQuery(jpql);
                query.setParameter("startDate", quarterStart);
                query.setParameter("endDate", quarterEnd);
                Long count = (Long) query.getSingleResult();
                quarterlyCounts.add(count.intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quarterlyCounts;
    }

    @Override
    public List<Integer> getYearlyCustomerCount() {
        List<Integer> yearlyCounts = new ArrayList<>();
        try {
            for (int year = 2023; year <= 2025; year++) {
                Date yearStart = new Date(year - 1900, 0, 1);
                Date yearEnd = new Date(year - 1900, 11, 31);
                yearEnd = new Date(yearEnd.getTime() + 1000 * 60 * 60 * 24);

                String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                        "FROM Orders o " +
                        "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
                Query query = em.createQuery(jpql);
                query.setParameter("startDate", yearStart);
                query.setParameter("endDate", yearEnd);
                Long count = (Long) query.getSingleResult();
                yearlyCounts.add(count.intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yearlyCounts;
    }

    @Override
    public int getTotalCustomerCountByQuarter(int year, int quarter) {
        try {
            Date quarterStart = new Date(year - 1900, (quarter - 1) * 3, 1);
            int endMonth = quarter * 3;
            int endDay = getLastDayOfMonth(endMonth, year);
            Date quarterEnd = new Date(year - 1900, endMonth - 1, endDay);
            quarterEnd = new Date(quarterEnd.getTime() + 1000 * 60 * 60 * 24);

            String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                    "FROM Orders o " +
                    "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", quarterStart);
            query.setParameter("endDate", quarterEnd);
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getTotalCustomerCount(int year) {
        try {
            Date yearStart = new Date(year - 1900, 0, 1);
            Date yearEnd = new Date(year - 1900, 11, 31);
            yearEnd = new Date(yearEnd.getTime() + 1000 * 60 * 60 * 24);

            String jpql = "SELECT COUNT(DISTINCT o.customer.customerId) " +
                    "FROM Orders o " +
                    "WHERE o.checkInDate >= :startDate AND o.checkInDate < :endDate AND o.status = 1";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", yearStart);
            query.setParameter("endDate", yearEnd);
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getLastDayOfMonth(int month, int year) {
        switch (month) {
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 31;
        }
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}