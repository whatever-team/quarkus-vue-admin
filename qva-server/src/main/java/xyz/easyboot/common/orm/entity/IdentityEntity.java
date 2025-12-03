package xyz.easyboot.common.orm.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import xyz.easyboot.common.orm.id.SnowFlakeIdGenerator;
import xyz.easyboot.common.orm.listener.AuditableEntityListener;
import xyz.easyboot.common.orm.listener.IdentityEntityListener;

import java.io.Serializable;

/**
 * 基类
 * @author wujiawei
 * @see
 */
@MappedSuperclass
//@EntityListeners({IdentityEntityListener.class})
public class IdentityEntity extends PanacheEntityBase implements Serializable {
    
    private static final long serialVersionUID = -1L;
    
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    public String id;
    
}
