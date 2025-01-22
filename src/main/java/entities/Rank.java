package entities;

public enum Rank {
    BRONZE("Đồng"), SILVER("Bạc"), GOLD("Vàng"), PLATINUM("Bạch kim"), DIAMOND("Kim cương");

    private String name;

    private Rank(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
