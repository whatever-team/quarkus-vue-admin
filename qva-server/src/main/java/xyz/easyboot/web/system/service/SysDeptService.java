package xyz.easyboot.web.system.service;

import cn.hutool.core.collection.CollectionUtil;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.dto.SysDeptDTO;
import xyz.easyboot.web.system.entity.SysDept;
import xyz.easyboot.web.system.entity.SysUserDept;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@ApplicationScoped
@Transactional
public class SysDeptService {
    
    /**
     * 将自身Menu转换为树结构
     * @param list
     * @param parentId
     * @return
     */
    public List<SysDeptDTO> convertMenuToTree(List<SysDept> list, String parentId) {
        List<SysDeptDTO> resultList = new ArrayList<>();
        for (SysDept data : list) {
            if (Objects.equals(data.parentId, parentId)) {
                SysDeptDTO treeDTO = new SysDeptDTO();
                BeanUtil.copyProperties(data, treeDTO);
                List<SysDeptDTO> children = convertMenuToTree(list, treeDTO.id);
                if (CollectionUtil.isNotEmpty(children)) {
                    treeDTO.children = children;
                }
                resultList.add(treeDTO);
            }
        }
        return resultList;
    }
    
    @Transactional
    public void delete(String...ids) {
        List<SysDept> deptList = SysDept.listAll();
    
        // 获取所有的子节点
        List<String> childrenIdList = new ArrayList<>();
        List<SysDept> children = new ArrayList<>();
        for (String id : ids) {
            children.addAll(findAllChildren(id, deptList));
            childrenIdList.add(id);
        }
        children = children.stream().distinct().collect(Collectors.toList());
        childrenIdList.addAll(children.stream().map(x -> x.id).toList());
    
        // 删除当前节点与所有子节点
        SysDept.delete("id in (?1)", childrenIdList);
    
        // 删除user_dept的当前节点与所有子节点
        SysUserDept.delete("deptId in (?1)", childrenIdList);
    }
    
    /**
     * 根据PID查找所有的子节点
     * @param parentId
     * @param list 所有的数据（平级展示）
     * @return
     */
    public List<SysDept> findAllChildren(String parentId, List<SysDept> list) {
        List<SysDept> children = new ArrayList<>();
        for (SysDept menu : list) {
            if (Objects.equals(menu.parentId, parentId)) {
                children.add(menu);
                List<SysDept> l = findAllChildren(menu.id, list);
                children.addAll(l);
            }
        }
        return children;
    }
}
