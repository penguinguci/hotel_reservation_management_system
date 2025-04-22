package interfaces;

import java.util.Date;
import java.util.List;

public interface AmountCustomerDAO {
    List<Integer> getAvailableYears();
    List<String> getDateRangeLabels(Date startDate, Date endDate);
    List<Integer> getCustomerCountByDateRange(Date startDate, Date endDate);
    int getTotalCustomerCountByDateRange(Date startDate, Date endDate);
    List<Integer> getMonthlyCustomerCount(int year);
    List<Integer> getQuarterlyCustomerCount(int year);
    List<Integer> getYearlyCustomerCount();
    int getTotalCustomerCountByQuarter(int year, int quarter);
    int getTotalCustomerCount(int year);
}
