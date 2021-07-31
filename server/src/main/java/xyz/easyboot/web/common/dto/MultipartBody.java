package xyz.easyboot.web.common.dto;

import javax.ws.rs.FormParam;
import java.io.File;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 3:27 下午
 */
public class MultipartBody {
    @FormParam("file")
    public File file;
    
    @FormParam("other")
    public String other;
}
