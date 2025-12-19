package io.lighting.web.common.resource;

import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import io.lighting.common.base.dto.Result;
import io.lighting.web.common.dto.MonitorDTO;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/28 9:43 上午
 */
@Path("monitor")
public class MonitorResource {
    
    @GET
    public Result<Object> index() {
        MonitorDTO result = new MonitorDTO();
        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        SystemUtil.dumpSystemInfo();
//        result.setCpuInfo(OshiUtil.getCpuInfo());
//        result.setDiskStores(OshiUtil.getDiskStores());
//        result.setGlobalMemory(OshiUtil.getMemory());
//        result.setSensors(OshiUtil.getSensors());
        return new Result<>(0);
    }
    
}
