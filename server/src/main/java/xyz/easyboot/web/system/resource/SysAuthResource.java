package xyz.easyboot.web.system.resource;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.BusinessLog;
import xyz.easyboot.web.system.dto.LoginDTO;
import xyz.easyboot.web.system.param.ChangePwdParam;
import xyz.easyboot.web.system.param.LoginParam;
import xyz.easyboot.web.system.service.SysUserService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("auth")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysAuthResource extends BaseResource {

    @Inject
    SysUserService service;

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
