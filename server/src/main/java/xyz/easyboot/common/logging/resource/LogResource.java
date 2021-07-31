package xyz.easyboot.common.logging.resource;

import cn.hutool.core.date.DateUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.entity.SysBusinessLog;
import xyz.easyboot.common.util.StringUtil;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:39 下午
 */
@Path("sys-business-log")
@Produces(value = MediaType.APPLICATION_JSON)
public class LogResource extends BaseResource {
    
    @GET
    public Result<List<SysBusinessLog>> list(
            @QueryParam("sortField") @DefaultValue("createTime") String sortField,
            @QueryParam("sortType") @DefaultValue("Descending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("clazz") String clazz,
            @QueryParam("method") String method,
            @QueryParam("message") String message,
            @QueryParam("serial") String serial,
            @QueryParam("createTimeStart") String createTimeStart,
            @QueryParam("createTimeEnd") String createTimeEnd
    ) {
        // where查询语句
        StringBuilder query = new StringBuilder("");
        Map<String, Object> args = new HashMap<>();
        query.append("1 = 1");
        if (StringUtil.isNotEmpty(clazz)) {
            query.append(" and clazz like concat('%', :clazz, '%')");
            args.put("clazz", clazz);
        }
        if (StringUtil.isNotEmpty(method)) {
            query.append(" and method like concat('%', :method, '%')");
            args.put("method", method);
        }
        if (StringUtil.isNotEmpty(message)) {
            query.append(" and message like concat('%', :message, '%')");
            args.put("message", message);
        }
        if (StringUtil.isNotEmpty(serial)) {
            query.append(" and serial like concat('%', :serial, '%')");
            args.put("serial", serial);
        }
        if (StringUtil.isNotEmpty(createTimeStart)) {
            query.append(" and createTime >= :createTimeStart");
            args.put("createTimeStart", DateUtil.parse(createTimeStart));
        }
        if (StringUtil.isNotEmpty(createTimeEnd)) {
            query.append(" and createTime <= :createTimeEnd");
            args.put("createTimeEnd", DateUtil.parse(createTimeEnd));
        }
    
        PanacheQuery<SysBusinessLog> panacheQuery = SysBusinessLog
                .find(query.toString(), Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysBusinessLog> list = panacheQuery.list();
        PageInfo<SysBusinessLog> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }
    
}
