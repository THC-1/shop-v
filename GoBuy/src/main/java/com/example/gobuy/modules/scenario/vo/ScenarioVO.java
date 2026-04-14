package com.example.gobuy.modules.scenario.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 场景配置表 VO (视图对象)
 */
@Data
public class ScenarioVO {

    /**
     * 场景ID
     */
    private Long id;

    /**
     * 场景名称
     */
    private String name;

    /**
     * 场景详尽描述
     */
    private String description;

    /**
     * 场景封面图链接
     */
    private String coverUrl;

    /**
     * 场景配置参数或扩展元数据
     */
    private String configData;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}