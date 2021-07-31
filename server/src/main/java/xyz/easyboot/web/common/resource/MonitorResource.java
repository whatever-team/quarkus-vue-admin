package xyz.easyboot.web.common.resource;

import cn.hutool.system.oshi.OshiUtil;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.web.common.dto.MonitorDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/28 9:43 上午
 */
@Path("monitor")
public class MonitorResource {
    
    @GET
    public Result<MonitorDTO> index() {
        MonitorDTO result = new MonitorDTO();
        result.setCpuInfo(OshiUtil.getCpuInfo());
        result.setDiskStores(OshiUtil.getDiskStores());
        result.setGlobalMemory(OshiUtil.getMemory());
        result.setSensors(OshiUtil.getSensors());
        return new Result<>(result);
    }
    
}
