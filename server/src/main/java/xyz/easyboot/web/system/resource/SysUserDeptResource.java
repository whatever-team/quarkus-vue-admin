package xyz.easyboot.web.system.resource;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.BusinessLog;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.entity.SysUserDept;
import xyz.easyboot.web.system.service.SysUserDeptService;

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
import java.util.List;

/**
* Created by Quarkus-Vue-Admin on 2021-07-03.
*/
@Path("/sys-user-dept")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysUserDeptResource extends BaseResource {

    @Inject
    SysUserDeptService service;

    @BusinessLog
    @GET
    public Result<List<SysUserDept>> list(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        // where查询语句
        String query = "";
        // where查询参数
        Object[] args = new Object[0];
        List<SysUserDept> list = SysUserDept
                .find(query, Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize))
                .list();
        PageInfo<SysUserDept> pageInfo = new PageInfo<>(pageIndex, pageSize, query);
        return new Result<>(list, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysUserDept> one(@PathParam("id") Long id) {
        SysUserDept data = SysUserDept.findById(id);
        return new Result<>(data);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysUserDept> update(SysUserDept account) {
        SysUserDept entity = SysUserDept.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        return new Result<>(entity);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysUserDept> add(SysUserDept account) {
        account.persist();
        return new Result<>(account);
    }

    @BusinessLog
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysUserDept.deleteById(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
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
        for (String id : idArr) {
            SysUserDept.deleteById(Long.parseLong(id));
        }
        return Result.defaultResp();
    }

}
