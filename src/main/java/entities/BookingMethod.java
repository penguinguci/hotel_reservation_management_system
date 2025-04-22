package entities;

import java.io.Serializable;

public enum BookingMethod  implements Serializable {
    AT_THE_COUNTER("Tại quầy"), CONTACT("Trực tuyến");

    private String name;
    private BookingMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
