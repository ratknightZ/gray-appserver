package com.arms.appserver.manager.imp;

import com.arms.appserver.manager.ActivityManager;
import com.arms.service.enums.UnitOfTime;
import com.arms.service.exception.ActivityException;
import com.arms.service.exception.AssetsException;
import com.arms.service.exception.UserException;
import com.arms.service.service.ActivityAssetsService;
import com.arms.service.service.ActivityRecordService;
import com.arms.service.service.SuperiorRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/11/26.
 */
@Service("activityManager")
public class ActivityManagerImp implements ActivityManager {

    @Resource
    private SuperiorRelationService superiorRelationService;

    @Resource
    private ActivityAssetsService activityAssetsService;

    @Resource
    private ActivityRecordService activityRecordService;

    private static final Logger logger = LoggerFactory.getLogger(ActivityManagerImp.class);

    @Override
    public void bindingSuperior(int userId, int superiorUserId) throws UserException {
        superiorRelationService.addSuperiorRelation(userId,superiorUserId);
        try {
            activityAssetsService.reward(superiorUserId,2);
            activityAssetsService.reward(userId,2);
        } catch (ActivityException e) {
            logger.info("",e);
        } catch (AssetsException e) {
            logger.info("",e);
        }
    }

    @Override
    public void dailyAttendance(int userId) {
        try {
            activityAssetsService.reward(userId,3);
        } catch (ActivityException e) {
            logger.info("",e);
        } catch (AssetsException e) {
            logger.info("",e);
        }
    }

    @Override
    public int getUserJoinCount(int userId, int activityId, UnitOfTime unitOfTime) {
        return activityRecordService.getUserJoinCount(userId,activityId,unitOfTime);
    }
}
