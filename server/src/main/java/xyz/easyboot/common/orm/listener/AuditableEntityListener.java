package xyz.easyboot.common.orm.listener;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.orm.entity.AuditableEntity;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/29 11:31 下午
 */
@Slf4j
public class AuditableEntityListener {
    
    public AuditableEntityListener() {}
    
    @PrePersist
    public void prePersist(Object target) {
       if (target instanceof AuditableEntity) {
           AuditableEntity entity = (AuditableEntity) target;
           entity.createTime = new Date();
           
           try {
               entity.createBy = "default";
           } catch (Exception e) {
               log.error("Audit error, persist {} . {}", entity.getClass().getName(), JSONUtil.toJsonStr(entity));
           }
       }
    }
    
    @PreUpdate
    public void preUpdate(Object target) {
        if (target instanceof AuditableEntity) {
            AuditableEntity entity = (AuditableEntity) target;
            entity.lastModifyTime = new Date();
        
            try {
                entity.lastModifyBy = "default";
            } catch (Exception e) {
                log.error("Audit error, update {} . {}", entity.getClass().getName(), JSONUtil.toJsonStr(entity));
            }
        }
    }
    
    @PreRemove
    public void preRemove(Object o) {
    
    }
    
    @PostLoad
    public void postLoad(Object o) {
    
    }
    
    @PostRemove
    public void postRemove(Object o) {
    
    }
    
    @PostUpdate
    public void postUpdate(Object o) {
    
    }
    
    @PostPersist
    public void postPersist(Object o) {
    
    }
}
