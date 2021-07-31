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
import xyz.easyboot.web.system.entity.SysDictItem;
import xyz.easyboot.web.system.service.SysDictItemService;

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
* Created by Quarkus-Vue-Admin on 2021-07-19.
*/
@Path("/sys-dict-item")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysDictItemResource extends BaseResource {

    @Inject
    SysDictItemService service;

    @BusinessLog("查询SysDictItem")
    @GET
    public Result<List<SysDictItem>> list(
            @QueryParam("sortField") @DefaultValue("seq") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("dictCode") String dictCode
    ) {
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();
        if (StringUtil.isNotEmpty(dictCode)) {
            query.append(" and dictCode = :dictCode");
            args.put("dictCode", dictCode);
        }

        PanacheQuery<SysDictItem> panacheQuery = SysDictItem
                        .find(query.toString(), Sort.by(sortField, sortType), args)
                        .page(Page.of(pageIndex - 1, pageSize));
        List<SysDictItem> list = panacheQuery.list();
        PageInfo<SysDictItem> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysDictItem> one(@PathParam("id") Long id) {
        SysDictItem data = SysDictItem.findById(id);
        return new Result<>(data);
    }

    @BusinessLog("更新SysDictItem")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysDictItem> update(SysDictItem data) {
        SysDictItem entity = SysDictItem.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        return new Result<>(entity);
    }

    @BusinessLog("新增SysDictItem")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysDictItem> add(SysDictItem data) {
        data.persist();
        return new Result<>(data);
    }

    @BusinessLog("删除SysDictItem")
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysDictItem.deleteById(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @BusinessLog("批量删除SysDictItem")
    @Transactional
    @DELETE
    @Path("batch")
    public Result<Object> deleteBatch(@QueryParam("ids") @DefaultValue("") String ids) {
        String[] idArr = ids.split(",");
        if (idArr.length == 0) {
            throw new IllegalArgumentException("Please check you parameter, ids must not be empty");
        }
        SysDictItem.delete("id in (?1)", Arrays.stream(idArr).map(Long::parseLong).collect(Collectors.toList()));
        return Result.defaultResp();
    }

}
