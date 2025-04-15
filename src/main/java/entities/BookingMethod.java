package entities;

public enum BookingMethod {
    AT_THE_COUNTER("Tại quầy"), CONTACT("Trực tuyến");

    private String name;
    private BookingMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
