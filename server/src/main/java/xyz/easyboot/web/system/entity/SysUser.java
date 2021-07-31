package xyz.easyboot.web.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.easyboot.common.orm.entity.AuditableEntity;
import xyz.easyboot.common.util.PasswordUtil;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.List;

/**
 * 系统账号
 * @author wujiawei
 * @see
 * @since 2021/6/29 11:07 下午
 */
@Entity
@Table(
        name = "sys_user",
        indexes = {
                @Index(name = "index_username", columnList = "username"),
                @Index(name = "index_phone", columnList = "phone")
        }
)
public class SysUser extends AuditableEntity {
    
    public String username;
    public String phone;
    public String email;
    public String avatar;
    public String description;
    
    @JsonIgnore
    public String password;
    public Boolean enabled;
    public String lastLoginIp;
    public String lastLoginTime;
    
    public static SysUser findByUsername(String username){
        return find("username", username).firstResult();
    }
    
    public static SysUser findByPhoneAndPassword(String phone, String password) {
        password = PasswordUtil.encrypt(password);
        return SysUser.find("phone = ?1 and password = ?2", phone, password).firstResult();
    }
    
    public static List<SysUser> findEnabled() {
        return list("enabled", Boolean.TRUE);
    }
    
}


