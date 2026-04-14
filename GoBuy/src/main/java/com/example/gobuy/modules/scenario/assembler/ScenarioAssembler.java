package com.example.gobuy.modules.scenario.assembler;

import com.example.gobuy.modules.scenario.entity.Scenario;
import com.example.gobuy.modules.scenario.dto.ScenarioDTO;
import com.example.gobuy.modules.scenario.vo.ScenarioVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

/**
 * 场景配置表 实体转换装配器
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScenarioAssembler {

    /**
     * DTO 转 Entity
     * @param dto DTO 对象
     * @return Entity 对象
     */
    Scenario toEntity(ScenarioDTO dto);

    /**
     * Entity 转 VO
     * @param entity Entity 对象
     * @return VO 对象
     */
    ScenarioVO toVO(Scenario entity);

    /**
     * Entity 列表 转 VO 列表
     * @param entityList Entity 列表
     * @return VO 列表
     */
    List<ScenarioVO> toVOList(List<Scenario> entityList);
}