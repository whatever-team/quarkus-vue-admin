package xyz.easyboot.web.common.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.handler.OssConfig;
import xyz.easyboot.web.common.dto.MultipartBody;
import xyz.easyboot.web.common.dto.MultipartResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 9:10 上午
 */
@Slf4j
@Path("oss")
public class MultipartResource {
    
    @Inject
    OssConfig ossConfig;
    
    @POST
    @Path("upload/{type}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({ MediaType.APPLICATION_JSON })
    public Result<MultipartResponse> upload(@MultipartForm MultipartBody body, @PathParam("type") String type) {
        // 类型/年月/名称
        String path = type;
        path += "/";
        path += (DateUtil.date().year() + "" + DateUtil.date().month());
        path += "/";
        path += body.file.getName();
        
        String storePath = ossConfig.storePath();
        if (!storePath.endsWith("/")) {
            storePath += "/";
        }
        storePath += path;
        
        log.info("final path = " + storePath);
        File target = FileUtil.copy(body.file, new File(storePath), true);
        log.info(target.getAbsolutePath());
        MultipartResponse response =  new MultipartResponse();
        response.setName(body.file.getName());
        response.setPath(path.replaceAll("/", "-"));
        response.setSize(body.file.getTotalSpace());
        return new Result<>(response);
    }
    
    @POST
    @Path("upload/test")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public void upload(@MultipartForm MultipartFormDataInput input) {
        Map<String, List<InputPart>> map = input.getFormDataMap();
        log.info(JSONUtil.toJsonPrettyStr(map));
    }
    
    @GET
    @Path("preview/{filePath}")
    @Produces(value = MediaType.MULTIPART_FORM_DATA)
    public File preview(@PathParam("filePath") String filePath) {
        return FileUtil.file(ossConfig.storePath() + filePath.replaceAll("-", "/"));
    }
    
}
