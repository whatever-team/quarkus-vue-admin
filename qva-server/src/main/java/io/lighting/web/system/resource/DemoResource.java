package io.lighting.web.system.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import io.lighting.common.base.dto.Result;
import io.lighting.common.logging.Logged;
import io.lighting.web.system.dto.export.SysVarExportDTO;
import io.lighting.web.system.entity.SysVar;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.ws.rs.BadRequestException;
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
    
    @Logged
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
    public Response upload(@RestForm FileUpload file) {
        if (file == null) {
            throw new BadRequestException("请选择文件");
        }
        String fileName = new String(file.fileName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return Response.ok(fileName).build();
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
