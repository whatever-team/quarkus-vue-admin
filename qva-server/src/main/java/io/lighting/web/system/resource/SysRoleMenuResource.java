package io.lighting.web.system.resource;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.lighting.common.base.BaseResource;
import io.lighting.common.base.dto.PageInfo;
import io.lighting.common.base.dto.Result;
import io.lighting.common.base.message.impl.DefaultRespCodeMsg;
import io.lighting.common.logging.Logged;
import io.lighting.common.util.BeanUtil;
import io.lighting.web.system.entity.SysRoleMenu;
import io.lighting.web.system.service.SysRoleMenuService;

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
@Path("/sys-role-menu")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysRoleMenuResource extends BaseResource {

    @Inject
    SysRoleMenuService service;

    @Logged
    @GET
    public Result<List<SysRoleMenu>> list(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        // where查询语句
        String query = "";
        // where查询参数
        Object[] args = new Object[0];
        var panacheQuery = SysRoleMenu
                .find(query, Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysRoleMenu> list = panacheQuery.list();
        PageInfo<SysRoleMenu> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysRoleMenu> one(@PathParam("id") Long id) {
        SysRoleMenu data = SysRoleMenu.findById(id);
        return new Result<>(data);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysRoleMenu> update(SysRoleMenu account) {
        SysRoleMenu entity = SysRoleMenu.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        return new Result<>(entity);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysRoleMenu> add(SysRoleMenu account) {
        account.persist();
        return new Result<>(account);
    }

    @Logged
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysRoleMenu.deleteById(id);
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
            SysRoleMenu.deleteById(Long.parseLong(id));
        }
        return Result.defaultResp();
    }

}
