package com.ruoyi.web.wp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.wp.entity.Vip;

import java.util.List;

/**
 * (Vip)表服务接口
 *
 * @author makejava
 * @since 2024-03-30 12:32:29
 */
public interface VipService extends IService<Vip> {

    List<Vip> selectList();
    List<Vip> selectVipList();

    List<Vip> selectCommonList();
}

