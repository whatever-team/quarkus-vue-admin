package xyz.easyboot.web.system.resource;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.BusinessLog;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.entity.SysVar;
import xyz.easyboot.web.system.service.SysVarService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2021-07-24.
*/
@Path("/sys-var")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysVarResource extends BaseResource {

    @Inject
    SysVarService service;

    @BusinessLog("查询SysVar")
    @GET
    public Result<List<SysVar>> list(
            @QueryParam("sortField") @DefaultValue("seq") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        // where
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();

        PanacheQuery<SysVar> panacheQuery = SysVar
                        .find(query.toString(), Sort.by(sortField, sortType), args)
                        .page(Page.of(pageIndex - 1, pageSize));
        List<SysVar> list = panacheQuery.list();
        PageInfo<SysVar> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysVar> one(@PathParam("id") Long id) {
        SysVar data = SysVar.findById(id);
        return new Result<>(data);
    }

    @BusinessLog("更新SysVar")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysVar> update(SysVar data) {
        SysVar entity = SysVar.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        return new Result<>(entity);
    }

    @BusinessLog("新增SysVar")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysVar> add(SysVar data) {
        data.persist();
        return new Result<>(data);
    }

    @BusinessLog("删除SysVar")
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysVar.deleteById(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @BusinessLog("批量删除SysVar")
    @Transactional
    @DELETE
    @Path("batch")
    public Result<Object> deleteBatch(@QueryParam("ids") @DefaultValue("") String ids) {
        String[] idArr = ids.split(",");
        if (idArr.length == 0) {
            throw new IllegalArgumentException("Please check you parameter, ids must not be empty");
        }
        SysVar.delete("id in (?1)", Arrays.stream(idArr).map(Long::parseLong).collect(Collectors.toList()));
        return Result.defaultResp();
    }

}
