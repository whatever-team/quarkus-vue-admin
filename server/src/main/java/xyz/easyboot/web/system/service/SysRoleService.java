package xyz.easyboot.web.system.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.dto.SysRoleDTO;
import xyz.easyboot.web.system.entity.SysRole;
import xyz.easyboot.web.system.entity.SysRoleMenu;
import xyz.easyboot.web.system.entity.SysUserRole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@ApplicationScoped
@Transactional
public class SysRoleService {
    
    @Inject
    Session session;
    
    /**
     * 补充关联的菜单数据
     * @param roles
     * @return
     */
    public List<SysRoleDTO> completeMenus(List<SysRole> roles) {
        return roles.stream().map(x -> {
            List<SysRoleMenu> relations = SysRoleMenu.list("roleId", x.id);
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtil.copyProperties(x, dto);
            dto.menuIds = relations.stream().map(re -> re.menu.id.toString()).collect(Collectors.toList());
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void updateMenus(SysRoleDTO dto) {
        // 获取当前绑定的菜单
        List<SysRoleMenu> relations = SysRoleMenu.list("roleId", dto.id);
        List<String> menuIdsInDb = relations.stream().map(re -> re.menu.id.toString()).collect(Collectors.toList());
        
        // 新产生的菜单
        List<String> menuIdsNew = dto.menuIds;
        
        // 删除表里有但新数据里没有的
        List<Long> deleteMenuIds = menuIdsInDb.stream()
                .filter(idInDb -> !menuIdsNew.contains(idInDb))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (!deleteMenuIds.isEmpty()) {
            Query deleteQuery = session.createQuery("delete from SysRoleMenu where roleId = :roleId and menuId in (:menuId)");
            deleteQuery.setParameter("roleId", dto.id);
            deleteQuery.setParameterList("menuId", deleteMenuIds);
            deleteQuery.executeUpdate();
        }
        
        // 录入新数据有但表里没有的
        List<SysRoleMenu> addRelations = menuIdsNew.stream().filter(idNew -> !menuIdsInDb.contains(idNew)).map(id -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.roleId = dto.id;
            roleMenu.menuId = Long.parseLong(id);
            roleMenu.seq = 0L;
            return roleMenu;
        }).collect(Collectors.toList());
        if (!addRelations.isEmpty()) {
            SysRoleMenu.persist(addRelations);
        }
    }
    
    @Transactional
    public void delete(String...ids) {
        List<Long> idList = Arrays.stream(ids).map(Long::parseLong).collect(Collectors.toList());
    
        // role
        SysRole.delete("id in (?1)", idList);
    
        // role_menu
        SysRoleMenu.delete("roleId in (?1)", idList);
        
        // user_role
        SysUserRole.delete("roleId in (?1)", idList);
    }

}
