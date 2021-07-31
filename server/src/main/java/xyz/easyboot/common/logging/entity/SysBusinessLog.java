package xyz.easyboot.common.logging.entity;

import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.transaction.Transactional;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 4:20 下午
 */
@Entity
@Table(
        name = "sys_business_log",
        indexes = {
                @Index(name = "index_serial", columnList = "serial"),
        }
)
public class SysBusinessLog extends AuditableEntity {
    
    public String serial;
    @Lob
    public String parameter;
    public String method;
    public String clazz;
    @Lob
    public String response;
    public String code;
    public String message;
    @Lob
    public String exception;
    public Long duration;
    public String title;
    
    @Transactional
    public void save() {
        this.persist();
    }

}
