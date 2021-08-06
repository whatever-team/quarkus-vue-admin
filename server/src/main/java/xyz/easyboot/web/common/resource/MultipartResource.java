package xyz.easyboot.web.common.resource;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.util.FileUtil;
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
import java.io.IOException;
import java.io.InputStream;

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
    public Result<MultipartResponse> upload(@MultipartForm MultipartBody body, @PathParam("type") String type)
            throws IOException {
        // fileName
        String fileName = FileUtil.getFileNameFromInputPart(body.file);
        
        // 类型/年月/名称
        String path = type;
        path += "/";
        path += (DateUtil.date().year() + "" + DateUtil.date().month());
        path += "/";
        path += fileName;
        
        // 存储位置
        String storePath = ossConfig.storePath();
        if (!storePath.endsWith("/")) {
            storePath += "/";
        }
        storePath += path;
        
        // 将文件流写入文件
        InputStream is = FileUtil.getInputStreamFromInputPart(body.file);
        File target = FileUtil.writeFromStream(is, storePath);
        MultipartResponse response =  new MultipartResponse();
        response.setName(target.getName());
        response.setPath(path.replaceAll("/", "-"));
        response.setSize(FileUtil.size(target));
        return new Result<>(response);
    }
    
    @GET
    @Path("preview/{filePath:.+}")
    @Produces(value = MediaType.MULTIPART_FORM_DATA)
    public File preview(@PathParam("filePath") String filePath) {
        return FileUtil.file(ossConfig.storePath() + filePath);
    }
    
}
