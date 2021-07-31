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
        name = "sys_user_dept",
        indexes = {
            @Index(name = "index_user_id", columnList = "userId"),
            @Index(name = "index_dept_id", columnList = "deptId"),
        }
)
public class SysUserDept extends AuditableEntity {

    public Long userId;
    
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
