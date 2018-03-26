package com.arms.appserver.controller.console;

import com.arms.appserver.vo.console.ListUserVO;
import com.arms.common.excel.ExcelUtil;
import com.arms.common.util.PageUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.bo.UserLendSourceIpBo;
import com.arms.service.enums.RoleAuth;
import com.arms.service.exception.UserException;
import com.arms.service.model.User;
import com.arms.service.model.UserIp;
import com.arms.service.service.UserIpService;
import com.arms.service.service.UserService;
import com.arms.service.service.UserSourceService;
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
 * @author zhangchaojie
 * @since 2016-08-25
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserSourceService userSourceService;

    @Resource
    private UserIpService userIpService;

    @ResponseBody
    @RequestMapping(value = "/user/center", method = {RequestMethod.GET, RequestMethod.POST})
    public String userCenter(@RequestParam int userId) {
        JsonVO json = new JsonVO();
        //Test cacheEvict
        User user = userService.get(userId);
        //        user.setNick("nick2");
        //        user.setPassword("password2");
        //        userService.update(user);
        json.setData(user);
        json.setIsSuccess(1);
        return json.toString();
    }

    @Sniff("ROLE_ADMIN")
    @RequestMapping(value = "/user/update_user_role_auth_page", method = RequestMethod.GET)
    public String updateUserRoleAuthPage(Model model, @RequestParam int userId) {
        User user = userService.get(userId);
        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("nick", user.getNick());
            model.addAttribute("avatar", user.getAvatar());
            model.addAttribute("cellphone", user.getCellphone());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("wechatOpenId", user.getWechatOpenId());
            model.addAttribute("roleAuth", user.getRoleAuth());
        }
        return "page/console/update-user-role-auth";
    }

    @Sniff("ROLE_ADMIN")
    @ResponseBody
    @RequestMapping(value = "/user/update_user_role_auth", method = RequestMethod.POST)
    public String updateUserRoleAuth(@RequestParam int userId,
                                     @RequestParam String roleAuth) {
        JsonVO json = new JsonVO();
        User user = userService.get(userId);
        if (user == null) {
            json.setIsSuccess(0);
            json.setMsg("用户不存在");
            json.setMsgForDebug("用户不存在,userId: " + userId);
            return json.toString();
        }
        if (RoleAuth.getRoleAuth(roleAuth) != null) {
            user.setRoleAuth(roleAuth);
            try {
                userService.update(user);
            } catch (UserException e) {
                json.setIsSuccess(0);
                json.setMsg("请求错误");
                json.setMsgForDebug(e.getMessage());
                return json.toString();
            }
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/user/list_page", method = RequestMethod.GET)
    public String listPage(Model model,
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
        int totalNum = userService.countByRoleAuth(startTime, endTime, null);
        int pageCount = PageUtil.calcPageTotal(totalNum, pageSize);
        List<UserLendSourceIpBo> userLendSourceIpBoList = userService.getUserLendInfoBo(startTime, endTime, page, pageSize);
        List<ListUserVO> listUserVOList = new ArrayList<>();
        for (UserLendSourceIpBo userLendSourceIpBo : userLendSourceIpBoList) {
            if (userLendSourceIpBo == null) {
                continue;
            }
            ListUserVO listUserVO = new ListUserVO();
            listUserVO.setUserId(userLendSourceIpBo.getUserId());
            listUserVO.setCellphone(userLendSourceIpBo.getCellphone());
            listUserVO.setCountLendInfo(userLendSourceIpBo.getCountLendInfo());
            listUserVO.setCreateTime(new DateTime(userLendSourceIpBo.getRegisterTime().getTime()).toString("yyyy-MM-dd HH:mm:ss"));
            listUserVO.setSource(userLendSourceIpBo.getSource());
            listUserVO.setIp(userLendSourceIpBo.getIp());
            listUserVO.setCountIp(userLendSourceIpBo.getCountIp());
            listUserVOList.add(listUserVO);
        }
        model.addAttribute("listUserVOList", listUserVOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startTime", new DateTime(startTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("endTime", new DateTime(endTime.getTime()).toString("yyyy-MM-dd"));
        model.addAttribute("totalNum",totalNum);
        return "page/console/user-list";
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/user/export_list", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportList(@RequestParam(required = false) Timestamp startTime,
                                             @RequestParam(required = false) Timestamp endTime) throws IOException {
        DateTime now = DateTime.now();
        if (endTime == null) {
            endTime = new Timestamp(DateTime.now().getMillis());
        }
        if (startTime == null) {
            startTime = new Timestamp(new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0).getMillis());
        }
        List<UserLendSourceIpBo> userLendSourceIpBoList = userService.getUserLendInfoBo(startTime, endTime, 0, 0);

        Map<String, String> titleMap = new LinkedHashMap<>();

        titleMap.put("cellphone", "手机号");
        titleMap.put("countLendInfo","借款次数");
        titleMap.put("registerTime", "注册时间");
        titleMap.put("source", "来源");
        titleMap.put("ip", "注册IP");
        titleMap.put("countIp","IP重复度");

        String filedisplay = ExcelUtil.excelExport(userLendSourceIpBoList, titleMap, "用户信息");

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
