package com.shing.codesplatformbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shing.codesplatformbackend.exception.BusinessException;
import com.shing.codesplatformbackend.exception.ErrorCode;
import com.shing.codesplatformbackend.exception.ThrowUtils;
import com.shing.codesplatformbackend.model.dto.app.AppAddRequest;
import com.shing.codesplatformbackend.model.dto.app.AppQueryRequest;
import com.shing.codesplatformbackend.model.entity.App;
import com.shing.codesplatformbackend.mapper.AppMapper;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.enums.CodeGenTypeEnum;
import com.shing.codesplatformbackend.model.vo.AppVO;
import com.shing.codesplatformbackend.model.vo.UserVO;
import com.shing.codesplatformbackend.service.AppService;
import com.shing.codesplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    UserService userService;

    @Override
    public long addApp(AppAddRequest appAddRequest, User loginUser) {
        // 构造入库对象
        App app = new App();
        BeanUtil.copyProperties(appAddRequest, app);
        app.setUserId(loginUser.getId());

        // 应用名称 = initPrompt 前 12 位
        String initPrompt = appAddRequest.getInitPrompt();
        app.setAppName(initPrompt.substring(0, Math.min(initPrompt.length(), 12)));

        // 默认代码生成类型 = MULTI_FILE
        app.setCodeGenType(CodeGenTypeEnum.MULTI_FILE.getValue());

        // 插入数据库
        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        return app.getId();
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }
}
