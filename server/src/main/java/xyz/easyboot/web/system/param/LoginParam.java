package xyz.easyboot.web.system.param;

import lombok.Data;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/3 6:14 下午
 */
@Data
public class LoginParam {
    private String name;
    private String password;
    private String code;
    // 验证码绑定的t字段
    private String t;
}
