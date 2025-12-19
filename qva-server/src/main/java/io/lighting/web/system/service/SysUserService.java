package io.lighting.web.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.quarkus.arc.Lock;
import io.lighting.common.util.PasswordUtil;
import io.lighting.common.util.BeanUtil;
import io.lighting.common.util.StringUtil;
import io.lighting.common.security.TokenUtil;
import io.lighting.common.security.AuthorizationConfig;
import io.lighting.web.common.resource.CaptchaResource;
import io.lighting.web.system.dto.LoginDTO;
import io.lighting.web.system.dto.SysUserDTO;
import io.lighting.web.system.entity.SysRoleMenu;
import io.lighting.web.system.entity.SysUser;
import io.lighting.web.system.entity.SysUserDept;
import io.lighting.web.system.entity.SysUserRole;
import io.lighting.web.system.param.ChangePwdParam;
import io.lighting.web.system.param.LoginParam;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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

    @Inject
    AuthorizationConfig authorizationConfig;
    
    /**
     * 后台用户登录
     * @return
     */
    public LoginDTO login(LoginParam param) {
        var username = param.getUsername();
        var password = param.getPassword();
        
        // 验证码校验
        if (authorizationConfig.captchaEnable()) {
            String codeFromCache = CaptchaResource.TIMED_CACHE.get(param.getT());
            if (StringUtil.isEmpty(codeFromCache)) {
                throw new RuntimeException("验证码已过期");
            }
            if (!StringUtil.equals(param.getCode().toUpperCase(), codeFromCache)) {
                throw new RuntimeException("验证码错误");
            }
            CaptchaResource.TIMED_CACHE.remove(param.getT());
        }

        SysUser entity = SysUser.findByUsernameAndPassword(username, password);
        LoginDTO loginDTO = new LoginDTO();

        // 权限信息
        List<String> roleNames = new ArrayList<>();
        List<LoginDTO.Permission> permissions = new ArrayList<>();
        List<SysUserRole> roles = SysUserRole.list("userId", entity.id);
        for (SysUserRole tmp : roles) {
            var role = tmp.role;
            List<SysRoleMenu> menus = SysRoleMenu.list("roleId", role.id);

            // 组合权限
            LoginDTO.Permission permission = new LoginDTO.Permission();
            permission.setId("*");
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
        loginDTO.setUser(getLoginUserInfo(entity, roleNames));
        
        // 生成Token
        Date expireAt = DateUtil.offsetDay(new Date(), 7);
        String token = TokenUtil.create(entity.id);
        loginDTO.setToken(token);
        loginDTO.setExpireAt(expireAt);
        
        return loginDTO;
    }

    /**
     * 解析User实体类，获取登录账号的用户信息
     * @param entity
     * @param roleNames
     * @return
     */
    public LoginDTO.User getLoginUserInfo(SysUser entity, List<String> roleNames) {
        if (entity.enabled == Boolean.FALSE) {
            throw new RuntimeException("账号已被封禁");
        }

        // 用户信息
        var user = new LoginDTO.User();
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
        return user;
    }
    
    /**
     * 补全用户关联的角色数据和部门
     * @param users
     * @return
     */
    public List<SysUserDTO> completeRolesAndDept(final List<SysUser> users) {
        return users.stream().map(user -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtil.copyPropertiesIgnoreNull(user, dto);

            List<SysUserRole> userRoles = SysUserRole.list("userId", user.id);
            dto.roleIds = userRoles.stream()
                    .map(ur -> ur.role != null ? ur.role.id.toString() : null)
                    .filter(StringUtil::isNotEmpty)
                    .collect(Collectors.toList());

            SysUserDept userDept = SysUserDept.find("userId", user.id).firstResult();
            if (userDept != null && userDept.deptId != null) {
                dto.deptId = userDept.deptId.toString();
            }

            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * 手机号字段校验
     * @param phone
     * @param userId
     */
    public void validPhone(String phone, String userId) {
        StringBuilder query = new StringBuilder("phone = :phone");
        Map<String, Object> args = new HashMap<>();
        args.put("phone", phone);
        if (userId != null) {
            query.append(" and id <> :userId");
            args.put("userId", userId);
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
        if (CollectionUtil.isEmpty(dto.roles)) {
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
        
        SysUser user = SysUser.findById(param.getUserId());
        if (!encryptOldPassword.equals(user.password)) {
            throw new ValidationException("原密码输入错误");
        }
    
        String encryptNewPassword = PasswordUtil.encrypt(param.getNewPassword());
        user.password = encryptNewPassword;
        
        return true;
    }

    @Lock(value = Lock.Type.READ)
    public LoginDTO.User userInfo(String userId) {
        SysUser entity = SysUser.findById(userId);
        return getLoginUserInfo(entity, null);
    }
}
