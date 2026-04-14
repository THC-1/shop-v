package com.example.gobuy.modules.scenario.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import com.example.gobuy.modules.scenario.dto.ScenarioDTO;
import com.example.gobuy.modules.scenario.entity.Scenario;
import com.example.gobuy.modules.scenario.entity.ScenarioProduct;
import com.example.gobuy.modules.scenario.mapper.ScenarioMapper;
import com.example.gobuy.modules.scenario.mapper.ScenarioProductMapper;
import com.example.gobuy.modules.scenario.service.IScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScenarioServiceImpl extends ServiceImpl<ScenarioMapper, Scenario> implements IScenarioService {

    private final ScenarioProductMapper scenarioProductMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> listRecommendScenarios(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        LambdaQueryWrapper<Scenario> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Scenario::getCreatedAt);
        wrapper.last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getScenarioProducts(Long scenarioId) {
        LambdaQueryWrapper<ScenarioProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenarioProduct::getScenarioId, scenarioId);
        wrapper.orderByAsc(ScenarioProduct::getSort);
        List<ScenarioProduct> scenarioProducts = scenarioProductMapper.selectList(wrapper);

        List<Long> productIds = scenarioProducts.stream()
                .map(ScenarioProduct::getProductId)
                .toList();

        if (productIds.isEmpty()) {
            return List.of();
        }

        return productMapper.selectBatchIds(productIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Scenario createScenario(ScenarioDTO dto) {
        Scenario entity = new Scenario();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setConfigData(dto.getConfigData());
        save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Scenario updateScenario(Long scenarioId, ScenarioDTO dto) {
        Scenario entity = getById(scenarioId);
        if (entity == null) {
            throw new BusinessException(404, "场景不存在");
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setConfigData(dto.getConfigData());
        updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScenario(Long scenarioId) {
        scenarioProductMapper.delete(new LambdaQueryWrapper<ScenarioProduct>()
                .eq(ScenarioProduct::getScenarioId, scenarioId));
        removeById(scenarioId);
    }
}
