package com.shing.codesplatformbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.mybatisflex.core.paginate.Page;
import com.shing.codesplatformbackend.common.BaseResponse;
import com.shing.codesplatformbackend.common.ResultUtils;
import com.shing.codesplatformbackend.exception.ErrorCode;
import com.shing.codesplatformbackend.exception.ThrowUtils;
import com.shing.codesplatformbackend.model.dto.app.AppAddRequest;
import com.shing.codesplatformbackend.model.entity.App;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.vo.AppVO;
import com.shing.codesplatformbackend.service.AppService;
import com.shing.codesplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/add")
    @SaCheckRole(value = {"user", "admin"}, mode = SaMode.OR)
    public BaseResponse<Long> addApp(@Validated @RequestBody AppAddRequest appAddRequest) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 交给 service 处理逻辑
        long appId = appService.addApp(appAddRequest, loginUser);
        return ResultUtils.success(appId);
    }

    /**
     * 根据 id 获取应用详情
     *
     * @param id 应用 id
     * @return 应用详情
     */
    @GetMapping("/get/vo")
    public BaseResponse<AppVO> getAppVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类（包含用户信息）
        return ResultUtils.success(appService.getAppVO(app));
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
