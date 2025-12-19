package io.lighting.web.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lighting.common.orm.entity.AuditableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

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

    public String userId;
    
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
