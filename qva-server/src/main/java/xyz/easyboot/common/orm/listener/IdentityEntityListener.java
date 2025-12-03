package xyz.easyboot.common.orm.listener;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.context.Qva;
import xyz.easyboot.common.orm.entity.AuditableEntity;
import xyz.easyboot.common.orm.entity.IdentityEntity;
import xyz.easyboot.common.orm.id.SnowFlakeIdGenerator;
import xyz.easyboot.common.util.StringUtil;

import java.util.Date;

@Slf4j
public class IdentityEntityListener {

    public IdentityEntityListener() {}
    
    @PrePersist
    public void prePersist(Object target) {
       if (target instanceof IdentityEntity) {
           try {
               IdentityEntity entity = (IdentityEntity) target;
               if (entity.id == null) {
                   Snowflake snowflake = IdUtil.getSnowflake(1, 1);
                   entity.id = snowflake.nextIdStr();
               }
           } catch (Exception e) {
               log.error("Identity error, persist {} . {}", target.getClass().getName(), JSONUtil.toJsonStr(target));
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
