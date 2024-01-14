package org.example.design.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@TableName(value = "relic")
@Data
public class Relic implements Serializable {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Integer id;
    private String type;
    private String name;
    private LocalDateTime time;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}