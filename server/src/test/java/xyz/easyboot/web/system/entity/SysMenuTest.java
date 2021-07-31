package xyz.easyboot.web.system.entity;

import io.quarkus.panache.common.Parameters;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.easyboot.web.system.entity.SysMenu;
import xyz.easyboot.web.system.entity.SysRoleMenu;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/5 9:12 下午
 */
@QuarkusTest
public class SysMenuTest {

    @Inject
    Session session;
    @Inject
    EntityManager entityManager;
    
    @Transactional
    @Test
    public void initRoleMenu() {
        long roleId = 1411225867245457408L;
        List<SysMenu> list = SysMenu.findAll().list();
        for (SysMenu menu : list) {
            SysRoleMenu x = SysRoleMenu.find("roleId = ?1 and menuId = ?2", roleId, menu.id).firstResult();
            if (x == null || x.id == null) {
                x = new SysRoleMenu();
                x.roleId = roleId;
                x.menuId = menu.id;
                x.seq = 0L;
            }
            x.persist();
            System.out.println("success, id = " + x.id);
        }
    }
    
    @Transactional
    @Test
    public void deleteBatch() {
//        String[] ids = {"1", "2", "3"};
        Long[] ids = {1L, 2L, 3L};
//        SysMenu.delete("where id in " + ids);
//        long rows = SysMenu.delete("id  = :", 1);
//        System.out.println("complete");
    
        Query query = session.createQuery("delete from SysMenu where id in (:id)");
        query.setParameterList("id", ids);
        int i = query.executeUpdate();
        System.err.println("i = "+i);
        
    }
    
}
