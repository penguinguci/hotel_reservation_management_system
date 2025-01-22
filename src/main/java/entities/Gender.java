package entities;

public enum Gender {
    MALE("Nam"), FEMALE("Nữ"), OTHER("Khác");

    private String name;

    private Gender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
