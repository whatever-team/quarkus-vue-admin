package xyz.easyboot.web.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 10:28 下午
 */
@Entity
@Table(
        name = "sys_role_menu",
        indexes = {
                @Index(name = "index_role_id", columnList = "roleId"),
                @Index(name = "index_menu_id", columnList = "menuId"),
        }
)
public class SysRoleMenu extends AuditableEntity {
    
    public Long roleId;
    
    public Long menuId;
    
    @JsonIgnore
    @JoinColumn(name = "menuId", insertable = false, updatable = false)
    @ManyToOne
    public SysMenu menu;
    
}
