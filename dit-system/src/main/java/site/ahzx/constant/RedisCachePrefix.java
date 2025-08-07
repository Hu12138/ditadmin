package site.ahzx.constant;

public interface RedisCachePrefix  {

      String USER_ROLE_PREFIX = "system:tenantId:userId:roles:";
      String USER_PERM_PREFIX = "system:tenantId:userId:perms:";
     String USER_MENU_PREFIX = "system:tenantId:userId:menus:";

}
