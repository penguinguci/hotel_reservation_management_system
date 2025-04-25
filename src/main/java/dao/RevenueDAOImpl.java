package dao;

import entities.Orders;
import entities.OrderDetails;
import entities.PaymentMethod;
import interfaces.RevenueDAO;
import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RevenueDAOImpl extends UnicastRemoteObject implements RevenueDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;
    private GenericDAO genericDAO;

    public RevenueDAOImpl() throws RemoteException {
        super();
        this.em = AppUtil.getEntityManager();
        if (em == null) {
            throw new IllegalStateException("EntityManager không thể khởi tạo");
        }
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    @Override
    public List<Double> getQuarterlyRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(Collections.nCopies(4, 0.0));
        String jpql = "SELECT FUNCTION('QUARTER', o.orderDate) AS quarter, " +
                "SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('QUARTER', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer quarter = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (quarter != null && revenue != null) {
                quarterlyRevenue.set(quarter - 1, revenue);
            }
        }
        return quarterlyRevenue;
    }

    @Override
    public List<Double> getYearlyRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>(Collections.nCopies(3, 0.0));
        String jpql = "SELECT FUNCTION('YEAR', o.orderDate) AS year, " +
                "SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) BETWEEN 2023 AND 2025 " +
                "GROUP BY FUNCTION('YEAR', o.orderDate)";
        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (year != null && revenue != null) {
                yearlyRevenue.set(year - 2023, revenue);
            }
        }
        return yearlyRevenue;
    }

    @Override
    public double getRoomRevenueByDateRange1(Date startDate, Date endDate) {
        String jpql = "SELECT SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getTotalRevenueByDateRange(Date startDate, Date endDate) {
        String jpql = "SELECT SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public List<Double> getMonthlyRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, " +
                "SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (month != null && revenue != null) {
                monthlyRevenue.set(month - 1, revenue);
            }
        }
        return monthlyRevenue;
    }

    @Override
    public double getTotalRevenue(int year) {
        String jpql = "SELECT SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getServiceRevenue(int year) {
        String jpql = "SELECT SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getRoomRevenue(int year) {
        String jpql = "SELECT SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public List<String> getRoomLabels(int year) {
        String jpql = "SELECT DISTINCT r.id " +
                "FROM Room r JOIN Orders o ON r.id = o.room.id " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        List<String> labels = query.getResultList();
        return labels.isEmpty() ? List.of("Không có phòng") : labels;
    }

    @Override
    public double getTotalRevenueByMonth(int year, int month) {
        String jpql = "SELECT SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("month", month);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getServiceRevenueByMonth(int year, int month) {
        String jpql = "SELECT SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("month", month);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getRoomRevenueByMonth(int year, int month) {
        String jpql = "SELECT SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("month", month);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getTotalRevenueByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        String jpql = "SELECT SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) BETWEEN :startMonth AND :endMonth";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("startMonth", startMonth);
        query.setParameter("endMonth", endMonth);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getServiceRevenueByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        String jpql = "SELECT SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) BETWEEN :startMonth AND :endMonth";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("startMonth", startMonth);
        query.setParameter("endMonth", endMonth);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public double getRoomRevenueByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        String jpql = "SELECT SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) BETWEEN :startMonth AND :endMonth";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("startMonth", startMonth);
        query.setParameter("endMonth", endMonth);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public List<Integer> getAvailableYears() {
        String jpql = "SELECT DISTINCT FUNCTION('YEAR', o.orderDate) FROM Orders o ORDER BY FUNCTION('YEAR', o.orderDate)";
        Query query = em.createQuery(jpql);
        List<Integer> years = query.getResultList();
        return years.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Double> getMonthlyTotalRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, " +
                "SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (month != null && revenue != null) {
                monthlyRevenue.set(month - 1, revenue);
            }
        }
        System.out.println("Total Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    @Override
    public List<Double> getMonthlyRoomRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, " +
                "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (month != null && revenue != null) {
                monthlyRevenue.set(month - 1, revenue);
            }
        }
        System.out.println("Room Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    @Override
    public List<Double> getMonthlyServiceRevenue(int year) {
        List<Double> monthlyRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (month != null && revenue != null) {
                monthlyRevenue.set(month - 1, revenue);
            }
        }
        System.out.println("Service Revenue for year " + year + ": " + monthlyRevenue);
        return monthlyRevenue;
    }

    @Override
    public List<Double> getYearlyRoomRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>(Collections.nCopies(3, 0.0));
        String jpql = "SELECT FUNCTION('YEAR', o.orderDate) AS year, " +
                "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) BETWEEN 2023 AND 2025 " +
                "GROUP BY FUNCTION('YEAR', o.orderDate)";
        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (year != null && revenue != null) {
                yearlyRevenue.set(year - 2023, revenue);
            }
        }
        System.out.println("Yearly Room Revenue: " + yearlyRevenue);
        return yearlyRevenue;
    }

    @Override
    public List<Double> getYearlyServiceRevenue() {
        List<Double> yearlyRevenue = new ArrayList<>(Collections.nCopies(3, 0.0));
        String jpql = "SELECT FUNCTION('YEAR', o.orderDate) AS year, SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) BETWEEN 2023 AND 2025 " +
                "GROUP BY FUNCTION('YEAR', o.orderDate)";
        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (year != null && revenue != null) {
                yearlyRevenue.set(year - 2023, revenue);
            }
        }
        System.out.println("Yearly Service Revenue: " + yearlyRevenue);
        return yearlyRevenue;
    }

    @Override
    public List<Double> getQuarterlyRoomRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(Collections.nCopies(4, 0.0));
        String jpql = "SELECT FUNCTION('QUARTER', o.orderDate) AS quarter, " +
                "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o JOIN o.room r " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('QUARTER', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer quarter = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (quarter != null && revenue != null) {
                quarterlyRevenue.set(quarter - 1, revenue);
            }
        }
        System.out.println("Quarterly Room Revenue for year " + year + ": " + quarterlyRevenue);
        return quarterlyRevenue;
    }

    @Override
    public List<Double> getQuarterlyServiceRevenue(int year) {
        List<Double> quarterlyRevenue = new ArrayList<>(Collections.nCopies(4, 0.0));
        String jpql = "SELECT FUNCTION('QUARTER', o.orderDate) AS quarter, SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('QUARTER', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer quarter = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (quarter != null && revenue != null) {
                quarterlyRevenue.set(quarter - 1, revenue);
            }
        }
        System.out.println("Quarterly Service Revenue for year " + year + ": " + quarterlyRevenue);
        return quarterlyRevenue;
    }

    @Override
    public List<Double> getRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) {
            String jpql = "SELECT FUNCTION('DATE', o.orderDate) AS date, " +
                    "SUM(o.totalPrice + o.depositAmount) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('DATE', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Object[] result : results) {
                Date date = (Date) result[0];
                Double revenue = (Double) result[1];
                if (date != null && revenue != null) {
                    revenueMap.put(sdf.format(date), revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentDateStr, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) {
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate), " +
                    "SUM(o.totalPrice + o.depositAmount) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer month = (Integer) result[0];
                Integer year = (Integer) result[1];
                Double revenue = (Double) result[2];
                if (month != null && year != null && revenue != null) {
                    String key = String.format("%02d/%d", month, year);
                    revenueMap.put(key, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            for (String label : labels) {
                String currentMonthYear = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentMonthYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else {
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), " +
                    "SUM(o.totalPrice + o.depositAmount) " +
                    "FROM Orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<Integer, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer year = (Integer) result[0];
                Double revenue = (Double) result[1];
                if (year != null && revenue != null) {
                    revenueMap.put(year, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = revenueMap.getOrDefault(currentYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Total Revenue by Date Range: " + revenueData);
        return revenueData;
    }

    @Override
    public double getServiceRevenueByDateRange1(Date startDate, Date endDate) {
        String jpql = "SELECT SUM(od.lineTotalAmount) " +
                "FROM OrderDetails od JOIN od.orders o " +
                "WHERE o.orderDate BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        Double result = (Double) query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public List<String> getDateRangeLabels(Date startDate, Date endDate) {
        List<String> labels = new ArrayList<>();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        if (diffInDays <= 60) {
            while (!startCal.after(endCal)) {
                labels.add(dayFormat.format(startCal.getTime()));
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) {
            while (!startCal.after(endCal)) {
                labels.add(monthFormat.format(startCal.getTime()));
                startCal.add(Calendar.MONTH, 1);
            }
        } else {
            while (!startCal.after(endCal)) {
                labels.add(yearFormat.format(startCal.getTime()));
                startCal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Date Range Labels: " + labels);
        return labels;
    }

    @Override
    public List<Double> getRoomRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) {
            String jpql = "SELECT FUNCTION('DATE', o.orderDate) AS date, " +
                    "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                    "FROM Orders o JOIN o.room r " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('DATE', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Object[] result : results) {
                Date date = (Date) result[0];
                Double revenue = (Double) result[1];
                if (date != null && revenue != null) {
                    revenueMap.put(sdf.format(date), revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentDateStr, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) {
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate), " +
                    "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                    "FROM Orders o JOIN o.room r " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer month = (Integer) result[0];
                Integer year = (Integer) result[1];
                Double revenue = (Double) result[2];
                if (month != null && year != null && revenue != null) {
                    String key = String.format("%02d/%d", month, year);
                    revenueMap.put(key, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            for (String label : labels) {
                String currentMonthYear = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentMonthYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else {
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), " +
                    "SUM((r.price * o.numberOfNights) + o.taxAmount + COALESCE(o.overstayFee, 0)) " +
                    "FROM Orders o JOIN o.room r " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<Integer, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer year = (Integer) result[0];
                Double revenue = (Double) result[1];
                if (year != null && revenue != null) {
                    revenueMap.put(year, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = revenueMap.getOrDefault(currentYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Room Revenue by Date Range: " + revenueData);
        return revenueData;
    }

    @Override
    public List<Double> getServiceRevenueByDateRange(Date startDate, Date endDate) {
        List<Double> revenueData = new ArrayList<>();
        List<String> labels = getDateRangeLabels(startDate, endDate);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        if (diffInDays <= 60) {
            String jpql = "SELECT FUNCTION('DATE', o.orderDate) AS date, SUM(od.lineTotalAmount) " +
                    "FROM OrderDetails od JOIN od.orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('DATE', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Object[] result : results) {
                Date date = (Date) result[0];
                Double revenue = (Double) result[1];
                if (date != null && revenue != null) {
                    revenueMap.put(sdf.format(date), revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                String currentDateStr = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentDateStr, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else if (diffInDays <= 365) {
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate), SUM(od.lineTotalAmount) " +
                    "FROM OrderDetails od JOIN od.orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('MONTH', o.orderDate), FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<String, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer month = (Integer) result[0];
                Integer year = (Integer) result[1];
                Double revenue = (Double) result[2];
                if (month != null && year != null && revenue != null) {
                    String key = String.format("%02d/%d", month, year);
                    revenueMap.put(key, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            for (String label : labels) {
                String currentMonthYear = sdf.format(cal.getTime());
                double revenue = revenueMap.getOrDefault(currentMonthYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.MONTH, 1);
            }
        } else {
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), SUM(od.lineTotalAmount) " +
                    "FROM OrderDetails od JOIN od.orders o " +
                    "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY FUNCTION('YEAR', o.orderDate)";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();
            Map<Integer, Double> revenueMap = new HashMap<>();
            for (Object[] result : results) {
                Integer year = (Integer) result[0];
                Double revenue = (Double) result[1];
                if (year != null && revenue != null) {
                    revenueMap.put(year, revenue);
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (String label : labels) {
                int currentYear = cal.get(Calendar.YEAR);
                double revenue = revenueMap.getOrDefault(currentYear, 0.0);
                revenueData.add(revenue);
                cal.add(Calendar.YEAR, 1);
            }
        }

        System.out.println("Service Revenue by Date Range: " + revenueData);
        return revenueData;
    }

    @Override
    public Map<Integer, Double> getRevenueByStatus(int year) {
        String jpql = "SELECT o.status, SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY o.status";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        Map<Integer, Double> revenueByStatus = new HashMap<>();
        for (Object[] result : results) {
            Integer status = (Integer) result[0];
            Double revenue = (Double) result[1];
            if (status != null && revenue != null) {
                revenueByStatus.put(status, revenue);
            }
        }
        return revenueByStatus;
    }

    @Override
    public Map<PaymentMethod, Double> getRevenueByPaymentMethod(int year) {
        String jpql = "SELECT o.paymentMethod, SUM(o.totalPrice + o.depositAmount) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY o.paymentMethod";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        Map<PaymentMethod, Double> revenueByPaymentMethod = new HashMap<>();
        for (Object[] result : results) {
            PaymentMethod method = (PaymentMethod) result[0];
            Double revenue = (Double) result[1];
            if (method != null && revenue != null) {
                revenueByPaymentMethod.put(method, revenue);
            }
        }
        return revenueByPaymentMethod;
    }

    @Override
    public Map<String, Double> getFeeBreakdown(int year) {
        String jpql = "SELECT SUM(o.taxAmount), SUM(o.serviceFee), SUM(COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        Object[] result = (Object[]) query.getSingleResult();
        Map<String, Double> feeBreakdown = new HashMap<>();
        feeBreakdown.put("taxAmount", result[0] != null ? (Double) result[0] : 0.0);
        feeBreakdown.put("serviceFee", result[1] != null ? (Double) result[1] : 0.0);
        feeBreakdown.put("overstayFee", result[2] != null ? (Double) result[2] : 0.0);
        return feeBreakdown;
    }

    @Override
    public List<Map<String, Double>> getMonthlyFeeBreakdown(int year) {
        List<Map<String, Double>> monthlyBreakdown = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            monthlyBreakdown.add(new HashMap<>());
            monthlyBreakdown.get(i).put("taxAmount", 0.0);
            monthlyBreakdown.get(i).put("serviceFee", 0.0);
            monthlyBreakdown.get(i).put("overstayFee", 0.0);
        }

        String jpql = "SELECT FUNCTION('MONTH', o.orderDate) AS month, " +
                "SUM(o.taxAmount), SUM(o.serviceFee), SUM(COALESCE(o.overstayFee, 0)) " +
                "FROM Orders o " +
                "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                "GROUP BY FUNCTION('MONTH', o.orderDate)";
        Query query = em.createQuery(jpql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double taxAmount = (Double) result[1];
            Double serviceFee = (Double) result[2];
            Double overstayFee = (Double) result[3];
            if (month != null) {
                Map<String, Double> monthBreakdown = monthlyBreakdown.get(month - 1);
                monthBreakdown.put("taxAmount", taxAmount != null ? taxAmount : 0.0);
                monthBreakdown.put("serviceFee", serviceFee != null ? serviceFee : 0.0);
                monthBreakdown.put("overstayFee", overstayFee != null ? overstayFee : 0.0);
            }
        }
        System.out.println("Monthly Fee Breakdown for year " + year + ": " + monthlyBreakdown);
        return monthlyBreakdown;
    }
}