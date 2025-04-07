//package data;
//
//import entities.*;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;
//import net.datafaker.Faker;
//import org.checkerframework.checker.units.qual.C;
//
//import javax.xml.crypto.Data;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//public class DataGenerator {
//    private Faker faker = new Faker();
//    private final Random rd = new Random();
//
//    // tạo một đối tượng Staff giả lập
//    public Staff generateStaff() {
//        Staff staff = new Staff();
//        staff.setStaffId(UUID.randomUUID().toString());
//        staff.setFirstName(faker.name().firstName());
//        staff.setLastName(faker.name().lastName());
//        staff.setGender(Gender.values()[rd.nextInt(Gender.values().length)]);
//        staff.setDateOfBirth(new Date(faker.date().birthday().getTime())); // Chuyển đổi java.util.Date thành java.sql.Date
//        staff.setPhoneNumbers(new HashSet<>(Arrays.asList(faker.phoneNumber().cellPhone())));
//        staff.setAddress(new Address(
//                faker.address().streetAddress(),
//                faker.address().city(),
//                faker.address().state(),
//                faker.address().country(),
//                faker.address().zipCode()
//        ));
//        staff.setEmail(faker.internet().emailAddress());
//        staff.setStaffImage(faker.internet().image());
//        staff.setRole(rd.nextBoolean() ? Role.STAFF : Role.MANAGER);
//        staff.setDateOfJoin(new Date(faker.date().past(1000, TimeUnit.DAYS).getTime())); // Chuyển đổi java.util.Date thành java.sql.Date
//        staff.setStatus(rd.nextBoolean());
//
//        return staff;
//    }
//
//    // tạo một đối tượng Account giả lập
//    public Account generateAccount(Staff staff) {
//        Account account = new Account();
//        account.setUsername(faker.internet().username());
//        account.setPassword(faker.internet().password());
//        account.setStaff(staff);
//
//        return account;
//    }
//
//    // tạo một đối tượng Customer giả lập
//    public Customer generateCustomer() {
//        Customer customer = new Customer();
//        customer.setCustomerId(UUID.randomUUID().toString());
//        customer.setFirstName(faker.name().firstName());
//        customer.setLastName(faker.name().lastName());
//        customer.setGender(Gender.values()[rd.nextInt(Gender.values().length)]);
//        customer.setDateOfBirth(faker.date().birthday());
//        customer.setPhoneNumber(faker.phoneNumber().cellPhone());
//        customer.setEmail(rd.nextBoolean() ? faker.internet().emailAddress() : null);
//        customer.setAddress(new Address(
//                faker.address().streetAddress(),
//                faker.address().city(),
//                faker.address().state(),
//                faker.address().country(),
//                faker.address().zipCode()
//        ));
//
//        return customer;
//    }
//
//    // tạo một đối tượng BonusPoint
////    public BonusPoint generateBonusPoint(Customer customer) {
////        BonusPoint bonusPoint = new BonusPoint();
////        bonusPoint.setBonusPointId(UUID.randomUUID().toString());
////        bonusPoint.setCustomer(customer);
////        bonusPoint.setTotalPoint(faker.number().randomDouble(2, 100, 5000));
////        bonusPoint.setLastUpdate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
////        bonusPoint.setExpirationDate(Date.from(LocalDate.now().plusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()));
////        bonusPoint.setRank(Rank.values()[rd.nextInt(Rank.values().length)]);
////        return bonusPoint;
////    }
//
//    // tạo một đối tượng Room giả lập
//    // standard room
//    public StandardRoom createStandardRoom() {
//        StandardRoom room = new StandardRoom();
//        room.setRoomId(UUID.randomUUID().toString());
//        room.setPricePerNight(faker.number().randomDouble(2, 50, 300));
//        room.setCapacity(rd.nextInt(4) + 1);
//        room.setDescription(faker.lorem().paragraph());
//        room.setStatus(rd.nextInt(2));
//        room.setAmenities(Arrays.asList("Wifi", "Điều hòa"));
//        room.setRoomSize(rd.nextDouble() * 50 + 20);
//        room.setHasBasicTV(rd.nextBoolean());
//        room.setHasSharedBathroom(rd.nextBoolean());
//        room.setBedType(rd.nextBoolean() ? "Đôi" : "Đơn");
//        room.setHasMiniBar(rd.nextBoolean());
//        return room;
//    }
//
//    // deluxe room
//    public DeluxeRoom createDeluxeRoom() {
//        DeluxeRoom room = new DeluxeRoom();
//        room.setRoomId(UUID.randomUUID().toString());
//        room.setPricePerNight(faker.number().randomDouble(2, 100, 500));
//        room.setCapacity(rd.nextInt(6) + 2);
//        room.setDescription(faker.lorem().paragraph());
//        room.setStatus(rd.nextInt(2));
//        room.setAmenities(Arrays.asList("Smart TV", "Wifi tốc độ cao"));
//        room.setRoomSize(rd.nextDouble() * 70 + 30);
//        room.setHasBalcony(rd.nextBoolean());
//        room.setLuxuryDecor(faker.lorem().word());
//        room.setSoundproofingLevel(rd.nextBoolean() ? "Cao" : "Trung bình");
//        room.setIncludesBreakfast(rd.nextBoolean());
//        room.setUpgradedAmenities(Arrays.asList("Minibar", "Spa"));
//        return room;
//    }
//
//    // suite room
//    public SuiteRoom createSuiteRoom() {
//        SuiteRoom room = new SuiteRoom();
//        room.setRoomId(UUID.randomUUID().toString());
//        room.setPricePerNight(faker.number().randomDouble(2, 200, 1000));
//        room.setCapacity(rd.nextInt(10) + 4);
//        room.setDescription(faker.lorem().paragraph());
//        room.setStatus(rd.nextInt(2));
//        room.setAmenities(Arrays.asList("Bồn tắm nước nóng", "Bếp riêng"));
//        room.setRoomSize(rd.nextDouble() * 120 + 50);
//        room.setHasPrivateKitchen(rd.nextBoolean());
//        room.setHasLivingArea(rd.nextBoolean());
//        room.setHasJacuzzi(rd.nextBoolean());
//        room.setNumberOfBedrooms(rd.nextInt(5) + 1);
//        room.setRoomView(rd.nextBoolean() ? "View biển": "View núi");
//        room.setExclusiveServices(Arrays.asList("Dịch vụ quản gia", "Hồ bơi riêng"));
//        return room;
//    }
//
//    public Room generateRoom() {
//        Room room = null;
//        int roomType = rd.nextInt(3);
//        switch (roomType) {
//            case 0:
//                room = createSuiteRoom();
//                break;
//            case 1:
//                room = createStandardRoom();
//                break;
//            case 2:
//                room = createDeluxeRoom();
//                break;
//        }
//        return room;
//    }
//
//    // tạo một đối tượng Service giả lập
//    public Service generateService(Promotion promotion) {
//        Service service = new Service();
//        service.setServiceId(rd.nextInt(100, 9999));
//        service.setName(faker.commerce().productName());
//        service.setDescription(faker.lorem().sentence());
//        service.setPrice(faker.number().randomDouble(2, 50, 1000));
//        service.setAvailability(faker.bool().bool());
//        service.setPromotion(promotion);
//        return service;
//    }
//
//    // tạo một đối tượng Promotion giả lập
//    public Promotion generatePromotion() {
//        Promotion promotion = new Promotion();
//        promotion.setPromotionId(UUID.randomUUID().toString());
//        promotion.setName(faker.commerce().promotionCode());
//
//        String description = faker.lorem().paragraph();
//        promotion.setDescription(description.length() > 255 ? description.substring(0, 255) : description);
//        promotion.setStartDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        promotion.setEndDate(Date.from(LocalDate.now().plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        promotion.setDiscountPercentage(faker.number().randomDouble(2, 5, 50));
//        return promotion;
//    }
//
//    // tạo một đối tượng Promotion giả lập
//    public Tax generateTax() {
//        Tax tax = new Tax();
//        tax.setTaxId(UUID.randomUUID().toString());
//
//        String taxName = faker.commerce().department();
//        tax.setTaxName(taxName.length() > 30 ? taxName.substring(0, 30) : taxName);
//        tax.setTaxName(faker.commerce().department());
//        tax.setTaxRate(faker.number().randomDouble(2, 1, 20));
//        tax.setDescription(faker.lorem().sentence(10));
//        tax.setApplicable(faker.bool().bool());
//        return tax;
//    }
//
//    // tạo một đối tượng Order giả lập
//    public Orders generateOrders(Customer customer, Staff staff, Tax tax) {
//        Orders orders = new Orders();
//        orders.setOrderId(UUID.randomUUID().toString());
//        orders.setCustomer(customer);
//        orders.setStaff(staff);
//        orders.setTax(tax);
//        orders.setOrderDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        orders.setStatus(faker.number().numberBetween(0, 1));
//        orders.setPaymentMethod(PaymentMethod.values()[rd.nextInt(PaymentMethod.values().length)]);
//        orders.setTaxPrice(orders.getTaxPrice());
//        orders.setPromotionPrice(orders.getPromotionPrice());
//        orders.setTotalPrice(orders.getTotalPrice());
//        return orders;
//    }
//
//    // tạo một đối tượng OrderDetail giả lập
//    public OrderDetails generateOrderDetails(Orders orders, Room room, Service service) {
//        OrderDetails orderDetails = new OrderDetails();
//        orderDetails.setOrders(orders);
//        orderDetails.setRoom(room);
//        orderDetails.setService(service);
//        orderDetails.setQuantity(faker.number().numberBetween(1, 10));
//        orderDetails.setLineTotalAmount(orderDetails.getLineTotalAmount());
//        return orderDetails;
//    }
//
//    // hàm hiển thị dữ liệu mẫu
//    public void generateAndPrintData() {
//        EntityManager em = Persistence.createEntityManagerFactory("mariadb")
//                .createEntityManager();
//        EntityTransaction tr = em.getTransaction();
//
//        for (int i = 0; i < 30; i++) {
//            Staff staff = generateStaff();
//            Account account = generateAccount(staff);
//            Customer customer = generateCustomer();
//            BonusPoint bonusPoint = generateBonusPoint(customer);
//            Room room = generateRoom();
//            Promotion promotion = generatePromotion();
//            Service service = generateService(promotion);
//            Tax tax = generateTax();
//            Orders orders = generateOrders(customer, staff, tax);
//            OrderDetails orderDetails = generateOrderDetails(orders, room, service);
//            tr.begin();
//                em.persist(staff);
//                em.persist(account);
//                em.persist(customer);
//                em.persist(bonusPoint);
//                em.persist(room);
//                em.persist(promotion);
//                em.persist(service);
//                em.persist(tax);
//                em.persist(orders);
//                em.persist(orderDetails);
//            tr.commit();
//        }
//    }
//
//    public static void main(String[] args) {
//        DataGenerator generator = new DataGenerator();
//        generator.generateAndPrintData();
//    }
//}
