package xyz.easyboot.common.util;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/1 8:16 下午
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {
    
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreNullValue(true);
        BeanCopier.create(source, target, copyOptions).copy();
    }
    
}
