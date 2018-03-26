package com.arms.appserver.controller.console;

import com.arms.common.vo.JsonVO;
import com.arms.service.exception.MessageException;
import com.arms.service.model.Message;
import com.arms.service.service.MessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2017/12/14
 */
@Controller
public class MessageController {

    @Resource
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "/message/send_system_message",method = RequestMethod.POST)
    public String sendSystemMessage(@RequestParam(required = false,defaultValue = "0") int toUserId,
                                    @RequestParam String title,
                                    @RequestParam String content){
        JsonVO json = new JsonVO();
        if (toUserId < 0){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数,toUserId: " + toUserId);
            return json.toString();
        }
        if (StringUtils.isBlank(title)){
            json.setIsSuccess(0);
            json.setMsg("消息标题不能为空");
            json.setMsgForDebug("消息标题不能为空");
            return json.toString();
        }
        if (StringUtils.isBlank(content)){
            json.setIsSuccess(0);
            json.setMsg("跳转平台不存在");
            json.setMsgForDebug("非法参数,content: " + content);
            return json.toString();
        }
        Message message = new Message();
        message.setFromUserId(0);
        message.setToUserId(0);
        message.setTitle(title);
        message.setContent(content);
        try {
            messageService.add(message);
        } catch (MessageException e) {
            json.setIsSuccess(0);
            json.setMsg(e.getMessage());
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
