package xyz.easyboot.common.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.orm.entity.BaseEntity;

import java.lang.reflect.InvocationTargetException;

/**
 * 分页信息
 *
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:48 下午
 */
@Slf4j
@Data
@AllArgsConstructor
public class PageInfo<T extends BaseEntity> {
    
    private Integer pageIndex;
    
    private Integer pageSize;
    
    private Long totalCount;
    
    private Integer totalPage;
    
    public PageInfo(Class<T> entityClass, int pageIndex, int pageSize, String query, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.totalCount = (Long) entityClass.getMethod("count", String.class, Object[].class).invoke(null, query, args);
        this.totalPage = countTotalPage(totalCount, pageSize);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
    
    public PageInfo(int pageIndex, int pageSize, String query, Object... args) {
        this.totalCount = T.count(query, args);
        this.totalPage = countTotalPage(totalCount, pageSize);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
    
    public PageInfo(int pageIndex, int pageSize) {
        this(pageIndex, pageSize, "");
    }
    
    private int countTotalPage(long totalCount, int pageSize) {
        int var = (int) totalCount % pageSize;
        int var2 = (int) totalCount / pageSize;
        return var > 0 ? var2 + 1 : var2;
    }
}
