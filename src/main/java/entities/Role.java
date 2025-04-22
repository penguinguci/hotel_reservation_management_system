package entities;

import java.io.Serializable;

public enum Role implements Serializable {
    STAFF("Nhân viên lễ tân"), MANAGER("Nhân viên quản lý");
    private static final long serialVersionUID = 1L;
    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
