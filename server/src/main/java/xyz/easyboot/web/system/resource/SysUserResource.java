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
import xyz.easyboot.common.util.PasswordUtil;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.web.system.dto.LoginDTO;
import xyz.easyboot.web.system.dto.SysUserDTO;
import xyz.easyboot.web.system.entity.SysUser;
import xyz.easyboot.web.system.entity.SysUserDept;
import xyz.easyboot.web.system.param.ChangePwdParam;
import xyz.easyboot.web.system.param.LoginParam;
import xyz.easyboot.web.system.service.SysUserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    
    @BusinessLog
    @GET
    public Result<List<SysUserDTO>> list(
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
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();
        if (StringUtil.isNotEmpty(phone)) {
            query.append(" and phone like concat('%', :phone, '%')");
            args.put("phone", phone);
        }
        if (StringUtil.isNotEmpty(username)) {
            query.append(" and username like concat('%', :username, '%')");
            args.put("username", username);
        }
        if (enabled != null) {
            query.append(" and enabled = :enabled");
            args.put("enabled", enabled);
        }
        if (StringUtil.isNotEmpty(deptId)) {
            List<SysUserDept> userDeptList = SysUserDept.list("deptId", Long.parseLong(deptId));
            query.append(" and id in (:userIds)");
            args.put("userIds", userDeptList.stream().map(x -> x.userId).collect(Collectors.toList()));
        }
    
        PanacheQuery<SysUser> panacheQuery = SysUser
                .find(query.toString(), Sort.by(sortField, sortType), args)
                .page(Page.of(pageIndex - 1, pageSize));
        List<SysUser> list = panacheQuery.list();
        List<SysUserDTO> results = service.completeRolesAndDept(list);
        PageInfo<SysUser> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(results, pageInfo);
    }
    
    @BusinessLog
    @GET
    @Path("one/{id}")
    public Result<SysUser> one(@PathParam("id") Long id) {
        SysUser data = SysUser.findById(id);
        return new Result<>(data);
    }
    
    @Transactional
    @BusinessLog
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
    @BusinessLog
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
    @BusinessLog
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") String id) {
        service.delete(id);
        return Result.defaultResp();
    }
    
    @Transactional
    @BusinessLog
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
    
    @BusinessLog("后台用户登录")
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Result<LoginDTO> login(LoginParam param) {
        LoginDTO loginDTO = service.login(param);
        Result<LoginDTO> result = new Result<>(loginDTO);
        result.setMessage("登录成功，欢迎回来");
        return result;
    }
    
    @BusinessLog("修改密码")
    @PUT
    @Path("password")
    public Result<Object> changePassword(@Valid ChangePwdParam param) {
        param.setId(getUserId());
        boolean flag = service.changePassword(param);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }
}
