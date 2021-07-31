package xyz.easyboot.web.system.entity;

import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 系统变量
 * @author wujiawei
 * @see
 * @since 2021/7/24 2:26 下午
 */
@Entity
@Table(
        name = "sys_var",
        indexes = {
                @Index(name = "index_var_code", columnList = "varCode"),
        }
)
public class SysVar extends AuditableEntity {
    
    public String varCode;
    public String varName;
    public String varValue;
    
}
