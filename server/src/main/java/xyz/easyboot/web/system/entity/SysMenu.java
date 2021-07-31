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
 * @since 2021/7/2 11:38 上午
 */
@Entity
@Table(
        name = "sys_menu",
        indexes = {
                @Index(name = "index_parent_id", columnList = "parentId"),
                @Index(name = "index_menu_name", columnList = "menuName"),
                @Index(name = "index_enabled", columnList = "enabled")
        }
)
public class SysMenu extends AuditableEntity {
    
    public static final long TOP_PARENT_ID = 1000000000000000L;
    
    public static final int TARGET_SELF = 0;
    public static final int TARGET_BLANK = 1;
    
    public static final int TYPE_DIRECTORY = 0;
    public static final int TYPE_MENU = 1;
    public static final int TYPE_BUTTON = 2;
    
    public String menuName;
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long parentId;
    
    /**
     * 路由名称
     */
    public String routerName;
    
    /**
     * 路由、链接地址
     */
    public String path;
    
    /**
     * 打开方式：0-内页，1-外链
     */
    public Integer target;
    
    /**
     * 类型：0-菜单，1-按钮
     */
    public Integer type;
    
    public Boolean enabled;
    
    /**
     * 权限
     */
    public String permission;
    
    public String icon;
    
}
