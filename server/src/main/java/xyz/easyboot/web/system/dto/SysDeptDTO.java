package xyz.easyboot.web.system.dto;

import xyz.easyboot.web.system.entity.SysDept;

import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/9 8:37 下午
 */
public class SysDeptDTO extends SysDept {
    
    public List<SysDeptDTO> children;
    
}
