package xyz.easyboot.web.system.resource;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.Logged;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.entity.SysDict;
import xyz.easyboot.web.system.entity.SysDictItem;
import xyz.easyboot.web.system.service.SysDictItemService;
import xyz.easyboot.web.system.service.SysDictService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
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

    @Logged("查询SysDict")
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

    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysDict> one(@PathParam("id") Long id) {
        SysDict data = SysDict.findById(id);
        return new Result<>(data);
    }
    
    @Logged
    @GET
    @Path("one/{dictCode}/{itemCode}")
    public Result<SysDictItem> query(@PathParam("dictCode") String dictCode, @PathParam("itemCode") String itemCode) {
        SysDictItem dict = SysDictItem
                .find("dictCode = ?1 and itemCode = ?2 and enabled = ?3", dictCode, itemCode, Boolean.TRUE).firstResult();
        return new Result<>(dict);
    }
    
    @Logged
    @GET
    @Path("list/{dictCode}")
    public Result<List<SysDictItem>> query(@PathParam("dictCode") String dictCode) {
        List<SysDictItem> list = SysDictItem.list("dictCode = ?1 and enabled = ?2", dictCode, Boolean.TRUE);
        return new Result<>(list);
    }
    
    @Logged("更新SysDict")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysDict> update(SysDict data) {
        SysDict entity = SysDict.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        return new Result<>(entity);
    }

    @Logged("新增SysDict")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysDict> add(SysDict data) {
        data.persist();
        return new Result<>(data);
    }

    @Logged("删除SysDict")
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        boolean flag = service.delete(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @Logged("批量删除SysDict")
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
