package io.lighting.web.system.entity;

import io.lighting.common.orm.entity.AuditableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 数据字典
 * @author wujiawei
 * @see
 * @since 2021/7/13 10:26 上午
 */
@Entity
@Table(
        name = "sys_dict",
        indexes = {
                @Index(name = "index_dict_code", columnList = "dictCode"),
                @Index(name = "index_enabled", columnList = "enabled"),
        }
)
public class SysDict extends AuditableEntity {

    public String dictCode;
    public String dictName;
    public Boolean enabled;
    
}
