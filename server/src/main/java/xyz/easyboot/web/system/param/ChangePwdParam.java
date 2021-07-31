package xyz.easyboot.web.system.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码 参数
 * @author wujiawei
 * @see
 * @since 2021/7/13 9:34 上午
 */
@Data
public class ChangePwdParam {
    
    private Long id;
    
    private String phone;
    
    @NotBlank(message="新密码不能为空")
    private String newPassword;
    
    @NotBlank(message="原密码不能为空")
    private String oldPassword;
    
}
