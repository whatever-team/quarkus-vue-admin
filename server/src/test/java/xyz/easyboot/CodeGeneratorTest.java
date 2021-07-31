package xyz.easyboot;

import org.junit.jupiter.api.Test;
import xyz.easyboot.common.generator.CodeGenerator;
import xyz.easyboot.web.system.entity.SysDict;
import xyz.easyboot.web.system.entity.SysDictItem;
import xyz.easyboot.web.system.entity.SysRoleMenu;
import xyz.easyboot.web.system.entity.SysUserDept;
import xyz.easyboot.web.system.entity.SysUserRole;
import xyz.easyboot.web.system.entity.SysVar;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 11:00 上午
 */
public class CodeGeneratorTest {
    
    @Test
    public void generate() {
        CodeGenerator.BUSINESS_PACKAGE = "web.system";
        CodeGenerator.genCode(SysVar.class);
    }
    
}
