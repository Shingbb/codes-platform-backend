package com.shing.codesplatformbackend.model.dto.app;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Shing
 * date 14/8/2025
 */
@Data
public class AppAddRequest implements Serializable {

    /**
     * 应用初始化的 prompt
     */
    @NotBlank(message = "初始化提示词不能为空")
    private String initPrompt;

    @Serial
    private static final long serialVersionUID = 1L;

}
