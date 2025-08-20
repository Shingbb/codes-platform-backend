package com.shing.codesplatformbackend.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.shing.codesplatformbackend.model.dto.app.AppQueryRequest;
import com.shing.codesplatformbackend.model.entity.App;
import com.shing.codesplatformbackend.model.vo.AppVO;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
public interface AppService extends IService<App> {

    /**
     * 获取 AppVO
     *
     * @param app App
     * @return AppVO
     */
    AppVO getAppVO(App app);

    /**
     * 根据查询条件构造查询参数
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

}
