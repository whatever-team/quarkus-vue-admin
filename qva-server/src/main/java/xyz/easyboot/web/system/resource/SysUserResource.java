package xyz.easyboot.web.system.resource;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.Logged;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.PasswordUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.dto.SysUserDTO;
import xyz.easyboot.web.system.entity.SysUser;
import xyz.easyboot.web.system.entity.SysUserDept;
import xyz.easyboot.web.system.service.SysUserService;

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
import java.util.stream.Collectors;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:39 下午
 */
@Path("sys-user")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysUserResource extends BaseResource {
    
    @Inject
    SysUserService service;
    
    @Logged
    @GET
    public Result<List<?>> list(
            @QueryParam("sortField") String sortField,
            @QueryParam("sortType") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("phone") String phone,
            @QueryParam("username") String username,
            @QueryParam("enabled") Boolean enabled,
            @QueryParam("deptId") String deptId
    ) {
        // where查询语句
        PanacheQuery<SysUser> panacheQuery = SysUser.findAll(Sort.by(sortField, sortType));
        if (StringUtil.isNotEmpty(phone)) {
            panacheQuery.filter("phone like concat('%', :phone, '%')", Parameters.with("phone", phone));
        }
        if (StringUtil.isNotEmpty(username)) {
            panacheQuery.filter("username like concat('%', :username, '%')", Parameters.with("username", username));
        }
        if (enabled != null) {
            panacheQuery.filter("enabled = :enabled", Parameters.with("enabled", enabled));
        }
        if (StringUtil.isNotEmpty(deptId)) {
            List<SysUserDept> userDeptList = SysUserDept.list("deptId", Long.parseLong(deptId));
            panacheQuery.filter("id in (:userIds)", Parameters.with("userIds", userDeptList.stream().map(x -> x.userId).collect(Collectors.toList())));
        }

        panacheQuery.page(Page.of(pageIndex - 1, pageSize));
        List<SysUserDTO> list = panacheQuery.list().stream().map(SysUserDTO::new).collect(Collectors.toList());
        PageInfo<SysUser> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }
    
    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysUser> one(@PathParam("id") Long id) {
        SysUser data = SysUser.findById(id);
        return new Result<>(data);
    }
    
    @Transactional
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysUser> update(SysUserDTO account) {
        if (account.id == null) {
            account.id = getUserId();
        }
        service.validPhone(account.phone, account.id);
        
        SysUser entity = SysUser.findById(account.id);
        BeanUtil.copyPropertiesIgnoreNull(account, entity);
        service.updateDept(account);
        service.updateRoles(account);
        return new Result<>(entity);
    }
    
    @Transactional
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysUserDTO> add(SysUserDTO account) {
        service.validPhone(account.phone, null);
        
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(account, sysUser);
        sysUser.password = PasswordUtil.encrypt(PasswordUtil.DEFAULT_PASSWORD);
        if (sysUser.enabled == null) {
            sysUser.enabled = true;
        }
        sysUser.persist();
        account.id = sysUser.id;
        
        service.updateDept(account);
        service.updateRoles(account);
        return new Result<>(account);
    }
    
    @Transactional
    @Logged
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        service.delete(id);
        return Result.defaultResp();
    }
    
    @Transactional
    @Logged
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
