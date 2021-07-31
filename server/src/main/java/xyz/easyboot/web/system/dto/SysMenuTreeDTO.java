package xyz.easyboot.web.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/6 8:27 下午
 */
@Data
public class SysMenuTreeDTO {
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String menuName;
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    
    /**
     * 路由名称
     */
    private String routerName;
    
    /**
     * 路由、链接地址
     */
    private String path;
    
    /**
     * 打开方式：0-内页，1-外链
     */
    private Integer target;
    
    /**
     * 类型：0-菜单，1-按钮
     */
    private Integer type;
    
    private Boolean enabled;
    
    /**
     * 权限
     */
    private String permission;
    
    private String icon;
    
    /**
     * 序号
     */
    private Long seq;
    
    private List<SysMenuTreeDTO> children;
    
}
