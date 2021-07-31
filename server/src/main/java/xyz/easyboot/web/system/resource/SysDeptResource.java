package xyz.easyboot.web.system.resource;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.BusinessLog;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.dto.SysDeptDTO;
import xyz.easyboot.web.system.entity.SysDept;
import xyz.easyboot.web.system.service.SysDeptService;

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
import java.util.ArrayList;
import java.util.List;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@Path("/sys-dept")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysDeptResource extends BaseResource {

    @Inject
    SysDeptService service;

    @BusinessLog
    @GET
    public Result<List<SysDeptDTO>> list(
            @QueryParam("sortField") @DefaultValue("seq") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("deptName") String deptName
    ) {
        String query = "";
        List<Object> args = new ArrayList<>();
        if (StringUtil.isNotEmpty(deptName)) {
            query = "deptName like concat('%', ?1, '%')";
            args.add(deptName);
        }
        
        // where查询参数
        PanacheQuery<SysDept> panacheQuery = SysDept
                .find(query, Sort.by(sortField, sortType), args.toArray())
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysDept> list = panacheQuery.list();
        List<SysDeptDTO> results = service.convertMenuToTree(list, 0);
        PageInfo<SysDept> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(results, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysDept> one(@PathParam("id") Long id) {
        SysDept data = SysDept.findById(id);
        return new Result<>(data);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysDept> update(SysDept account) {
        SysDept entity = SysDept.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        return new Result<>(entity);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysDept> add(SysDept account) {
        account.persist();
        return new Result<>(account);
    }

    @BusinessLog
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        service.delete(id);
        return Result.defaultResp();
    }

    @BusinessLog
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
