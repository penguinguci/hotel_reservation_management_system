package dao;

import entities.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import jakarta.persistence.TypedQuery;
import utils.AppUtil;

public class RevenueDAOImpl {

    private EntityManager em;

    public RevenueDAOImpl() {
        this.em = AppUtil.getEntityManager();
        if (em == null) {
            throw new IllegalStateException("EntityManager không thể khởi tạo");
        }
    }
    public List<Double> getQuarterlyRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(4);
        for (int i = 1; i <= 4; i++) {
            int startMonth = (i - 1) * 3 + 1;
            int endMonth = i * 3;
            Query query = em.createQuery(
                    "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o " +
                            "WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) BETWEEN :startMonth AND :endMonth"
            );
            query.setParameter("year", year);
            query.setParameter("startMonth", startMonth);
            query.setParameter("endMonth", endMonth);
            Double revenue = (Double) query.getSingleResult();
            quarterlyRevenue.add(revenue != null ? revenue : 0.0);
        }
        return quarterlyRevenue;
    }

    public List<Double> getYearlyRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>();
        for (int year = 2023; year <= 2025; year++) {
            Query query = em.createQuery(
                    "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o " +
                            "WHERE YEAR(o.orderDate) = :year"
            );
            query.setParameter("year", year);
            Double revenue = (Double) query.getSingleResult();
            yearlyRevenue.add(revenue != null ? revenue : 0.0);
        }
        return yearlyRevenue;
    }
    public List<Double> getRevenueByCustomer(int year) {
        Query query = em.createQuery(
                "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o " +
                        "WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY o.customer.id"
        );
        query.setParameter("year", year);
        List<Double> results = query.getResultList();
        return results.isEmpty() ? List.of(0.0) : results;
    }

    public List<String> getCustomerLabels(int year) {
        Query query = em.createQuery(
                "SELECT c.firstName FROM Customer c JOIN Orders o ON c.id = o.customer.id " +
                        "WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY c.id, c.firstName"
        );
        query.setParameter("year", year);
        List<String> labels = query.getResultList();
        return labels.isEmpty() ? List.of("Không có khách hàng") : labels;
    }

    public List<Double> getRevenueByService(int year) {
        Query query = em.createQuery(
                "SELECT COALESCE(SUM(od.quantity * od.lineTotalAmount), 0) FROM OrderDetails od JOIN od.orders o " +
                        "WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY od.service.id"
        );
        query.setParameter("year", year);
        List<Double> results = query.getResultList();
        return results.isEmpty() ? List.of(0.0) : results;
    }

    public List<String> getServiceLabels(int year) {
        Query query = em.createQuery(
                "SELECT s.name FROM Service s JOIN OrderDetails od ON s.id = od.service.id " +
                        "JOIN od.orders o WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY s.id, s.name"
        );
        query.setParameter("year", year);
        List<String> labels = query.getResultList();
        return labels.isEmpty() ? List.of("Không có dịch vụ") : labels;
    }

    public List<Double> getRevenueByRoom(int year) {
        Query query = em.createQuery(
                "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o JOIN o.room r " +
                        "WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY r.id"
        );
        query.setParameter("year", year);
        List<Double> results = query.getResultList();
        return results.isEmpty() ? List.of(0.0) : results;
    }

        public double getRoomRevenueByDateRange1(Date startDate, Date endDate) {
        // Truy vấn để lấy tất cả các Orders trong khoảng thời gian
        String jpql = "SELECT o FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Orders> ordersList = query.getResultList();

        // Tính tổng doanh thu từ phòng
        double totalRoomRevenue = 0.0;
        for (Orders order : ordersList) {
            if (order.getRoom() != null) {
                totalRoomRevenue += order.getRoom().getPrice() * order.getNumberOfNights();
            }
        }

        return totalRoomRevenue;
    }

    public double getTotalRevenueByDateRange(Date startDate, Date endDate) {
        // Truy vấn để lấy tất cả các Orders trong khoảng thời gian
        String jpql = "SELECT o FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Orders> ordersList = query.getResultList();

        // Tính tổng doanh thu (phòng + dịch vụ)
        double totalRevenue = 0.0;
        for (Orders order : ordersList) {
            totalRevenue += order.calculateTotalPrice();
        }

        return totalRevenue;
    }
    public List<Double> getMonthlyRevenue(int year) {
        String jpql = "SELECT o FROM Orders o " +
                "WHERE YEAR(o.orderDate) = :year";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("year", year);

        List<Orders> ordersList = query.getResultList();
        List<Double> monthlyRevenue = new ArrayList<>();

        // Khởi tạo doanh thu bằng 0 cho 12 tháng
        for (int i = 0; i < 12; i++) {
            monthlyRevenue.add(0.0);
        }

        // Tính tổng doanh thu theo từng tháng
        for (Orders order : ordersList) {
            int month = order.getOrderDate().getMonth(); // getMonth() trả về 0-11
            double revenue = order.calculateTotalPrice();
            monthlyRevenue.set(month, monthlyRevenue.get(month) + revenue);
        }

        return monthlyRevenue;
    }
    public double getTotalRevenue(int year) {
        String jpql = "SELECT o FROM Orders o " +
                "WHERE YEAR(o.orderDate) = :year";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("year", year);

        List<Orders> ordersList = query.getResultList();

        double totalRevenue = 0.0;
        for (Orders order : ordersList) {
            totalRevenue += order.calculateTotalPrice();
        }

        return totalRevenue;
    }

    public double getServiceRevenue(int year) {
        String jpql = "SELECT o FROM Orders o " +
                "WHERE YEAR(o.orderDate) = :year";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("year", year);

        List<Orders> ordersList = query.getResultList();

        double totalServiceRevenue = 0.0;
        for (Orders order : ordersList) {
            if (order.getOrderDetails() != null) {
                totalServiceRevenue += order.getOrderDetails().stream()
                        .mapToDouble(od -> od.calculateLineTotal())
                        .sum();
            }
        }

        return totalServiceRevenue;
    }

    public double getRoomRevenue(int year) {
        String jpql = "SELECT o FROM Orders o " +
                "WHERE YEAR(o.orderDate) = :year";
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
        query.setParameter("year", year);

        List<Orders> ordersList = query.getResultList();

        double totalRoomRevenue = 0.0;
        for (Orders order : ordersList) {
            if (order.getRoom() != null) {
                totalRoomRevenue += order.getRoom().getPrice() * order.getNumberOfNights();
            }
        }

        return totalRoomRevenue;
    }
    public List<String> getRoomLabels(int year) {
        Query query = em.createQuery(
                "SELECT r.id FROM Room r JOIN Orders o ON r.id = o.room.id " +
                        "WHERE YEAR(o.orderDate) = :year " +
                        "GROUP BY r.id, r.id"
        );
        query.setParameter("year", year);
        List<String> labels = query.getResultList();
        return labels.isEmpty() ? List.of("Không có phòng") : labels;
    }

    public double getTotalRevenueByMonth(int year, int month) {
        String jpql = "SELECT SUM(o.totalPrice) FROM Orders o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("month", month);
        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    // Phương thức mới: Lấy doanh thu dịch vụ của một tháng cụ thể

    public double getServiceRevenueByMonth(int year, int month) {
        String jpql = "SELECT SUM(od.lineTotalAmount) FROM Orders o JOIN o.orderDetails od WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("month", month);
        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    // Phương thức mới: Lấy doanh thu phòng của một tháng cụ thể

    public double getRoomRevenueByMonth(int year, int month) {
        double totalRevenue = getTotalRevenueByMonth(year, month);
        double serviceRevenue = getServiceRevenueByMonth(year, month);
        return totalRevenue - serviceRevenue;
    }

    // Phương thức mới: Lấy tổng doanh thu của một quý cụ thể

    public double getTotalRevenueByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        String jpql = "SELECT SUM(o.totalPrice) FROM Orders o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) BETWEEN :startMonth AND :endMonth";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("startMonth", startMonth);
        query.setParameter("endMonth", endMonth);
        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    // Phương thức mới: Lấy doanh thu dịch vụ của một quý cụ thể
    public double getServiceRevenueByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        String jpql = "SELECT SUM(od.lineTotalAmount) FROM Orders o JOIN o.orderDetails od WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) BETWEEN :startMonth AND :endMonth";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("startMonth", startMonth);
        query.setParameter("endMonth", endMonth);
        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    // Phương thức mới: Lấy doanh thu phòng của một quý cụ thể
    public double getRoomRevenueByQuarter(int year, int quarter) {
        double totalRevenue = getTotalRevenueByQuarter(year, quarter);
        double serviceRevenue = getServiceRevenueByQuarter(year, quarter);
        return totalRevenue - serviceRevenue;
    }
    public List<Integer> getAvailableYears() {
        String jpql = "SELECT DISTINCT YEAR(o.orderDate) FROM Orders o ORDER BY YEAR(o.orderDate)";
        Query query = em.createQuery(jpql);
        List<Integer> years = query.getResultList();
        return years.stream()
                .filter(year -> year != null)
                .collect(Collectors.toList());
    }
    public List<Double> getMonthlyTotalRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            monthlyRevenue.add(0.0);
        }

        // Truy vấn tổng doanh thu (totalPrice) từ Orders
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, SUM(o.totalPrice) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double total = (Double) result[1];
            if (month != null && total != null) {
                monthlyRevenue.set(month - 1, total);
            }
        }

        System.out.println("Total Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    // Phương thức lấy doanh thu phòng theo tháng
    public List<Double> getMonthlyRoomRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            monthlyRevenue.add(0.0);
        }

        // Truy vấn doanh thu phòng (room.price * numberOfNights) từ Orders
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, SUM(o.room.price * o.numberOfNights) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double total = (Double) result[1];
            if (month != null && total != null) {
                monthlyRevenue.set(month - 1, total);
            }
        }

        System.out.println("Room Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    // Phương thức lấy doanh thu dịch vụ theo tháng
    public List<Double> getMonthlyServiceRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            monthlyRevenue.add(0.0);
        }

        // Truy vấn doanh thu dịch vụ (lineTotalAmount) từ OrderDetails
        String jpql = "SELECT FUNCTION('MONTH', o.orders.orderDate) AS month, SUM(o.lineTotalAmount) " +
                "FROM OrderDetails o " +
                "WHERE FUNCTION('YEAR', o.orders.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orders.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double total = (Double) result[1];
            if (month != null && total != null) {
                monthlyRevenue.set(month - 1, total);
            }
        }

        System.out.println("Service Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    public List<Double> getYearlyRoomRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            yearlyRevenue.add(0.0);
        }

        String jpql = "SELECT FUNCTION('YEAR', o.orderDate) AS year, SUM(o.room.price * o.numberOfNights) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) BETWEEN 2023 AND 2025 " +
                "GROUP BY FUNCTION('YEAR', o.orderDate)";
        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double total = (Double) result[1];
            if (year != null && total != null) {
                yearlyRevenue.set(year - 2023, total);
            }
        }

        System.out.println("Yearly Room Revenue: " + yearlyRevenue);
        return yearlyRevenue;
    }

    // Phương thức lấy doanh thu dịch vụ theo năm (2023-2025)
    public List<Double> getYearlyServiceRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            yearlyRevenue.add(0.0);
        }

        String jpql = "SELECT FUNCTION('YEAR', o.orders.orderDate) AS year, SUM(o.lineTotalAmount) " +
                "FROM OrderDetails o " +
                "WHERE FUNCTION('YEAR', o.orders.orderDate) BETWEEN 2023 AND 2025 " +
                "GROUP BY FUNCTION('YEAR', o.orders.orderDate)";
        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double total = (Double) result[1];
            if (year != null && total != null) {
                yearlyRevenue.set(year - 2023, total);
            }
        }

        System.out.println("Yearly Service Revenue: " + yearlyRevenue);
        return yearlyRevenue;
    }

    // Phương thức lấy doanh thu phòng theo quý trong một năm
    public List<Double> getQuarterlyRoomRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            quarterlyRevenue.add(0.0);
        }

        String jpql = "SELECT FUNCTION('QUARTER', o.orderDate) AS quarter, SUM(o.room.price * o.numberOfNights) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('QUARTER', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer quarter = (Integer) result[0];
            Double total = (Double) result[1];
            if (quarter != null && total != null) {
                quarterlyRevenue.set(quarter - 1, total);
            }
        }

        System.out.println("Quarterly Room Revenue for year " + year + ": " + quarterlyRevenue);
        return quarterlyRevenue;
    }

    // Phương thức lấy doanh thu dịch vụ theo quý trong một năm
    public List<Double> getQuarterlyServiceRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            quarterlyRevenue.add(0.0);
        }

        String jpql = "SELECT FUNCTION('QUARTER', o.orders.orderDate) AS quarter, SUM(o.lineTotalAmount) " +
                "FROM OrderDetails o " +
                "WHERE FUNCTION('YEAR', o.orders.orderDate) = :year " +
                "GROUP BY FUNCTION('QUARTER', o.orders.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer quarter = (Integer) result[0];
            Double total = (Double) result[1];
            if (quarter != null && total != null) {
                quarterlyRevenue.set(quarter - 1, total);
            }
        }

        System.out.println("Quarterly Service Revenue for year " + year + ": " + quarterlyRevenue);
        return quarterlyRevenue;
    }

    // Lấy tổng doanh thu theo khoảng thời gian (cho panel hiển thị)
    public List<Double> getRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) { // Chia theo ngày
            String jpql = "SELECT DATE(o.orderDate) AS date, SUM(o.totalPrice) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY DATE(o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < labels.size(); i++) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = 0.0;
                for (Object[] result : results) {
                    Date date = (Date) result[0];
                    String resultDateStr = sdf.format(date);
                    if (currentDateStr.equals(resultDateStr)) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) { // Chia theo tháng
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate), SUM(o.totalPrice) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentMonth = cal.get(Calendar.MONTH) + 1;
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int month = (Integer) result[0];
                    int year = (Integer) result[1];
                    if (currentMonth == month && currentYear == year) {
                        revenue = (Double) result[2];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else { // Chia theo năm
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), SUM(o.totalPrice) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int year = (Integer) result[0];
                    if (currentYear == year) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Total Revenue by Date Range: " + revenueData);
        return revenueData;
    }

    // Lấy doanh thu dịch vụ theo khoảng thời gian (cho panel hiển thị)
    public double getServiceRevenueByDateRange1(Date startDate, Date endDate) {
        String jpql = "SELECT SUM(o.lineTotalAmount) FROM OrderDetails o WHERE o.orders.orderDate BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }
    public List<String> getDateRangeLabels(Date startDate, Date endDate) {
        List<String> labels = new ArrayList<>();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        // Tính số ngày giữa startDate và endDate
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        if (diffInDays <= 60) { // Nếu khoảng thời gian dưới 2 tháng, chia theo ngày
            while (!startCal.after(endCal)) {
                labels.add(dayFormat.format(startCal.getTime()));
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) { // Nếu khoảng thời gian dưới 1 năm, chia theo tháng
            while (!startCal.after(endCal)) {
                labels.add(monthFormat.format(startCal.getTime()));
                startCal.add(Calendar.MONTH, 1);
            }
        } else { // Nếu khoảng thời gian lớn hơn 1 năm, chia theo năm
            while (!startCal.after(endCal)) {
                labels.add(yearFormat.format(startCal.getTime()));
                startCal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Date Range Labels: " + labels);
        return labels;
    }
    // Lấy doanh thu phòng theo khoảng thời gian (cho panel hiển thị)
    public List<Double> getRoomRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) { // Chia theo ngày
            String jpql = "SELECT DATE(o.orderDate) AS date, SUM(o.room.price * o.numberOfNights) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY DATE(o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < labels.size(); i++) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = 0.0;
                for (Object[] result : results) {
                    Date date = (Date) result[0];
                    String resultDateStr = sdf.format(date);
                    if (currentDateStr.equals(resultDateStr)) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) { // Chia theo tháng
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate), SUM(o.room.price * o.numberOfNights) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentMonth = cal.get(Calendar.MONTH) + 1;
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int month = (Integer) result[0];
                    int year = (Integer) result[1];
                    if (currentMonth == month && currentYear == year) {
                        revenue = (Double) result[2];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else { // Chia theo năm
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), SUM(o.room.price * o.numberOfNights) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int year = (Integer) result[0];
                    if (currentYear == year) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Room Revenue by Date Range: " + revenueData);
        return revenueData;
    }
    // Lấy doanh thu dịch vụ theo khoảng thời gian
    public List<Double> getServiceRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) { // Chia theo ngày
            String jpql = "SELECT DATE(o.orders.orderDate) AS date, SUM(o.lineTotalAmount) " +
                    "FROM OrderDetails o " +
                    "WHERE o.orders.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY DATE(o.orders.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < labels.size(); i++) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = 0.0;
                for (Object[] result : results) {
                    Date date = (Date) result[0];
                    String resultDateStr = sdf.format(date);
                    if (currentDateStr.equals(resultDateStr)) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) { // Chia theo tháng
            String jpql = "SELECT FUNCTION('MONTH', o.orders.orderDate), FUNCTION('YEAR', o.orders.orderDate), SUM(o.lineTotalAmount) " +
                    "FROM OrderDetails o " +
                    "WHERE o.orders.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orders.orderDate), FUNCTION('YEAR', o.orders.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentMonth = cal.get(Calendar.MONTH) + 1;
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int month = (Integer) result[0];
                    int year = (Integer) result[1];
                    if (currentMonth == month && currentYear == year) {
                        revenue = (Double) result[2];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else { // Chia theo năm
            String jpql = "SELECT FUNCTION('YEAR', o.orders.orderDate), SUM(o.lineTotalAmount) " +
                    "FROM OrderDetails o " +
                    "WHERE o.orders.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orders.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < labels.size(); i++) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = 0.0;
                for (Object[] result : results) {
                    int year = (Integer) result[0];
                    if (currentYear == year) {
                        revenue = (Double) result[1];
                        break;
                    }
                }
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Service Revenue by Date Range: " + revenueData);
        return revenueData;
    }
}