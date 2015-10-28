package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemErpMeasureHouseOrderServiceImpl;
import com.bluemobi.pro.service.impl.UhoemErpMeasureHouseTaskServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 量房
 */
@Controller
@RequestMapping("/api/measureHouse")
public class MeasureHouseApi {
    @Autowired
    private UhoemErpMeasureHouseOrderServiceImpl uhoemErpMeasureHouseOrderServiceImpl;

    @Autowired
    private UhoemErpMeasureHouseTaskServiceImpl uhoemErpMeasureHouseTaskServiceImpl;

    /**
     * 保存方案（开发中）
     * 参数：
     * contacts 联系人（非必填）
     * contact_mobile 联系方式（非必填）
     * new_home_address 新居地址（非必填）
     * plan_date  量房时间(必填)
     * covered_area 建筑面积(必填)
     * house_type 户型(必填)
     * participants_number 参与人数（必填）
     * is_tailored_taxi_service 是否接受专车服务（必填）
     * receive_taxi_date 接车时间（非必填）
     * receive_taxi_address 接车地点（非必填）
     * member 用户id （必填）
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "plan_date", "covered_area", "house_type", "participants_number",
                    "is_tailored_taxi_service","member")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            uhoemErpMeasureHouseOrderServiceImpl.save(params);
            return ResultUtils.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 查询量房流程
     * 查询uhoemErpMeasureHouseOrder，判断是否有数据，
     * 如果没有数据表示需要提交量房信息，如果有数据，则去查询后续的流程信息
     * 参数：
     * member
     */
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> task(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "member")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            Map<String,Object> result = new HashMap<String,Object>();
            Map<String, Object> temp = uhoemErpMeasureHouseOrderServiceImpl.findByMember(params);
            if(temp==null || temp.size()==0){
                result.put("status","measureHourse");
            }else {
                result.put("status","task");
                Map<String, Object> taskparam = new HashMap<String, Object>();
                taskparam.put("orderid",temp.get("id"));
                Map<String, Object> task = uhoemErpMeasureHouseTaskServiceImpl.findMyTask(taskparam, (String) params.get("member"));
                result.put("task",task);
            }
            return ResultUtils.parse(result,"mytask");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 查询量房页面的基础信息：户型，面积，新居地址
     * 参数：
     * member
     */
    @RequestMapping(value = "/baseinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> baseinfo(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "member")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Map<String, Object> baseinfo = uhoemErpMeasureHouseOrderServiceImpl.iBaseInfo(params);
            if(baseinfo == null){
                baseinfo = new HashMap<String, Object>();
            }
            return ResultUtils.parse(baseinfo,"baseinfo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}
