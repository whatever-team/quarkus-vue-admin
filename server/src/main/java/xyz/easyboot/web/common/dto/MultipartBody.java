package xyz.easyboot.web.common.dto;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.FormParam;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 3:27 下午
 */
public class MultipartBody {
    @FormParam("file")
    public InputPart file;
    
    @FormParam("other")
    public String other;
}
