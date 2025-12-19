package io.lighting.common.base.dto;

import lombok.Data;
import io.lighting.common.orm.entity.IdentityEntity;

/**
 * 分页信息
 *
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:48 下午
 */
@Data
public class PageInfo<T extends IdentityEntity> {
    
    private Integer pageIndex;
    
    private Integer pageSize;
    
    private Long totalCount;
    
    private Integer totalPage;
    
    public PageInfo(int pageIndex, int pageSize, long totalCount, int totalPage) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
    }

    public PageInfo(int pageIndex, int pageSize, long totalCount) {
        this(pageIndex, pageSize, totalCount, countTotalPage(totalCount, pageSize));
    }
    
    private static int countTotalPage(long totalCount, int pageSize) {
        int var = (int) totalCount % pageSize;
        int var2 = (int) totalCount / pageSize;
        return var > 0 ? var2 + 1 : var2;
    }
}
