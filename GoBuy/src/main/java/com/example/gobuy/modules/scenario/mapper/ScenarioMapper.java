package com.example.gobuy.modules.scenario.mapper;

import com.example.gobuy.modules.scenario.entity.Scenario;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场景配置表 Mapper 接口
 */
@Mapper
public interface ScenarioMapper extends BaseMapper<Scenario> {
}