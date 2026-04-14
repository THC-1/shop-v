package com.example.gobuy.modules.scenario.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("scenarios")
public class Scenario extends BaseEntity {

    private String name;

    private String description;

    private String coverUrl;

    private String configData;
}
