package entities;


import java.io.Serializable;

public enum PaymentMethod implements Serializable {
    CASH("Tiền mặt"), E_WALLET("Ví điện tử"), CREDIT_CARD("Thẻ tín dụng"), BANK_TRANSFER("Chuyển khoản ngân hàng");

    private String name;
    private PaymentMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
