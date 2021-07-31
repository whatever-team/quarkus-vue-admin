package xyz.easyboot.web.system.entity;

import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 系统角色
 * @author wujiawei
 * @see
 * @since 2021/7/2 11:12 上午
 */
@Entity
@Table(
        name = "sys_role",
        indexes = {
                @Index(name = "index_role_name", columnList = "roleName"),
                @Index(name = "index_role_code", columnList = "roleCode"),
                @Index(name = "index_enabled", columnList = "enabled")
        }
)
public class SysRole extends AuditableEntity {
    
    public String roleName;
    public String roleCode;
    public Boolean enabled;
    
}
