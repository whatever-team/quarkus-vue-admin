package xyz.easyboot.common.orm.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import xyz.easyboot.common.orm.listener.AuditableEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 审计基类
 * @author wujiawei
 * @see
 * @since 2021/6/29 10:41 下午
 */
@MappedSuperclass
@EntityListeners({AuditableEntityListener.class})
public abstract class AuditableEntity extends BaseEntity {
    
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @Column(updatable = false)
    public Date createTime;
    
    @Column(updatable = false)
    public String createBy;
    
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @Column(insertable = false)
    public Date lastModifyTime;
    
    @Column(insertable = false)
    public String lastModifyBy;
    
    /**
     * 备注
     */
    public String remark;
    
    /**
     * 序号
     */
    public Long seq;
}
