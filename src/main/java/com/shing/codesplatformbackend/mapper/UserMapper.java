package com.shing.codesplatformbackend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.shing.codesplatformbackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 映射层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
