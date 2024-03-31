package com.ruoyi.web.wp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.wp.mapper.VipMapper;
import com.ruoyi.web.wp.entity.Vip;
import com.ruoyi.web.wp.service.VipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Vip)表服务实现类
 *
 * @author makejava
 * @since 2024-03-30 12:32:29
 */
@Service("vipService")
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements VipService {
    @Resource
    private VipMapper vipDao;

    @Override
    public List<Vip> selectList() {
        QueryWrapper<Vip> vipQueryWrapper = new QueryWrapper<>();
        return vipDao.selectList(vipQueryWrapper);
    }

    @Override
    public List<Vip> selectVipList() {
        QueryWrapper<Vip> vipQueryWrapper = new QueryWrapper<>();
        vipQueryWrapper.eq("status", 0);
        return vipDao.selectList(vipQueryWrapper);
    }
}

