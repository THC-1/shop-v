package com.example.gobuy.modules.scenario.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("scenario_products")
public class ScenarioProduct extends BaseEntity {

    private Long scenarioId;

    private Long productId;

    private String recommendReason;

    private Integer sort;
}
