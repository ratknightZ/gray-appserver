package com.arms.appserver.manager;

import com.arms.service.enums.UnitOfTime;
import com.arms.service.exception.UserException;

/**
 * Created by Administrator on 2017/11/26.
 */
public interface ActivityManager {

    void bindingSuperior(int userId, int superiorUserId) throws UserException;

    void dailyAttendance(int userId);

    int getUserJoinCount(int userId, int activityId, UnitOfTime unitOfTime);
}
