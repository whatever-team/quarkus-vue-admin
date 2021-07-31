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
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.entity.SysDict;
import xyz.easyboot.web.system.entity.SysDictItem;
import xyz.easyboot.web.system.service.SysDictItemService;
import xyz.easyboot.web.system.service.SysDictService;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by Quarkus-Vue-Admin on 2021-07-13.
*/
@Path("/sys-dict")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysDictResource extends BaseResource {

    @Inject
    SysDictService service;
    @Inject
    SysDictItemService itemService;

    @BusinessLog("查询SysDict")
    @GET
    public Result<List<SysDict>> list(
            @QueryParam("sortField") @DefaultValue("seq") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("dictCode") String dictCode,
            @QueryParam("dictName") String dictName
    ) {
        // where
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();
        if (StringUtil.isNotEmpty(dictName)) {
            query.append(" and dictName like concat('%', :dictName, '%')");
            args.put("dictName", dictName);
        }
        if (StringUtil.isNotEmpty(dictCode)) {
            query.append(" and dictCode = :dictCode");
            args.put("dictCode", dictCode);
        }

        PanacheQuery<SysDict> panacheQuery = SysDict
                        .find(query.toString(), Sort.by(sortField, sortType), args)
                        .page(Page.of(pageIndex - 1, pageSize));
        List<SysDict> list = panacheQuery.list();
        PageInfo<SysDict> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysDict> one(@PathParam("id") Long id) {
        SysDict data = SysDict.findById(id);
        return new Result<>(data);
    }
    
    @BusinessLog
    @GET
    @Path("one/{dictCode}/{itemCode}")
    public Result<SysDictItem> query(@PathParam("dictCode") String dictCode, @PathParam("itemCode") String itemCode) {
        SysDictItem dict = SysDictItem
                .find("dictCode = ?1 and itemCode = ?2 and enabled = ?3", dictCode, itemCode, Boolean.TRUE).firstResult();
        return new Result<>(dict);
    }
    
    @BusinessLog
    @GET
    @Path("list/{dictCode}")
    public Result<List<SysDictItem>> query(@PathParam("dictCode") String dictCode) {
        List<SysDictItem> list = SysDictItem.list("dictCode = ?1 and enabled = ?2", dictCode, Boolean.TRUE);
        return new Result<>(list);
    }
    
    @BusinessLog("更新SysDict")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysDict> update(SysDict data) {
        SysDict entity = SysDict.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        return new Result<>(entity);
    }

    @BusinessLog("新增SysDict")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysDict> add(SysDict data) {
        data.persist();
        return new Result<>(data);
    }

    @BusinessLog("删除SysDict")
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        boolean flag = service.delete(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @BusinessLog("批量删除SysDict")
    @Transactional
    @DELETE
    @Path("batch")
    public Result<Object> deleteBatch(@QueryParam("ids") @DefaultValue("") String ids) {
        String[] idArr = ids.split(",");
        if (idArr.length == 0) {
            throw new IllegalArgumentException("Please check you parameter, ids must not be empty");
        }
        service.delete(idArr);
        return Result.defaultResp();
    }

}
