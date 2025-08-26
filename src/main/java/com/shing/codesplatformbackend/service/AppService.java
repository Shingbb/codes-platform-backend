package com.shing.codesplatformbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.shing.codesplatformbackend.model.dto.app.AppAddRequest;
import com.shing.codesplatformbackend.model.dto.app.AppAdminUpdateRequest;
import com.shing.codesplatformbackend.model.dto.app.AppQueryRequest;
import com.shing.codesplatformbackend.model.dto.app.AppUpdateRequest;
import com.shing.codesplatformbackend.model.entity.App;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
public interface AppService extends IService<App> {

    // ==================== 用户相关操作 ====================

    /**
     * 【用户】创建应用（须填写 initPrompt）
     *
     * @param appAddRequest App 添加参数
     * @param loginUser     当前登录用户
     * @return AppId
     */
    long addApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 【用户】根据 id 修改自己的应用（目前只支持修改应用名称）
     */
    Boolean updateApp(AppUpdateRequest appUpdateRequest, User loginUser);

    /**
     * 【用户】根据 id 删除自己的应用
     *
     * @param id AppId
     * @return 删除结果
     */
    Boolean deleteApp(Long id);

    /**
     * 【用户】根据 id 查看应用详情
     *
     * @param id 应用 id
     * @return AppVO
     */
    AppVO getAppVOById(long id);

    /**
     * 【用户】分页查询自己的应用列表（支持根据名称查询，每页最多 20 个）
     *
     * @param appQueryRequest 查询条件
     * @param loginUser       当前登录用户
     * @return 分页结果
     */
    Page<AppVO> listMyAppVOByPage(AppQueryRequest appQueryRequest, User loginUser);

    /**
     * 【用户】分页查询精选的应用列表（支持根据名称查询，每页最多 20 个）
     *
     * @param appQueryRequest 查询条件
     * @return 分页结果
     */
    Page<AppVO> listRecommendAppVOByPage(AppQueryRequest appQueryRequest);


    // ==================== 管理员相关操作 ====================

    /**
     * 【管理员】根据 id 删除任意应用
     *
     * @param id AppId
     * @return 删除结果
     */
    Boolean deleteAppByAdmin(Long id);

    /**
     * 【管理员】根据 id 更新任意应用（支持更新应用名称、应用封面、优先级）
     */
    Boolean updateAppByAdmin(AppAdminUpdateRequest appAdminUpdateRequest);

    /**
     * 【管理员】分页查询应用列表（支持根据除时间外的任何字段查询，每页数量不限）
     *
     * @param appQueryRequest 查询条件
     * @return 分页结果
     */
    Page<AppVO> listAppVOByPageByAdmin(AppQueryRequest appQueryRequest);

    /**
     * 【管理员】根据 id 查看应用详情
     *
     * @param id 应用 id
     * @return AppVO
     */
    AppVO getAppVOByIdByAdmin(long id);


    // ==================== 公共工具方法 ====================

    /**
     * 获取 AppVO
     *
     * @param app App
     * @return AppVO
     */
    AppVO getAppVO(App app);

    /**
     * 批量获取 AppVO
     *
     * @param appList App列表
     * @return AppVO列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 根据查询条件构造查询参数
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);


    // ==================== AI方法 ====================
    Flux<String> chatToGenCode(Long appId, String message,User loginUser);
}