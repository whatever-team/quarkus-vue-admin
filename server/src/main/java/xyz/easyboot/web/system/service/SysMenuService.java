package xyz.easyboot.web.system.service;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.dto.RouteDTO;
import xyz.easyboot.web.system.dto.SysMenuTreeDTO;
import xyz.easyboot.web.system.entity.SysMenu;
import xyz.easyboot.web.system.entity.SysRoleMenu;
import xyz.easyboot.web.system.entity.SysUserRole;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@Slf4j
@ApplicationScoped
@Transactional
public class SysMenuService {
    
    /**
     * 生成前端路由数据结构
     * @param userId
     * @return
     */
    public List<RouteDTO> generateRoute(Long userId) {
        List<SysUserRole> userRoles = SysUserRole.find("userId", userId).list();
        List<Long> roleIds = userRoles.stream().map(x -> x.roleId).collect(Collectors.toList());
        List<SysRoleMenu> roleMenus = SysRoleMenu.list("roleId in (?1)", roleIds);
        List<SysMenu> menus = roleMenus.stream()
                .filter(x -> x.menu != null && x.menu.enabled == Boolean.TRUE && x.menu.type != SysMenu.TYPE_BUTTON)
                .map(x -> x.menu)
                .collect(Collectors.toList())
                .stream().distinct()
                .collect(Collectors.toList())
                .stream().distinct()
                .collect(Collectors.toList());
        // 转换树结构
        List<RouteDTO> root = new ArrayList<>();
        RouteDTO rootNode = new RouteDTO();
        rootNode.setRouter("root");
        rootNode.setChildren(convertMenuToRouteTree(menus, SysMenu.TOP_PARENT_ID));
        root.add(rootNode);
        return root;
    }
    
    /**
     * 转换菜单为前端路由的树
     * @param list
     * @param parentId
     * @return
     */
    private List<RouteDTO> convertMenuToRouteTree(List<SysMenu> list, long parentId) {
        List<RouteDTO> resultList = new ArrayList<>();
        for (SysMenu menu : list) {
            if (menu.parentId == parentId) {
                RouteDTO route = new RouteDTO();
                route.setIcon(menu.icon);
                route.setName(menu.menuName);
                route.setRouter(menu.routerName);
                if (menu.target == SysMenu.TARGET_BLANK) {
                    route.setLink(menu.path);
                }
                
                List<RouteDTO> children = convertMenuToRouteTree(list, menu.id);
                if (CollectionUtil.isNotEmpty(children)) {
                    route.setChildren(children);
                }
                resultList.add(route);
            }
        }
        return resultList;
    }
    
    /**
     * 将自身Menu转换为树结构
     * @param list
     * @param parentId
     * @return
     */
    public List<SysMenuTreeDTO> convertMenuToTree(List<SysMenu> list, long parentId) {
        List<SysMenuTreeDTO> resultList = new ArrayList<>();
        for (SysMenu menu : list) {
            if (menu.parentId == parentId) {
                SysMenuTreeDTO treeDTO = new SysMenuTreeDTO();
                BeanUtil.copyProperties(menu, treeDTO);
                List<SysMenuTreeDTO> children = convertMenuToTree(list, treeDTO.getId());
                if (CollectionUtil.isNotEmpty(children)) {
                    treeDTO.setChildren(children);
                }
                resultList.add(treeDTO);
            }
        }
        return resultList;
    }
    
    @Transactional
    public void delete(String...ids) {
        List<SysMenu> menus = SysMenu.listAll();
        
        // 获取所有的子节点
        List<Long> childrenIdList = new ArrayList<>();
        List<SysMenu> children = new ArrayList<>();
        for (String id : ids) {
            children.addAll(findAllChildren(Long.parseLong(id), menus));
            childrenIdList.add(Long.parseLong(id));
        }
        children = children.stream().distinct().collect(Collectors.toList());
        childrenIdList.addAll(children.stream().map(x -> x.id).collect(Collectors.toList()));
        
        // 删除当前节点与所有子节点
        SysMenu.delete("id in (?1)", childrenIdList);
        
        // 删除role_menu的当前节点与所有子节点
        SysRoleMenu.delete("menuId in (?1)", childrenIdList);
    }
    
    /**
     * 根据PID查找所有的子节点
     * @param parentId
     * @param menus
     * @return
     */
    public List<SysMenu> findAllChildren(long parentId, List<SysMenu> menus) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.parentId == parentId) {
                children.add(menu);
                List<SysMenu> l = findAllChildren(menu.id, menus);
                children.addAll(l);
            }
        }
        return children;
    }
}
