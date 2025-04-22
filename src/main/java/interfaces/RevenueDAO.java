package interfaces;

import entities.PaymentMethod;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RevenueDAO {
    List<Double> getQuarterlyRevenue(int year);
    List<Double> getYearlyRevenue();
    double getRoomRevenueByDateRange1(Date startDate, Date endDate);
    double getTotalRevenueByDateRange(Date startDate, Date endDate);
    List<Double> getMonthlyRevenue(int year);
    double getTotalRevenue(int year);
    double getServiceRevenue(int year);
    double getRoomRevenue(int year);
    List<String> getRoomLabels(int year);
    double getTotalRevenueByMonth(int year, int month);
    double getServiceRevenueByMonth(int year, int month);
    double getRoomRevenueByMonth(int year, int month);
    double getTotalRevenueByQuarter(int year, int quarter);
    double getServiceRevenueByQuarter(int year, int quarter);
    double getRoomRevenueByQuarter(int year, int quarter);
    List<Integer> getAvailableYears();
    List<Double> getMonthlyTotalRevenue(int year);
    List<Double> getMonthlyRoomRevenue(int year);
    List<Double> getMonthlyServiceRevenue(int year);
    List<Double> getYearlyRoomRevenue();
    List<Double> getYearlyServiceRevenue();
    List<Double> getQuarterlyRoomRevenue(int year);
    List<Double> getQuarterlyServiceRevenue(int year);
    List<Double> getRevenueByDateRange(Date startDate, Date endDate);
    double getServiceRevenueByDateRange1(Date startDate, Date endDate);
    List<String> getDateRangeLabels(Date startDate, Date endDate);
    List<Double> getRoomRevenueByDateRange(Date startDate, Date endDate);
    List<Double> getServiceRevenueByDateRange(Date startDate, Date endDate);
    Map<Integer, Double> getRevenueByStatus(int year);
    Map<PaymentMethod, Double> getRevenueByPaymentMethod(int year);
    Map<String, Double> getFeeBreakdown(int year);
    List<Map<String, Double>> getMonthlyFeeBreakdown(int year);
}
