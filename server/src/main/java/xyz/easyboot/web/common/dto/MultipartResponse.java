package xyz.easyboot.web.common.dto;

import lombok.Data;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/20 9:10 上午
 */
@Data
public class MultipartResponse {

    private String name;
    private String path;
    private long size;
    
}
