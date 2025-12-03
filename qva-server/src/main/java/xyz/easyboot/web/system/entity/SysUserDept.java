package xyz.easyboot.web.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.easyboot.common.orm.entity.AuditableEntity;

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
        name = "sys_user_dept",
        indexes = {
            @Index(name = "index_user_id", columnList = "userId"),
            @Index(name = "index_dept_id", columnList = "deptId"),
        }
)
public class SysUserDept extends AuditableEntity {

    public String userId;
    
    public Long deptId;
    
    @JsonIgnore
    @JoinColumn(name = "userId", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne
    public SysUser user;
    
    @JsonIgnore
    @JoinColumn(name = "deptId", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne
    public SysDept dept;
    
}
