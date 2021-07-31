package xyz.easyboot.common.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:47 下午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultData<T> {
    
    private T data;
    
    private PageInfo page;
    
}
