package xyz.easyboot.web.system.resource;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.Logged;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.web.system.entity.SysUserRole;
import xyz.easyboot.web.system.service.SysUserRoleService;

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
import java.util.List;

/**
* Created by Quarkus-Vue-Admin on 2021-07-03.
*/
@Path("/sys-user-role")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysUserRoleResource extends BaseResource {

    @Inject
    SysUserRoleService service;

    @Logged
    @GET
    public Result<List<SysUserRole>> list(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        // where查询语句
        String query = "";
        // where查询参数
        Object[] args = new Object[0];
        List<SysUserRole> list = SysUserRole
                .find(query, Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize))
                .list();
        PageInfo<SysUserRole> pageInfo = new PageInfo<>(pageIndex, pageSize, query);
        return new Result<>(list, pageInfo);
    }

    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysUserRole> one(@PathParam("id") Long id) {
        SysUserRole data = SysUserRole.findById(id);
        return new Result<>(data);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysUserRole> update(SysUserRole account) {
        SysUserRole entity = SysUserRole.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        return new Result<>(entity);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysUserRole> add(SysUserRole account) {
        account.persist();
        return new Result<>(account);
    }

    @Logged
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysUserRole.deleteById(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @Logged
    @Transactional
    @DELETE
    @Path("batch")
    public Result<Object> deleteBatch(@QueryParam("ids") @DefaultValue("") String ids) {
        String[] idArr = ids.split(",");
        if (idArr.length == 0) {
            throw new IllegalArgumentException("Please check you parameter, ids must not be empty");
        }
        for (String id : idArr) {
            SysUserRole.deleteById(Long.parseLong(id));
        }
        return Result.defaultResp();
    }

}
