package xyz.easyboot.web.system.service;

import cn.hutool.core.collection.CollectionUtil;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import xyz.easyboot.common.logging.entity.SysBusinessLog;
import xyz.easyboot.web.system.entity.SysMenu;
import xyz.easyboot.web.system.entity.SysRoleMenu;
import xyz.easyboot.web.system.entity.SysUser;
import xyz.easyboot.web.system.entity.SysUserRole;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SysMenuServiceTest {
    
    @Inject
    Logger logger;
    
    @Test
    void generateRoute() {
        long userId = 1411221497711824896L;
        List<SysUserRole> userRoles = SysUserRole.find("userId", userId).list();
        List<Long> roleIds = userRoles.stream().map(x -> x.roleId).collect(Collectors.toList());
        List<SysRoleMenu> roleMenus = SysRoleMenu.list("roleId IN (?1)", roleIds);
        logger.info(roleMenus.size());
    
        List<SysMenu> menus = roleMenus.stream()
                .filter(x -> x.menu != null && x.menu.enabled == Boolean.TRUE && x.menu.type != SysMenu.TYPE_BUTTON)
                .map(x -> x.menu)
                .collect(Collectors.toList());
        logger.info(menus.size());
        
        menus = roleMenus.stream()
                .filter(x -> x.menu != null && x.menu.enabled == Boolean.TRUE && x.menu.type != SysMenu.TYPE_BUTTON)
                .map(x -> x.menu)
                .collect(Collectors.toList())
                .stream().distinct()
                .collect(Collectors.toList());
        logger.info(menus.size());
    }
    
    @Transactional
    @Test
    public void deleteBatch() {
        String[] idArr = {"1414151951951728640", "1414152114971742208"};
        long rows = SysUser.delete("id in (?1)", Arrays.stream(idArr)
                .map(Long::parseLong)
                .collect(Collectors.toList()));
//        long rows = SysMenu.delete("id in (?1)", list);
        logger.info(rows);
    }
}
