package xyz.easyboot.web.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 前端路由
 * @author wujiawei
 * @see
 * @since 2021/7/5 2:24 下午
 */
@Data
public class RouteDTO {
    
    /**
     * 路由标识
     * 匹配 router.map.js 中注册名 registerName = $router 的路由
     */
    private String router;
    
    /**
     * 路径
     */
    private String path;
    
    /**
     * 路由名称
     */
    private String name;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 外链
     */
    private String link;
    
    /**
     * 子节点
     */
    private List<RouteDTO> children;
    
}
