package com.ruoyi.web.wp.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.wp.entity.Vip;
import com.ruoyi.web.wp.service.VipService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Vip)表控制层
 *
 * @author makejava
 * @since 2024-03-30 12:32:25
 */
@RestController
@RequestMapping("vip")
public class VipController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private VipService vipService;

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAll() {
        return getDataTable(this.vipService.selectList());
    }

    @GetMapping("/num")
    public R selectVipNum() {
        List<Vip> vips = this.vipService.selectVipList();
        return R.ok(vips.size());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(this.vipService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param vip 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Vip vip) {
        return R.ok(this.vipService.save(vip));
    }

    /**
     * 修改数据
     *
     * @param vip 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Vip vip) {
        return R.ok(this.vipService.updateById(vip));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return R.ok(this.vipService.removeByIds(idList));
    }
}

