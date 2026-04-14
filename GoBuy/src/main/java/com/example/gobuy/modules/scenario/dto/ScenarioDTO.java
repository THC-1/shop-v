package com.example.gobuy.modules.scenario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScenarioDTO {

    @NotBlank(message = "场景名称不能为空")
    private String name;

    private String description;

    private String coverUrl;

    private String configData;
}
