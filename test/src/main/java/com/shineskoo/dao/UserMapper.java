package com.shineskoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Date: 2021/1/15 11:06 上午
 * Author: Cosmos
 * Desc: 数据访问层(DAL)
 */
@Mapper
@Repository
public interface UserMapper {

    String selectByAccount(String account);

    String findPass(String account);

    String findIP(String account);
}