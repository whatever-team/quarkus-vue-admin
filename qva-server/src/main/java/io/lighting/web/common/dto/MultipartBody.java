package io.lighting.web.common.dto;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 3:27 下午
 */
public class MultipartBody {
    @RestForm("file")
    public FileUpload file;
    
    @RestForm("other")
    public String other;
}
