package xyz.easyboot.common.orm.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 基类
 * @author wujiawei
 * @see
 * @since 2021/6/29 10:38 下午
 */
@MappedSuperclass
public class BaseEntity extends PanacheEntityBase implements Serializable {
    
    public static final long serialVersionUID = -1L;
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    @GeneratedValue(generator = "snowFlakeGenerator")
    @GenericGenerator(name = "snowFlakeGenerator", strategy = "xyz.easyboot.common.orm.id.SnowFlakeIdGenerator")
    @Column(nullable = false)
    public Long id;
    
}
