package com.shing.codesplatformbackend.model.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用部署请求
 *
 * @author Shing
 * date 28/8/2025
 */
@Data
public class AppDeployRequest implements Serializable {

    /**
     * 应用 id
     */
    @NotNull(message = "应用 id 不能为空")
    private Long appId;

    @Serial
    private static final long serialVersionUID = 1L;

}