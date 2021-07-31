package xyz.easyboot.web.system.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 11:52 上午
 */
@Entity
@Table(
        name = "sys_dept",
        indexes = {
                @Index(name = "index_dept_name", columnList = "deptName"),
                @Index(name = "index_parentId", columnList = "parentId")
        }
)
public class SysDept extends AuditableEntity {
    
    public String deptName;
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long parentId;
    
    /**
     * 负责人
     */
    public String leader;
    
    /**
     * 联系方式
     */
    public String contact;

    public Boolean enabled;
}
