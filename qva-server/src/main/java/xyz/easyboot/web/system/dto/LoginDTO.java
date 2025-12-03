package xyz.easyboot.web.system.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/3 6:14 下午
 */
@Data
public class LoginDTO {
    
    private User user;
    
    private String token;
    private Date expireAt;
    
 
    // 用户的权限列表
    private List<Permission> permissions;
    
    @Data
    public static class User {
        private String phone;
        private String username;
        private String avatar;
        private String email;
        private String desc;
    
        // 用户的角色名称
        private List<String> roleNames;
        // 用户的所在机构名称
        private List<String> deptNames;
    }
    
    @Data
    public static class Role {
        // id = roleCode
        private String id;
        private String roleName;
        private List<String> operation;
    }
    
    @Data
    public static class Permission {
        // id = role code
        private String id;
        private List<String> operation;
    }
    
}
