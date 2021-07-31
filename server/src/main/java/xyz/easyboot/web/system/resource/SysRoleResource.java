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
import xyz.easyboot.web.system.dto.SysRoleDTO;
import xyz.easyboot.web.system.entity.SysRole;
import xyz.easyboot.web.system.service.SysRoleService;

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
@Path("/sys-role")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysRoleResource extends BaseResource {

    @Inject
    SysRoleService service;

    @BusinessLog
    @GET
    public Result<List<SysRoleDTO>> list(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("roleName") String roleName
    ) {
        // where查询语句
        String query = "";
        List<Object> objects = new ArrayList<>();
        if (StringUtil.isNotEmpty(roleName)) {
            query = "roleName like concat('%', ?1, '%')";
            objects.add(roleName);
        }
        
        PanacheQuery<SysRole> panacheQuery = SysRole
                .find(query, Sort.by(sortField, sortType), objects.toArray())
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysRole> list = panacheQuery.list();
        List<SysRoleDTO> relationList = service.completeMenus(list);
        PageInfo<SysRole> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(relationList, pageInfo);
    }

    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysRole> one(@PathParam("id") Long id) {
        SysRole data = SysRole.findById(id);
        return new Result<>(data);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysRole> update(SysRoleDTO data) {
        SysRole entity = SysRole.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        service.updateMenus(data);
        return new Result<>(entity);
    }

    @BusinessLog
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysRole> add(SysRoleDTO data) {
        data.persist();
        service.updateMenus(data);
        return new Result<>(data);
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
