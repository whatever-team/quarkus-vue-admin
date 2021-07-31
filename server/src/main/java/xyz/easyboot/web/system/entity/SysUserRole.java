package xyz.easyboot.web.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 2:21 下午
 */
@Entity
@Table(
        name = "sys_user_role",
        indexes = {
            @Index(name = "index_role_id", columnList = "roleId"),
            @Index(name = "index_user_id", columnList = "userId"),
        }
)
public class SysUserRole extends AuditableEntity {

    public Long userId;
    
    public Long roleId;
    
    @JsonIgnore
    @JoinColumn(name = "userId", insertable = false, updatable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne
    public SysUser user;
    
    @JsonIgnore
    @JoinColumn(name = "roleId", insertable = false, updatable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne
    public SysRole role;

}
