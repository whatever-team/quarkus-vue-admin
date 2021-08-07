package xyz.easyboot;

import org.junit.jupiter.api.Test;
import xyz.easyboot.common.generator.CodeGenerator;
import xyz.easyboot.web.system.entity.SysVar;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 11:00 上午
 */
public class CodeGeneratorTest {
    
    @Test
    public void generate() {
        CodeGenerator.BUSINESS_PACKAGE = "web.business";
        CodeGenerator.genCode(SysVar.class);
    }
    
}
