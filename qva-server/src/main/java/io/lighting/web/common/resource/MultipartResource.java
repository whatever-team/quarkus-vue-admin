package io.lighting.web.common.resource;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import io.lighting.common.base.dto.Result;
import io.lighting.common.util.FileUtil;
import io.lighting.common.util.StringUtil;
import io.lighting.handler.OssConfig;
import io.lighting.web.common.dto.MultipartBody;
import io.lighting.web.common.dto.MultipartResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 9:10 上午
 */
@Slf4j
@Path("oss")
public class MultipartResource {
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    
    @Inject
    OssConfig ossConfig;
    
    @POST
    @Path("upload/{type}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({ MediaType.APPLICATION_JSON })
    public Result<MultipartResponse> upload(@MultipartForm MultipartBody body, @PathParam("type") String type) {
        FileUpload upload = body.file;
        if (upload == null) {
            throw new BadRequestException("文件不能为空");
        }
        if (FileUtil.size(upload) > MAX_FILE_SIZE) {
            throw new BadRequestException("文件超过大小限制");
        }

        String fileName = FileUtil.getFileName(upload);
        String safeType = normalizeType(type);
        String yearMonth = DateUtil.date().toString("yyyyMM");
        java.nio.file.Path basePath = Paths.get(ossConfig.storePath()).toAbsolutePath().normalize();
        java.nio.file.Path relative = Paths.get(safeType, yearMonth, fileName);
        java.nio.file.Path targetPath = basePath.resolve(relative).normalize();
        if (!targetPath.startsWith(basePath)) {
            throw new ForbiddenException("非法路径");
        }

        File target = FileUtil.moveUpload(upload, targetPath);
        MultipartResponse response =  new MultipartResponse();
        response.setName(target.getName());
        response.setPath(relative.toString().replace("\\", "/"));
        response.setSize(FileUtil.size(upload));
        return new Result<>(response);
    }
    
    @GET
    @Path("preview/{filePath:.+}")
    @Produces(value = MediaType.MULTIPART_FORM_DATA)
    public Response preview(@PathParam("filePath") String filePath) {
        java.nio.file.Path target = safeResolve(filePath);
        if (!Files.exists(target)) {
            throw new NotFoundException("文件不存在");
        }
        return Response.ok(target.toFile()).build();
    }

    private java.nio.file.Path safeResolve(String relativePath) {
        java.nio.file.Path basePath = Paths.get(ossConfig.storePath()).toAbsolutePath().normalize();
        java.nio.file.Path target = basePath.resolve(relativePath).normalize();
        if (!target.startsWith(basePath)) {
            throw new ForbiddenException("非法路径");
        }
        return target;
    }

    private String normalizeType(String type) {
        if (StringUtil.isEmpty(type)) {
            return "default";
        }
        String cleaned = type.replaceAll("[^a-zA-Z0-9_-]", "");
        return StringUtil.isEmpty(cleaned) ? "default" : cleaned;
    }
    
}
