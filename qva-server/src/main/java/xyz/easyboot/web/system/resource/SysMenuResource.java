package xyz.easyboot.web.system.resource;

import cn.hutool.json.JSONUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.Logged;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.dto.RouteDTO;
import xyz.easyboot.web.system.dto.SysMenuTreeDTO;
import xyz.easyboot.web.system.entity.SysMenu;
import xyz.easyboot.web.system.service.SysMenuService;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by Quarkus-Vue-Admin on 2021-07-02.
*/
@Slf4j
@Path("/sys-menu")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysMenuResource extends BaseResource {

    @Inject
    SysMenuService service;

    @Logged
    @GET
    public Result<Object> list(
            @QueryParam("sortField") @DefaultValue("id") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("menuName") String menuName
    ) {
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();
        if (StringUtil.isNotEmpty(menuName)) {
            query.append(" and menuName like concat('%', :menuName, '%')");
            args.put("menuName", menuName);
        }
    
        PanacheQuery<SysMenu> panacheQuery = SysMenu
                .find(query.toString(), Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysMenu> list = panacheQuery.list();
        PageInfo<SysMenu> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @GET
    @Path("tree")
    public Result<Object> treeList(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("menuName") String menuName
    ) {
        // where查询
        String query = "";
        List<Object> objects = new ArrayList<>();
        if (StringUtil.isNotEmpty(menuName)) {
            query = "menuName like concat('%', ?1, '%')";
            objects.add(menuName);
        }
        
        PanacheQuery<SysMenu> panacheQuery = SysMenu
                .find(query, Sort.by(sortField, sortType), objects.toArray())
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysMenu> list = panacheQuery.list();
        PageInfo<SysMenu> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        List<SysMenuTreeDTO> treeList = service.convertMenuToTree(list, SysMenu.TOP_PARENT_ID);
        log.info(JSONUtil.toJsonStr(new Result<>(treeList, pageInfo)));
        return new Result<>(treeList, pageInfo);
    }
    
    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysMenu> one(@PathParam("id") Long id) {
        SysMenu data = SysMenu.findById(id);
        return new Result<>(data);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysMenu> update(SysMenu account) {
        SysMenu entity = SysMenu.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        return new Result<>(entity);
    }

    @Logged
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysMenu> add(SysMenu account) {
        account.persist();
        return new Result<>(account);
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
        for (String id : idArr) {
            SysMenu.deleteById(Long.parseLong(id));
        }
        return Result.defaultResp();
    }
    
    @Logged
    @GET
    @Path("routes")
    public Result<Object> routes() {
        String userId = getUserId();
        List<RouteDTO> list = service.generateRoute(userId);
        return new Result<>(list);
    }
    
}
