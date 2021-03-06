package com.adc.da.pc_execute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adc.da.sys.annotation.EnableAccess;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adc.da.base.web.BaseController;
import com.adc.da.common.ConstantUtils;
import com.adc.da.log.annotation.BusinessLog;
import com.adc.da.pc_execute.entity.PCMaterialsOutEO;
import com.adc.da.pc_execute.page.PCMaterialsOutEOPage;
import com.adc.da.pc_execute.service.PCMaterialsOutEOService;
import com.adc.da.pc_execute.service.PVCVService;
import com.adc.da.pc_execute.vo.PCMaterialsOutSearchVO;
import com.adc.da.pc_execute.vo.PCMaterialsOutVO;
import com.adc.da.sys.entity.RequestEO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * PC物资出门申请单
 * @author Administrator
 * @date 2019年11月27日
 */
@RestController
@RequestMapping("${restPath}/pcTrial/materialsOut")
@Api(tags = "PcTrialTask-PV/CV试验执行模块")
@SuppressWarnings("all")
public class PCMaterialsOutEOController extends BaseController<PCMaterialsOutEO> {
	/**
	 * 用户记录日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(PCMaterialsOutEOController.class);
	
	/**
     * eo vo 转换
     * @see dozer
     */
    @Autowired
    BeanMapper beanMapper;
    
    @Autowired
    private PCMaterialsOutEOService pcMaterialsOutEOService;


    @Autowired
    private PVCVService PVCVService;
    
    
	/**
	 * 物资出门申请单-分页查询
	 * @Title: page
	 * @param page
	 * @param searchPhrase
	 * @return
	 * @throws Exception
	 * @return List<PCMaterialsOutEO>
	 * @author: ljy
	 * @date: 2019年11月27日
	 */
    @ApiOperation(value = "|物资出门申请单分页查询")
    @BusinessLog(description = "物资出门申请单-分页查询")
    @GetMapping("/page")
    @RequiresPermissions("pcTrial:materialsOut:page")
    public ResponseMessage<PageInfo<PCMaterialsOutSearchVO>> page(
    		PCMaterialsOutEOPage page,
    		@RequestParam(value = "searchPhrase", required = false) String searchPhrase){
    	try {
    		List<PCMaterialsOutEO> rows = pcMaterialsOutEOService.page(page, searchPhrase);
    		//设置总条数
			Integer rowsCount = pcMaterialsOutEOService.queryByCount(page);
			page.getPager().setRowCount(rowsCount);
			return Result.success("0", "查询成功", beanMapper.mapPage(
					getPageInfo(page.getPager(), rows), PCMaterialsOutSearchVO.class));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1", "查询失败!");
		}
    }
    
    
	/**
	 * 保存物资出门单信息
	 * @Title: save
	 * @param pcMaterialsOutEO
	 * @return
	 * @return PCMaterialsOutEO
	 * @author: ljy
	 * @date: 2019年11月27日
	 */
    @ApiOperation(value = "|物资出门申请单保存")
    @BusinessLog(description = "物资出门申请单-保存")
    @PostMapping("/save")
    @RequiresPermissions("pcTrial:materialsOut:save")
    public ResponseMessage<PCMaterialsOutVO> save(
    		@RequestBody PCMaterialsOutVO pcMaterialsOutVO){
    	try {
			PCMaterialsOutEO pcMaterialsOutEO = pcMaterialsOutEOService.save(
					beanMapper.map(pcMaterialsOutVO, PCMaterialsOutEO.class));
			return Result.success("0", "新增成功!", beanMapper.map(pcMaterialsOutEO, PCMaterialsOutVO.class));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1", "新增失败!");
		}
    }
    
    
    
    /**
     * 物资出门申请单-编辑
     * @Title: edit
     * @param pcMaterialsOutVO
     * @return
     * @return ResponseMessage<PCMaterialsOutVO>
     * @author: ljy
     * @date: 2019年11月27日
     */
    @ApiOperation(value = "|物资出门申请单编辑")
    @BusinessLog(description = "物资出门申请单-编辑")
    @PutMapping("/edit")
    @RequiresPermissions("pcTrial:materialsOut:save")
    public ResponseMessage<PCMaterialsOutVO> edit(
    		@RequestBody PCMaterialsOutVO pcMaterialsOutVO){
    	try {
			PCMaterialsOutEO pcMaterialsOutEO = pcMaterialsOutEOService.edit(
					beanMapper.map(pcMaterialsOutVO, PCMaterialsOutEO.class));
			return Result.success("0", "编辑成功!", beanMapper.map(pcMaterialsOutEO, PCMaterialsOutVO.class));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1", "编辑失败!");
		}
    }
    
    
    
    /**
     * 物资出门申请单-详情
     * @Title: getDetialById
     * @param id
     * @return
     * @return ResponseMessage<PCMaterialsOutVO>
     * @author: ljy
     * @date: 2019年11月28日
     */
    @ApiOperation(value = "|物资出门申请单详情")
    @BusinessLog(description = "物资出门申请单-详情")
    @GetMapping("/{id}")
    @RequiresPermissions("pcTrial:materialsOut:save")
    public ResponseMessage<PCMaterialsOutVO> getDetialById(
    		@PathVariable(value = "id") String id){
    	try {
    		return Result.success("0", "查询成功", 
    				beanMapper.map(pcMaterialsOutEOService.selectByPrimaryKey(id), 
    						PCMaterialsOutVO.class));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1","查询失败!");
		}
    }
    
    
    
    /**
     * 物资出门申请单-单号
     * @Title: getMaterialsOutNo
     * @return
     * @return ResponseMessage
     * @author: ljy
     * @date: 2019年12月3日
     */
    @ApiOperation(value = "|物资出门申请单单号")
    @BusinessLog(description = "物资出门申请单-单号")
    @GetMapping("/getMaterialsOutNo")
    @RequiresPermissions("pcTrial:materialsOut:save")
    public ResponseMessage getMaterialsOutNo() {
    	try {
			return Result.success("0", "获取成功!",
					pcMaterialsOutEOService.getMaterialsOutNo());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1","获取失败!");
		}
    }
    
    
    /**
     * 物资出门申请单-删除
     * @Title: deleteById
     * @param id
     * @return
     * @return ResponseMessage
     * @author: ljy
     * @date: 2019年11月28日
     */
    @ApiOperation(value = "|物资出门申请单删除")
    @BusinessLog(description = "物资出门申请单-删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("pcTrial:materialsOut:delete")
    public ResponseMessage deleteById(@PathVariable(value = "id") String id) {
    	try {
    		pcMaterialsOutEOService.deleteById(id);
			return Result.success("0", "删除成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1","删除失败!");
		}
    }
    
    
    
    /**
     * 物资出门申请单-导出
     * @Title: exportPCMaterialsOut
     * @param response
     * @param request
     * @param id
     * @return
     * @return ResponseMessage
     * @author: ljy
     * @date: 2019年11月28日
     */
    @ApiOperation(value = "|物资出门申请单导出")
    @BusinessLog(description = "物资出门申请单-导出")
    @GetMapping("/export")
    @RequiresPermissions("pcTrial:materialsOut:export")
    public  ResponseMessage exportPCMaterialsOut(HttpServletResponse response, 
    		HttpServletRequest request, String id){
    	try {
    		pcMaterialsOutEOService.exportPCMaterialsOut(response, request, id);
			return Result.success("0", "导出成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-1","导出失败!");
		}
    }
    
   
    
    /**
     * 物资出门单-流程启动
     * @Title: submitMaterialsOut
     * @param pcMaterialsOutVO
     * @return
     * @return ResponseMessage
     * @author: ljy
     * @date: 2019年11月28日
     */
    @ApiOperation(value = "|物资出门申请单-流程启动")
    @BusinessLog(description = "物资出门申请单-流程启动")
    @PostMapping("/startProcess")
    @RequiresPermissions("pcTrial:materialsOut:startProcess")
    public ResponseMessage submitMaterialsOut(
            @RequestBody PCMaterialsOutVO pcMaterialsOutVO) {
        try {
        	//判断下一节点是否有审批人
            JSONObject jsonObj = PVCVService.getStartNextNodeInfo(ConstantUtils.PV_MATERIALS_OUT_TYPE, 
            		ConstantUtils.CV_MATERIALS_OUT_TYPE);
            if("-1".equals(jsonObj.getString("isSuccess"))) {
            	return Result.error("-1", "下一节点没有审批人，请联系管理员配置后在进行审批");
            }
        	pcMaterialsOutEOService.submitMaterialsOut(
                    beanMapper.map(pcMaterialsOutVO, PCMaterialsOutEO.class), jsonObj.getString("flag"));
            return Result.error("0", "流程启动成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error("-1", "流程启动失败!");
        }
    }
    
    
    
    
    /**
     * 物资出门申请单-流程审批
     * @Title: approvalProcess
     * @param request
     * @param requestEO
     * @return
     * @return ResponseMessage
     * @author: ljy
     * @date: 2019年11月29日
     */
    @ApiOperation(value = "|物资出门申请单流程审批")
    @BusinessLog(description = "物资出门申请单-流程审批")
    @PostMapping("/approvalProcess")
    @RequiresPermissions("pcTrial:materialsOut:approvalProcess")
    @EnableAccess
    public ResponseMessage approvalProcess(
    		HttpServletRequest request, @RequestBody RequestEO requestEO) {
        try {
            //校验是否为空
            if (StringUtils.isEmpty(requestEO)) {
                return Result.error("-1", "审批信息不可为空");
            } else {
                if (StringUtils.isEmpty(requestEO.getBaseBusId())) {
                    return Result.error("-1", "业务Id不可为空");
                }
                if (StringUtils.isEmpty(requestEO.getTaskId())) {
                    return Result.error("-1", "任务Id不可为空");
                }
                if (StringUtils.isEmpty(requestEO.getProcId())) {
                    return Result.error("-1", "流程实例Id不可为空");
                }
                if (StringUtils.isEmpty(requestEO.getVariables())) {
                    return Result.error("-1", "审批意见不可为空");
                }
            }
            //判断下一节点是否有审批人
            String str = PVCVService.getNextNodeInfo(requestEO);
            if("-1".equals(str)) {
            	return Result.error("-1", "下一节点没有审批人，请联系管理员配置后在进行审批");
            }
            pcMaterialsOutEOService.approvalProcess(request, requestEO);
            return Result.success("0", "流程审核成功!");
        } catch (Exception e) {
            logger.error("-1", "流程审批失败！");
            return Result.error("-1", "流程审批失败！");
        }
    }
    
    
}
