package io.lighting.web.system.resource;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.*;
import io.lighting.common.base.BaseResource;
import io.lighting.common.base.dto.PageInfo;
import io.lighting.common.base.dto.Result;
import io.lighting.common.logging.Logged;
import io.lighting.common.util.BeanUtil;
import io.lighting.common.util.StringUtil;
import io.lighting.web.system.dto.SysRoleDTO;
import io.lighting.web.system.entity.SysRole;
import io.lighting.web.system.service.SysRoleService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@Path("/sys-role")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysRoleResource extends BaseResource {

    @Inject
    SysRoleService service;

    @Logged
    @GET
    public Result<List<SysRoleDTO>> list(
            @QueryParam("sortField") @DefaultValue("id") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("roleName") String roleName
    ) {
        var sort = Sort.by(sortField, sortType);
        var panacheQuery = StringUtil.isNotEmpty(roleName)
                ? SysRole.find("roleName like concat('%', ?1, '%')", sort, roleName)
                : SysRole.findAll(sort);
        panacheQuery.page(Page.of(pageIndex - 1, pageSize));

        List<SysRoleDTO> relationList = service.completeMenus(panacheQuery.list());
        PageInfo<SysRole> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(relationList, pageInfo);
    }

    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysRole> one(@PathParam("id") Long id) {
        SysRole data = SysRole.findById(id);
        return new Result<>(data);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysRole> update(SysRoleDTO data) {
        SysRole entity = SysRole.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        service.updateMenus(data);
        return new Result<>(entity);
    }

    @Logged
    @Transactional(Transactional.TxType.REQUIRED)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysRole> add(SysRoleDTO data) {
        SysRole entity = new SysRole();
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        entity.persist();
        data.id = entity.id;
//        service.updateMenus(data);
        return new Result<>(data);
    }

    @Logged
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        service.delete(id);
        return Result.defaultResp();
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
        service.delete(idArr);
        return Result.defaultResp();
    }

}
