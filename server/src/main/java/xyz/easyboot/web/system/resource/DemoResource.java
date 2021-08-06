package xyz.easyboot.web.system.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.BusinessLog;
import xyz.easyboot.web.system.dto.export.SysVarExportDTO;
import xyz.easyboot.web.system.entity.SysVar;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 3:15 下午
 */
@Slf4j
@Path("demo")
public class DemoResource {
    
    @Context
    HttpServerResponse response;
    
    @BusinessLog
    @GET
    @Path("hello")
    public Result<String> hello(@QueryParam("a") String a, @QueryParam("b") Integer b, @QueryParam("c") String ignore) {
        return new Result<>("hello");
    }
    
    @GET
    @Path("download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download() throws UnsupportedEncodingException {
        String filePath = "/Users/wujiawei/Downloads/调查表汇总2021.08.04 09_00_13.xlsx";
        String fileName = URLEncoder.encode("调查表汇总" + DateUtil.now(), "UTF-8").replaceAll("\\+", "%20");
        File file = FileUtil.file(filePath);
        return Response.ok(file)
                .header("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx")
                .header("Content-Length", file.length())
                .build();
    }
    
    @POST
    @Path("upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({ MediaType.TEXT_PLAIN })
    public Response upload(@MultipartForm UploadEntity uploadEntity, @Context HttpServerRequest request) {
        String[] contentDisposition = uploadEntity.getFile().getHeaders().getFirst("Content-Disposition").split(";");
        String fileName = "";
        for (String name : contentDisposition) {
            if (name.trim().startsWith("filename=")) {
                String[] arr = name.split("=");
                fileName = arr[1].trim().replaceAll("\"", "");
//                fileName = URLDecoder.decode(fileName, "UTF-8");
                fileName = new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                log.info(fileName);
//                fileName = CharsetUtil.convert(fileName, "ISO-8859-1", "UTF-8");
            }
        }
        log.info("文件名: {}", fileName);
        return Response.ok(fileName).build();
    }
    
    public static class UploadEntity {
        @FormParam("file")
        private InputPart file;
    
        public InputPart getFile() {
            return file;
        }
    
        public void setFile(InputPart file) {
            this.file = file;
        }
    }
    
    @Getter
    @Setter
    public static class FormData {
        @FormParam("file")
        private FileUpload file;
    }
    
    @POST
    @Path("export")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportExcel() {
        List<SysVar> list = SysVar.listAll();
        List<SysVarExportDTO> exportList = SysVarExportDTO.fromSysVar(list);
    
        StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream out) {
                EasyExcel.write(out, SysVarExportDTO.class)
                        .sheet("sheet1")
                        .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20))
                        .doWrite(exportList);
            }
        };
    
        String fileName = URLEncoder.encode("SysVar" + DateUtil.now(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        return Response
                .ok(output)
                .header("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx")
                .build();
        
    }
}
