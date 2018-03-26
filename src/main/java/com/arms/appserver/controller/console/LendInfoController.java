package com.arms.appserver.controller.console;

import com.arms.appserver.vo.LendInfoVO;
import com.arms.appserver.vo.console.ListLendInfoVO;
import com.arms.common.excel.ExcelUtil;
import com.arms.common.util.PageUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.bo.UserLendInfoBo;
import com.arms.service.model.LendInfo;
import com.arms.service.model.Terrace;
import com.arms.service.model.User;
import com.arms.service.service.LendInfoService;
import com.arms.service.service.TerraceService;
import com.arms.service.service.UserService;
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
import javax.servlet.http.HttpServletRequest;
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
 * @since 2017/12/7
 */
@Controller
public class LendInfoController {

    @Resource
    private LendInfoService lendInfoService;

    @Resource
    private UserService userService;

    @Resource
    private TerraceService terraceService;

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/lend_info/list_page", method = RequestMethod.GET)
    public String getLendInfoList(Model model,
                                  @RequestParam(required = false, defaultValue = "") String source,
                                  @RequestParam(required = false, defaultValue = "") String terraceName,
                                  @RequestParam(required = false) Timestamp startTime,
                                  @RequestParam(required = false) Timestamp endTime,
                                  @RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "12") int pageSize) {

        DateTime now = DateTime.now();
        if (endTime == null) {
            endTime = new Timestamp(DateTime.now().getMillis());
        }
        if (startTime == null) {
            startTime = new Timestamp(new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0).getMillis());
        }
        String terraceNameUtf8 = "";
        try {
            terraceNameUtf8 = new String(terraceName.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int totalNum = lendInfoService.countUserLendInfoBo(source, terraceNameUtf8, startTime, endTime);
        int pageCount = PageUtil.calcPageTotal(totalNum, pageSize);
        List<UserLendInfoBo> userLendInfoBoList = lendInfoService.getUserLendInfoBo(source, terraceNameUtf8, startTime, endTime, page, pageSize);
        List<ListLendInfoVO> listLendInfoVOList = new ArrayList<>();
        for (UserLendInfoBo userLendInfoBo : userLendInfoBoList) {
            if (userLendInfoBo == null) {
                continue;
            }
            ListLendInfoVO listLendInfoVO = new ListLendInfoVO();
            listLendInfoVO.setTerraceName(userLendInfoBo.getTerraceName());
            listLendInfoVO.setTime(userLendInfoBo.getApplyForTime());
            listLendInfoVO.setCellphone(userLendInfoBo.getCellphone());
            listLendInfoVO.setApplyForAmount(userLendInfoBo.getApplyForAmount());
            listLendInfoVO.setTimeLimit(userLendInfoBo.getTimeLimit());
            listLendInfoVO.setLendInfoId(userLendInfoBo.getLendInfoId());
            listLendInfoVO.setSource(userLendInfoBo.getSource());
            listLendInfoVOList.add(listLendInfoVO);
        }
        model.addAttribute(listLendInfoVOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startTime", new DateTime(startTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("endTime", new DateTime(endTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("source", source);
        model.addAttribute("terraceName", terraceNameUtf8);
        model.addAttribute("totalNum",totalNum);
        return "page/console/lend-info-list";
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/lend_info/export_list", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportList(@RequestParam(required = false, defaultValue = "") String source,
                                             @RequestParam(required = false, defaultValue = "") String terraceName,
                                             @RequestParam(required = false) Timestamp startTime,
                                             @RequestParam(required = false) Timestamp endTime) throws IOException {
        DateTime now = DateTime.now();

        if (endTime == null) {
            endTime = new Timestamp(DateTime.now().getMillis());
        }
        if (startTime == null) {
            startTime = new Timestamp(new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0).getMillis());
        }
        String terraceNameUtf8 = "";
        try {
            terraceNameUtf8 = new String(terraceName.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<UserLendInfoBo> userLendInfoBoList = lendInfoService.getUserLendInfoBo(source, terraceNameUtf8, startTime, endTime, 0, 0);
        Map<String, String> titleMap = new LinkedHashMap<>();

        titleMap.put("source","渠道编号");
        titleMap.put("cellphone", "手机号");
        titleMap.put("terraceName", "申请平台");
        titleMap.put("applyForAmount", "申请金额(元)");
        titleMap.put("timeLimit", "申请期限(天)");
        titleMap.put("applyForTime", "申请时间");

        String filedisplay = ExcelUtil.excelExport(userLendInfoBoList, titleMap, "借贷信息");

        File file = new File("tem/excel/" + filedisplay);

        try {
            String dfileName = new String(filedisplay.getBytes("gb2312"), "iso8859-1");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", dfileName);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } finally {
            file.delete();
        }
    }
}
