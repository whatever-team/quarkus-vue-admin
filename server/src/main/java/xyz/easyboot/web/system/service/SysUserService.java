package xyz.easyboot.web.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.PasswordUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.common.util.TokenUtil;
import xyz.easyboot.web.common.resource.CaptchaResource;
import xyz.easyboot.web.system.dto.LoginDTO;
import xyz.easyboot.web.system.dto.SysUserDTO;
import xyz.easyboot.web.system.entity.SysRole;
import xyz.easyboot.web.system.entity.SysRoleMenu;
import xyz.easyboot.web.system.entity.SysUser;
import xyz.easyboot.web.system.entity.SysUserDept;
import xyz.easyboot.web.system.entity.SysUserRole;
import xyz.easyboot.web.system.param.ChangePwdParam;
import xyz.easyboot.web.system.param.LoginParam;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统账号Service
 * @author wujiawei
 * @see
 * @since 2021/7/1 3:44 下午
 */
@ApplicationScoped
@Transactional
public class SysUserService {
    
    /**
     * 后台用户登录
     * @return
     */
    public LoginDTO login(LoginParam param) {
        String name = param.getName();
        String password = param.getPassword();
        
        // 验证码校验
        String codeFromCache = CaptchaResource.TIMED_CACHE.get(param.getT());
        if (!StringUtil.equals(param.getCode().toUpperCase(), codeFromCache)) {
            throw new RuntimeException("验证码错误");
        }
        CaptchaResource.TIMED_CACHE.remove(param.getT());
        
        SysUser entity = SysUser.findByPhoneAndPassword(name, password);
        if (entity == null) {
            throw new RuntimeException("账户名或密码错误");
        }
        if (entity.enabled == Boolean.FALSE) {
            throw new RuntimeException("账号已被封禁");
        }
        LoginDTO loginDTO = new LoginDTO();
        
        // 权限信息
        List<String> roleNames = new ArrayList<>();
        List<LoginDTO.Permission> permissions = new ArrayList<>();
        List<SysUserRole> roles = SysUserRole.list("userId", entity.id);
        for (SysUserRole tmp : roles) {
            SysRole role = tmp.role;
            List<SysRoleMenu> menus = SysRoleMenu.list("roleId", role.id);
    
            // 组合权限
            LoginDTO.Permission permission = new LoginDTO.Permission();
            permission.setId(role.roleCode);
            permission.setOperation(menus.stream().map(x -> {
                if (x != null && x.menu != null && StringUtil.isNotEmpty(x.menu.permission)) {
                    return x.menu.permission;
                }
                return null;
            }).filter(StringUtil::isNotEmpty).collect(Collectors.toList()));
            permissions.add(permission);
            
            // 角色
            if (tmp.role != null && StringUtil.isNotEmpty(tmp.role.roleName)) {
                roleNames.add(tmp.role.roleName);
            }
        }
        loginDTO.setPermissions(permissions);
        
        // 用户信息
        LoginDTO.User user = new LoginDTO.User();
        user.setAvatar(entity.avatar);
        user.setUsername(entity.username);
        user.setPhone(entity.phone);
        user.setDesc(entity.description);
        user.setEmail(entity.email);
        // 角色和部门
        user.setRoleNames(roleNames);
        List<SysUserDept> deptList = SysUserDept.list("userId", entity.id);
        user.setDeptNames(deptList.stream().map(x -> {
            if (x != null && x.dept != null && StringUtil.isNotEmpty(x.dept.deptName)) {
                return x.dept.deptName;
            }
            return null;
        }).filter(StringUtil::isNotEmpty).collect(Collectors.toList()));
    
        loginDTO.setUser(user);
        
        // 生成Token
        Date expireAt = DateUtil.offsetDay(new Date(), 7);
        String token = TokenUtil.create(entity.id);
        loginDTO.setToken(token);
        loginDTO.setExpireAt(expireAt);
        
        return loginDTO;
    }
    
    /**
     * 补全用户关联的角色数据和部门
     * @param users
     * @return
     */
    public List<SysUserDTO> completeRolesAndDept(final List<SysUser> users) {
        return users.stream().map(x -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtil.copyProperties(x, dto);
            
            List<SysUserRole> userRoles = SysUserRole.list("userId", x.id);
            dto.roleIds = userRoles.stream().map(ur -> ur.role.id.toString()).collect(Collectors.toList());
            
            SysUserDept userDept = SysUserDept.find("userId", x.id).firstResult();
            if (userDept != null) {
                dto.deptId = userDept.deptId.toString();
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * 手机号字段校验
     * @param phone
     */
    public void validPhone(String phone, Long id) {
        StringBuilder query = new StringBuilder("phone = :phone");
        Map<String, Object> args = new HashMap<>();
        args.put("phone", phone);
        if (id != null) {
            query.append(" and id <> :id");
            args.put("id", id);
        }
        long count = SysUser
                .find(query.toString(), args)
                .count();
        if (count > 0) {
            throw new RuntimeException("手机号已存在，请重新输入");
        }
    }
    
    /**
     * 更新关联的角色数据
     * @param dto
     */
    public void updateRoles(SysUserDTO dto) {
        if (CollectionUtil.isEmpty(dto.roleIds)) {
            return;
        }
        
        // 获取当前绑定的角色
        List<SysUserRole> relations = SysUserRole.list("userId", dto.id);
        List<String> roleIdsInDb = relations.stream().map(re -> re.role.id.toString()).collect(Collectors.toList());
    
        // 新产生的菜单
        List<String> roleIdsNew = dto.roleIds;
    
        // 删除表里有但新数据里没有的
        List<Long> deleteRoleIds = roleIdsInDb.stream()
                .filter(idInDb -> !roleIdsNew.contains(idInDb))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (!deleteRoleIds.isEmpty()) {
            SysUserRole.delete("roleId in (?1) and userId = ?2", deleteRoleIds, dto.id);
        }
    
        // 录入新数据有但表里没有的
        List<SysUserRole> addRelations = roleIdsNew.stream().filter(idNew -> !roleIdsInDb.contains(idNew)).map(id -> {
            SysUserRole userRole = new SysUserRole();
            userRole.userId = dto.id;
            userRole.roleId = Long.parseLong(id);
            userRole.seq = 0L;
            return userRole;
        }).collect(Collectors.toList());
        if (!addRelations.isEmpty()) {
            SysUserRole.persist(addRelations);
        }
    }
    
    /**
     * 更新关联的部门数据
     * @param dto
     */
    public void updateDept(SysUserDTO dto) {
        if (StringUtil.isEmpty(dto.deptId)) {
            return;
        }
        // 已经绑定的部门
        SysUserDept userDept = SysUserDept.find("userId", dto.id).firstResult();
        if (userDept == null) {
            userDept = new SysUserDept();
            userDept.seq = 0L;
            userDept.deptId = Long.parseLong(dto.deptId);
            userDept.userId = dto.id;
            userDept.persist();
        } else {
            userDept.deptId = Long.parseLong(dto.deptId);
        }
    }
    
    @Transactional
    public void delete(String...ids) {
        List<Long> idList = Arrays.stream(ids).map(Long::parseLong).collect(Collectors.toList());
    
        // role
        SysUser.delete("id in (?1)", idList);
    
        // user_dept
        SysUserDept.delete("userId in (?1)", idList);
    
        // user_role
        SysUserRole.delete("userId in (?1)", idList);
    }
    
    /**
     * 修改密码
     * @param param
     * @return
     */
    public boolean changePassword(ChangePwdParam param) {
        String encryptOldPassword = PasswordUtil.encrypt(param.getOldPassword());
        
        SysUser user = SysUser.findById(param.getId());
        if (!encryptOldPassword.equals(user.password)) {
            throw new ValidationException("原密码输入错误");
        }
    
        String encryptNewPassword = PasswordUtil.encrypt(param.getNewPassword());
        user.password = encryptNewPassword;
        
        return true;
    }
}
