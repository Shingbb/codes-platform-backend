package com.shing.codesplatformbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.shing.codesplatformbackend.common.BaseResponse;
import com.shing.codesplatformbackend.common.DeleteRequest;
import com.shing.codesplatformbackend.common.ResultUtils;
import com.shing.codesplatformbackend.exception.BusinessException;
import com.shing.codesplatformbackend.exception.ErrorCode;
import com.shing.codesplatformbackend.exception.ThrowUtils;
import com.shing.codesplatformbackend.model.dto.app.AppAddRequest;
import com.shing.codesplatformbackend.model.dto.app.AppAdminUpdateRequest;
import com.shing.codesplatformbackend.model.dto.app.AppQueryRequest;
import com.shing.codesplatformbackend.model.dto.app.AppUpdateRequest;
import com.shing.codesplatformbackend.model.entity.App;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.vo.AppVO;
import com.shing.codesplatformbackend.service.AppService;
import com.shing.codesplatformbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 应用 控制层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    // region

    /**
     * 创建应用
     *
     * @param appAddRequest 创建应用请求
     * @return 应用 id
     */
    @Operation(summary = "创建应用", description = "用户创建一个新的应用，需要提供初始化提示词")
    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<Long> addApp(@Validated @RequestBody AppAddRequest appAddRequest) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 创建应用
        long appId = appService.addApp(appAddRequest, loginUser);
        // 返回 appId
        return ResultUtils.success(appId);
    }

    /**
     * 更新应用（用户只能更新自己的应用名称）
     *
     * @param appUpdateRequest 更新应用请求
     * @return 是否成功
     */
    @Operation(summary = "更新应用", description = "用户更新自己的应用信息，目前仅支持更新应用名称")
    @PostMapping("/update")
    @SaCheckRole(value = {"user", "admin"}, mode = SaMode.OR)
    public BaseResponse<Boolean> updateApp(@RequestBody @Validated AppUpdateRequest appUpdateRequest) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 更新应用
        boolean result = appService.updateApp(appUpdateRequest, loginUser);
        // 返回结果
        return ResultUtils.success(result);
    }

    /**
     * 删除应用（用户只能删除自己的应用）
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @Operation(summary = "删除应用", description = "用户删除自己的应用")
    @DeleteMapping("/delete")
    @SaCheckRole(value = {"user", "admin"}, mode = SaMode.OR)
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(appService.deleteApp(deleteRequest.getId()));
    }

    /**
     * 根据 id 获取应用详情
     *
     * @param id 应用 id
     * @return 应用详情
     */
    @Operation(summary = "获取应用详情", description = "根据应用ID获取应用详情信息")
    @GetMapping("/get/vo")
    @SaCheckRole(value = {"user", "admin"}, mode = SaMode.OR)
    public BaseResponse<AppVO> getAppVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.getAppVOById(id));
    }

    /**
     * 分页获取当前用户创建的应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @Operation(summary = "获取我的应用列表", description = "分页获取当前用户创建的应用列表，支持根据名称查询，每页最多20个")
    @PostMapping("/my/list/page/vo")
    @SaCheckRole(value = {"user", "admin"}, mode = SaMode.OR)
    public BaseResponse<Page<AppVO>> listMyAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser();
        return ResultUtils.success(appService.listMyAppVOByPage(appQueryRequest, loginUser));
    }

    /**
     * 管理员删除应用
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @Operation(summary = "管理员删除应用", description = "管理员根据ID删除任意应用")
    @PostMapping("/admin/delete")
    @SaCheckRole(value = "admin")
    public BaseResponse<Boolean> deleteAppByAdmin(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(appService.deleteAppByAdmin(deleteRequest.getId()));
    }

    /**
     * 管理员更新应用
     *
     * @param appAdminUpdateRequest 更新请求
     * @return 更新结果
     */
    @Operation(summary = "管理员更新应用", description = "管理员更新应用信息，支持更新应用名称、封面和优先级")
    @PostMapping("/admin/update")
    @SaCheckRole(value = "admin")
    public BaseResponse<Boolean> updateAppByAdmin(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        if (appAdminUpdateRequest == null || appAdminUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(appService.updateAppByAdmin(appAdminUpdateRequest));
    }

    /**
     * 管理员分页获取应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @Operation(summary = "管理员获取应用列表", description = "管理员分页获取所有应用列表，支持多种字段查询")
    @PostMapping("/admin/list/page/vo")
    @SaCheckRole(value = "admin")
    public BaseResponse<Page<AppVO>> listAppVOByPageByAdmin(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.listAppVOByPageByAdmin(appQueryRequest));
    }

    /**
     * 管理员根据 id 获取应用详情
     *
     * @param id 应用 id
     * @return 应用详情
     */
    @Operation(summary = "管理员获取应用详情", description = "管理员根据ID获取应用详情信息")
    @GetMapping("/admin/get/vo")
    @SaCheckRole(value = "admin")
    public BaseResponse<AppVO> getAppVOByIdByAdmin(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.getAppVOByIdByAdmin(id));
    }

    /**
     * 应用聊天生成代码（流式 SSE）
     *
     * @param appId   应用 ID
     * @param message 用户消息
     * @return 生成结果流
     */
    @Operation(summary = "应用聊天生成代码（流式 SSE）", description = "应用聊天生成代码（流式 SSE）")
    @GetMapping(value = "/chat/gen/code", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @SaCheckLogin
    public Flux<ServerSentEvent<String>> chatToGenCode(@RequestParam Long appId, @RequestParam String message) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");
        ThrowUtils.throwIf(CharSequenceUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 调用服务生成代码（流式）
        Flux<String> contentFlux = appService.chatToGenCode(appId, message, loginUser);
        // 转换为 ServerSentEvent 格式
        return contentFlux
                .map(chunk -> {
                    // 将内容包装成JSON对象
                    Map<String, String> wrapper = Map.of("d", chunk);
                    String jsonData = JSONUtil.toJsonStr(wrapper);
                    return ServerSentEvent.<String>builder()
                            .data(jsonData)
                            .build();
                });
    }

    // endregion


    // region

    /**
     * 保存应用。
     *
     * @param app 应用
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody App app) {
        return appService.save(app);
    }

    /**
     * 根据主键删除应用。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return appService.removeById(id);
    }

    /**
     * 根据主键更新应用。
     *
     * @param app 应用
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody App app) {
        return appService.updateById(app);
    }

    /**
     * 查询所有应用。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<App> list() {
        return appService.list();
    }

    /**
     * 根据主键获取应用。
     *
     * @param id 应用主键
     * @return 应用详情
     */
    @GetMapping("getInfo/{id}")
    public App getInfo(@PathVariable Long id) {
        return appService.getById(id);
    }

    /**
     * 分页查询应用。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<App> page(Page<App> page) {
        return appService.page(page);
    }

    // endregion
}