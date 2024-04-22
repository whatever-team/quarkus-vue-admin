package xyz.easyboot.common.orm.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

/**
 * 基类
 * @author wujiawei
 * @see
 */
@MappedSuperclass
public class BaseEntity extends PanacheEntityBase implements Serializable {
    
    private static final long serialVersionUID = -1L;
    
    @JsonDeserialize(using = NumberDeserializers.NumberDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    @GeneratedValue(generator = "snowFlakeGenerator")
    @GenericGenerator(name = "snowFlakeGenerator", strategy = "xyz.easyboot.common.orm.id.SnowFlakeIdGenerator")
    @Column(nullable = false)
    public Long id;
    
}
