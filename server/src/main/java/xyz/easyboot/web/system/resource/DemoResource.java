package xyz.easyboot.web.system.resource;

import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.BusinessLog;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 3:15 下午
 */
@Path("demo")
public class DemoResource {
    
    @BusinessLog
    @GET
    @Path("hello")
    public Result<String> hello(@QueryParam("a") String a, @QueryParam("b") Integer b, @QueryParam("c") String ignore) {
        return new Result<>("hello");
    }
    
}
