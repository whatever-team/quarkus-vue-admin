package xyz.easyboot.web.business.resource;

import cn.hutool.core.date.DateUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import xyz.easyboot.common.base.BaseResource;
import xyz.easyboot.common.base.dto.PageInfo;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.logging.Logged;
import xyz.easyboot.common.util.BeanUtil;
import xyz.easyboot.common.util.FileUtil;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import xyz.easyboot.web.common.dto.MultipartBody;
import xyz.easyboot.web.common.service.ExcelService;
import xyz.easyboot.web.system.entity.SysVar;
import xyz.easyboot.web.business.service.SysVarService;

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
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2024-04-21.
*/
@Path("/sys-var")
@Produces(value = MediaType.APPLICATION_JSON)
public class SysVarResource extends BaseResource {

    @Inject
    SysVarService service;
    @Inject
    ExcelService excelService;

    @Logged("查询SysVar")
    @GET
    public Result<List<SysVar>> list(
            @QueryParam("sortField") @DefaultValue("seq") String sortField,
            @QueryParam("sortType") @DefaultValue("Ascending") Sort.Direction sortType,
            @QueryParam("pageIndex") @DefaultValue("1") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        // where
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> args = new HashMap<>();

        PanacheQuery<SysVar> panacheQuery = SysVar
                        .find(query.toString(), Sort.by(sortField, sortType), args)
                        .page(Page.of(pageIndex - 1, pageSize));
        List<SysVar> list = panacheQuery.list();
        PageInfo<SysVar> pageInfo = new PageInfo<>(pageIndex, pageSize, panacheQuery.count(), panacheQuery.pageCount());
        return new Result<>(list, pageInfo);
    }

    @Logged
    @GET
    @Path("one/{id}")
    public Result<SysVar> one(@PathParam("id") Long id) {
        SysVar data = SysVar.findById(id);
        return new Result<>(data);
    }

    @Logged("更新SysVar")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Result<SysVar> update(SysVar data) {
        SysVar entity = SysVar.findById(data.id);
        BeanUtil.copyPropertiesIgnoreNull(data, entity);
        return new Result<>(entity);
    }

    @Logged("新增SysVar")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Result<SysVar> add(SysVar data) {
        data.persist();
        return new Result<>(data);
    }

    @Logged("删除SysVar")
    @Transactional
    @DELETE
    @Path("{id}")
    public Result<Object> delete(@PathParam("id") Long id) {
        boolean flag = SysVar.deleteById(id);
        return flag ? Result.defaultResp() : new Result<>(DefaultRespCodeMsg.FAIL);
    }

    @Logged("批量删除SysVar")
    @Transactional
    @DELETE
    @Path("batch")
    public Result<Object> deleteBatch(@QueryParam("ids") @DefaultValue("") String ids) {
        String[] idArr = ids.split(",");
        if (idArr.length == 0) {
            throw new IllegalArgumentException("Please check you parameter, ids must not be empty");
        }
        SysVar.delete("id in (?1)", Arrays.stream(idArr).map(Long::parseLong).collect(Collectors.toList()));
        return Result.defaultResp();
    }

    @Logged("导出SysVar到Excel")
    @POST
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportExcel() {
        List<SysVar> exportList = SysVar.listAll();
        File target = excelService.doExport("SysVar" + DateUtil.now(), SysVar.class, exportList);
        return Response.ok(target)
                .header("Content-disposition", "attachment;filename=" + target.getName())
                .build();
    }

    @Logged("导入SysVar")
    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Result<Object> importExcel(@MultipartForm MultipartBody body) throws IOException {
        InputStream is = FileUtil.getInputStreamFromInputPart(body.file);
        excelService.doImport(is, SysVar.class, SysVar::persist);
        return Result.defaultResp();
    }

}
