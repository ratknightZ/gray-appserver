package com.arms.appserver.controller.console;

import com.arms.appserver.vo.console.ListSourceDataVO;
import com.arms.common.excel.ExcelUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.model.SourceData;
import com.arms.service.service.StatisticsService;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuchen
 * @since 2018/1/2
 */
@Controller
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/statistics/list_page",method = {RequestMethod.GET,RequestMethod.POST})
    public String listPage(Model model, @RequestParam(required = false) Timestamp startTime,
                           @RequestParam(required = false) Timestamp endTime,
                           @RequestParam(required = false,defaultValue = "") String source){

        if (startTime == null){
            startTime = new Timestamp(DateTime.now().getMillis());
            endTime = new Timestamp(DateTime.now().getMillis());
        }
        List<SourceData> sourceDataList = statisticsService.getSourceData(startTime,endTime,source);
        List<ListSourceDataVO> listSourceDataVOList = new ArrayList<>();
        int totalRegister = 0;
        for (SourceData sourceData : sourceDataList){
            if (sourceData == null){
                continue;
            }
            totalRegister = totalRegister + sourceData.getRegister();
            ListSourceDataVO listSourceDataVO = new ListSourceDataVO();
            listSourceDataVO.setId(sourceData.getId());
            listSourceDataVO.setSource(sourceData.getSource());
            listSourceDataVO.setPv(sourceData.getPv());
            listSourceDataVO.setUv(sourceData.getUv());
            listSourceDataVO.setRegister(sourceData.getRegister());
            listSourceDataVO.setCreateTime(new DateTime(sourceData.getGmtCreate().getTime()).toString("yyyy-MM-dd"));
            listSourceDataVOList.add(listSourceDataVO);
        }
        model.addAttribute("totalRegister", totalRegister);
        model.addAttribute("listSourceDataVOList",listSourceDataVOList);
        model.addAttribute("startTime",new DateTime(startTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("endTime",new DateTime(endTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("source",source);
        return "page/console/source-data-list";
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/statistics/statistics",method = RequestMethod.GET)
    public String statistics(){
        JsonVO json = new JsonVO();
        try {
            statisticsService.statistics();
        } catch (Exception e) {
            json.setIsSuccess(0);
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/statistics/export_list",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseEntity<byte[]> exportList(@RequestParam(required = false) Timestamp startTime,
                                             @RequestParam(required = false) Timestamp endTime,
                                             @RequestParam(required = false,defaultValue = "") String source) throws IOException {

        if (startTime == null){
            startTime = new Timestamp(DateTime.now().getMillis());
        }
        List<SourceData> sourceDataList = statisticsService.getSourceData(startTime,endTime,source);
        List<ListSourceDataVO> listSourceDataVOList = new ArrayList<>();
        for (SourceData sourceData : sourceDataList){
            if (sourceData == null){
                continue;
            }
            ListSourceDataVO listSourceDataVO = new ListSourceDataVO();
            listSourceDataVO.setId(sourceData.getId());
            listSourceDataVO.setSource(sourceData.getSource());
            listSourceDataVO.setPv(sourceData.getPv());
            listSourceDataVO.setUv(sourceData.getUv());
            listSourceDataVO.setRegister(sourceData.getRegister());
            listSourceDataVO.setCreateTime(new DateTime(sourceData.getGmtCreate().getTime()).toString("yyyy-MM-dd"));
            listSourceDataVOList.add(listSourceDataVO);
        }

        Map<String,String> titleMap = new LinkedHashMap<>();

        titleMap.put("source","渠道");
        titleMap.put("pv","PV");
        titleMap.put("uv","UV");
        titleMap.put("register","注册量");
        titleMap.put("createTime","日期");

        String filedisplay = ExcelUtil.excelExport(listSourceDataVOList,titleMap,"渠道推广数据");

        File file = new File("tem/excel/"+filedisplay);

        try {
            String dfileName = new String(filedisplay.getBytes("gb2312"),"iso8859-1");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",dfileName);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
        } finally {
            file.delete();
        }
    }
}
