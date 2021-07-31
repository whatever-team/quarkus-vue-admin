package xyz.easyboot.web.common.dto;

import cn.hutool.system.oshi.CpuInfo;
import lombok.Data;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/28 9:44 上午
 */
@Data
public class MonitorDTO {
    
    private CpuInfo cpuInfo;
    
    private OperatingSystem operatingSystem;
    
    private GlobalMemory globalMemory;
    
    private Sensors sensors;
    
    private List<HWDiskStore> diskStores;
    
}
