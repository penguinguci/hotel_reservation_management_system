package entities;

import java.io.Serializable;

public enum Role implements Serializable {
    STAFF("Nhân viên lễ tân"), MANAGER("Nhân viên quản lý");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
