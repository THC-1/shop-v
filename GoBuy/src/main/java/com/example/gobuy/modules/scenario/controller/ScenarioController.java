package com.example.gobuy.modules.scenario.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.scenario.assembler.ScenarioAssembler;
import com.example.gobuy.modules.scenario.dto.ScenarioDTO;
import com.example.gobuy.modules.scenario.entity.Scenario;
import com.example.gobuy.modules.scenario.service.IScenarioService;
import com.example.gobuy.modules.scenario.vo.ScenarioVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "场景推荐", description = "场景推荐相关接口")
@RestController
@RequestMapping("/api/v1/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final IScenarioService scenarioService;
    private final ScenarioAssembler scenarioAssembler;

    @GetMapping
    @Operation(summary = "获取推荐场景列表", description = "获取推荐场景列表（公开接口，无需认证）")
    public Result<List<ScenarioVO>> listRecommend(
            @Parameter(description = "返回数量", example = "10") @RequestParam(required = false) Integer limit) {
        List<Scenario> scenarios = scenarioService.listRecommendScenarios(limit);
        return Result.success(scenarioAssembler.toVOList(scenarios));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取场景详情", description = "根据场景 ID 获取详细信息（公开接口）")
    public Result<ScenarioVO> getById(
            @Parameter(description = "场景 ID", example = "1") @PathVariable("id") Long id) {
        Scenario entity = scenarioService.getById(id);
        if (entity == null) {
            return Result.fail(404, "未找到相关数据");
        }
        return Result.success(scenarioAssembler.toVO(entity));
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "获取场景推荐商品", description = "根据场景 ID 获取推荐商品列表")
    public Result<List<Product>> getScenarioProducts(
            @Parameter(description = "场景 ID", example = "1") @PathVariable("id") Long id) {
        List<Product> products = scenarioService.getScenarioProducts(id);
        return Result.success(products);
    }

    @PostMapping
    @Operation(summary = "创建场景", description = "创建新的推荐场景")
    public Result<ScenarioVO> create(@Valid @RequestBody ScenarioDTO dto) {
        Scenario entity = scenarioService.createScenario(dto);
        return Result.success(scenarioAssembler.toVO(entity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新场景", description = "更新指定场景的信息")
    public Result<ScenarioVO> update(
            @Parameter(description = "场景 ID", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody ScenarioDTO dto) {
        Scenario entity = scenarioService.updateScenario(id, dto);
        return Result.success(scenarioAssembler.toVO(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除场景", description = "删除指定场景")
    public Result<Void> delete(
            @Parameter(description = "场景 ID", example = "1") @PathVariable("id") Long id) {
        scenarioService.deleteScenario(id);
        return Result.success();
    }
}
