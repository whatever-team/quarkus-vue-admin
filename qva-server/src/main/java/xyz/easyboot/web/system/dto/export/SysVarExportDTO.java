package xyz.easyboot.web.system.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.entity.SysVar;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 专用于数据导出的DTO
 * @author wujiawei
 * @see
 * @since 2021/8/5 4:41 下午
 */
@Data
public class SysVarExportDTO {
    
    @ExcelProperty("编码")
    private String varCode;
    
    @ExcelProperty("名称")
    private String varName;
    
    @ExcelProperty("值")
    private String varValue;
    
    public static List<SysVarExportDTO> fromSysVar(List<SysVar> list) {
        return list.stream().map(x -> {
            SysVarExportDTO exportDTO = new SysVarExportDTO();
            BeanUtil.copyProperties(x, exportDTO);
            return exportDTO;
        }).collect(Collectors.toList());
    }
    
}
