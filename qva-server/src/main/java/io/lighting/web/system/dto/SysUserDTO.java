package io.lighting.web.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.OneToMany;
import io.lighting.common.util.BeanUtil;
import io.lighting.web.system.entity.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/11 5:19 下午
 */
public class SysUserDTO {

    public SysUserDTO() {

    }

    public SysUserDTO(SysUser user) {
        BeanUtil.copyPropertiesIgnoreNull(user, this);
        this.roles = user.roles.stream().map(x -> x.role).collect(Collectors.toList());
        this.departments = user.departments.stream().map(x -> x.dept).collect(Collectors.toList());
    }

    public static SysUserDTO of (SysUser user) {
        return new SysUserDTO(user);
    }
    
    public String id;
    
    public String username;
    public String nickName;
    public String phone;
    public String email;
    public String avatar;
    public String description;
    
    public Boolean enabled;
    public String lastLoginIp;
    public String lastLoginTime;

    public List<String> roleIds;
    public String deptId;

    public List<SysRole> roles;
    public List<SysDept> departments;
}
