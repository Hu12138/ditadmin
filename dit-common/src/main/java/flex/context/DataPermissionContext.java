package flex.context;

import flex.enums.DataScope;
import lombok.Data;

import java.util.List;

public class DataPermissionContext {


    private static final ThreadLocal<DataPermissionInfo> context = new ThreadLocal<>();

    public static void set(DataPermissionInfo permission) { context.set(permission); }

    public static DataPermissionInfo get() { return context.get(); }

    public static void clear() { context.remove(); }
}
