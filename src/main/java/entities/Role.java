package entities;

public enum Role {
    STAFF("Nhân viên"), MANAGER("Nhân viên quản lý");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
