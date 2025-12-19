package io.lighting.web.system.resource;

import io.lighting.common.base.BaseResource;
import io.lighting.common.base.dto.Result;
import io.lighting.common.base.message.impl.DefaultRespCodeMsg;
import io.lighting.common.logging.Logged;
import io.lighting.web.system.dto.LoginDTO;
import io.lighting.web.system.param.ChangePwdParam;
import io.lighting.web.system.param.LoginParam;
import io.lighting.web.system.service.SysUserService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("auth")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysAuthResource extends BaseResource {

    @Inject
    SysUserService service;

    @Logged("后台用户登录")
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Result<LoginDTO> login(LoginParam param) {
        LoginDTO loginDTO = service.login(param);
        Result<LoginDTO> result = new Result<>(loginDTO);
        result.setMessage("登录成功，欢迎回来");
        return result;
    }

    @Logged("获取后台登录用户信息")
    @GET
    @Path("info")
    @Consumes(MediaType.APPLICATION_JSON)
    public Result<LoginDTO.User> userInfo() {
        return new Result<>(service.userInfo(getUserId()));
    }

    @Logged("修改密码")
    @PUT
    @Path("password")
    public Result<Object> changePassword(@Valid ChangePwdParam param) {
        param.setUserId(getUserId());
        boolean flag = service.changePassword(param);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

}
