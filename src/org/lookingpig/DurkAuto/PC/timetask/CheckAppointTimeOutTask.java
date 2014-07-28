package org.lookingpig.DurkAuto.PC.timetask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.TimingTask.Job;

/**
 * 检测预约过期任务
 * @author Pig
 *
 */
public class CheckAppointTimeOutTask implements Job {
	private static final Logger logger = LogManager.getLogger(CheckAppointTimeOutTask.class);
	private static CheckAppointTimeOutTask task;
	
	private CheckAppointTimeOutTask() {
		
	}
	
	public static CheckAppointTimeOutTask getTask() {
		if (null == task) {
			task = new CheckAppointTimeOutTask();
		}
		
		return task;
	}
	
	@Override
	public void execute() {
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			logger.warn("检测预约过期任务无法执行，原因：数据服务无法使用！");
			return;
		}
		
		service.execute("Appointment_AppointTimeOut", null);
	}

}
