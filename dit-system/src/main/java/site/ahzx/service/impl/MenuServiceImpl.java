package site.ahzx.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.mapper.SysMenusMapper;
import site.ahzx.service.MenuService;
import site.ahzx.constant.RedisCachePrefix;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysMenusMapper sysMenuMapper; // 假设你有这个 mapper 从数据库查菜单

    @Override
    public List<RouterVO> getRouterByUserId(Long userId) {
        try {
            String key = RedisCachePrefix.USER_MENUS_PREFIX + LoginContext.get().getTenantId() + ":" + userId;
            @SuppressWarnings("unchecked")
            List<SysMenu> cachedMenus = (List<SysMenu>) redisTemplate.opsForValue().get(key);

            List<SysMenu> menus;
            if (cachedMenus != null && !cachedMenus.isEmpty()) {
                menus = cachedMenus;
            } else {
                // TODO: 从数据库查询用户权限菜单
                menus = sysMenuMapper.selectMenusByUserId(userId);
                // 缓存
                redisTemplate.opsForValue().set(key, menus, Duration.ofHours(3));
            }
            log.debug("menus are : {}",menus);
            // 构建 RouterVO 树结构
            List<RouterVO> routerList = MenuRouterBuilder.buildMenuTree(menus);
            log.debug("routerList is : {}",routerList);

            return routerList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
