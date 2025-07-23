package flex.enums;

public enum DataScope {
    /**
     * 所有
     */
    ALL("1"),
    /**
     * 自定义（需要根据角色去查部门）
     */
    CUSTOM("2"),
    DEPT_ONLY("3"),
    DEPT_AND_CHILD("4"),
    SELF("5");
    private final String code;

    DataScope(String code) { this.code = code; }

    public static DataScope from(String code) {
        for (DataScope scope : values()) {
            if (scope.code.equals(code)) return scope;
        }
        return SELF;
    }
}
