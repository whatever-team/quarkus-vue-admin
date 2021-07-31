package xyz.easyboot.common.orm.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import xyz.easyboot.common.orm.entity.BaseEntity;

import java.io.Serializable;
import java.util.Properties;

/**
 * 雪花算法 ID生成器
 * @author wujiawei
 * @see
 * @since 2021/6/30 9:30 下午
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator, Configurable {
    
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
    
    }
    
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        BaseEntity entity = (BaseEntity) o;
        long id;
        if (entity.id != null && entity.id < Long.MAX_VALUE) {
            id = entity.id;
        } else {
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            id = snowflake.nextId();
        }
        return id;
    }
}
