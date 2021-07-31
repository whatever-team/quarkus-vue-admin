package xyz.easyboot.web.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/11 5:19 下午
 */
public class SysUserDTO {
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;
    
    public String username;
    public String phone;
    public String email;
    public String avatar;
    public String description;
    
    public Boolean enabled;
    public String lastLoginIp;
    public String lastLoginTime;
    public List<String> roleIds;
    public String deptId;
}
