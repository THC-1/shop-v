package com.example.gobuy.modules.scenario.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.scenario.dto.ScenarioDTO;
import com.example.gobuy.modules.scenario.entity.Scenario;

import java.util.List;

public interface IScenarioService extends IService<Scenario> {

    List<Scenario> listRecommendScenarios(Integer limit);

    List<Product> getScenarioProducts(Long scenarioId);

    Scenario createScenario(ScenarioDTO dto);

    Scenario updateScenario(Long scenarioId, ScenarioDTO dto);

    void deleteScenario(Long scenarioId);
}
