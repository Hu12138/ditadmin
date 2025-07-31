package site.ahzx.constant;

public interface RedisCachePrefix  {

      String USER_ROLES_PREFIX = "system:tenantId:userId:roles:";
      String USER_PERMS_PREFIX = "system:tenantId:userId:perms:";
     String USER_MENUS_PREFIX = "system:tenantId:userId:menus:";

}
