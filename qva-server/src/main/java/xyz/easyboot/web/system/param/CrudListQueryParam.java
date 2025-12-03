package xyz.easyboot.web.system.param;

import io.quarkus.panache.common.Sort;
import lombok.Data;

@Data
public class CrudListQueryParam {

    private String sortField;
    private Sort.Direction sortType = Sort.Direction.Ascending; // 设置默认值
    private int pageIndex = 1; // 设置默认值
    private int pageSize = 20; // 设置默认值

}
