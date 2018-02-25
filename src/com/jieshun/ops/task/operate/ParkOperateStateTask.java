package com.jieshun.ops.task.operate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.project.model.Project;
import com.jieshun.ops.project.model.ProjectCarPostpayDO;
import com.jieshun.ops.project.model.ProjectOperateDay;
import com.jieshun.ops.util.BeanUtil;

/**
 * 统计任务
 * 
 * @author 刘淦潮
 *
 */
public class ParkOperateStateTask implements Runnable {

	private ParkOperateState parent;
	private Project project;
	private Date bizDate;
	private String platformCode;
	
	private static int PREDAYS = 30;//30天

	private ProjectOperateDay projectOperateDay;

	private SimpleDateFormat dformater = new SimpleDateFormat("yyyy-MM-dd ");

	/**
	 * 构造函数
	 * 
	 * @param project
	 *            项目对象
	 * @param bizDate
	 *            业务日期
	 */
	public ParkOperateStateTask(ParkOperateState parent, String platformCode,
			Project project, Date bizDate) {
		this.parent = parent;
		this.platformCode = platformCode;
		this.project = project;
		this.bizDate = bizDate;
	}

	/**
	 * 统计项目每一天的运营数据
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (project != null && bizDate != null) {
			Calendar stateDate = new GregorianCalendar();
			stateDate.setTime(this.bizDate);
			state(stateDate.getTime());
			// 如果要统计之前的7天，就要处理
			for (int i = 1; i <= PREDAYS; i++) {
				stateDate.add(Calendar.DATE, -1);
				state(stateDate.getTime());
				try {
					Thread.sleep(500 );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void state(Date stateDate) {
		try {
			Calendar stateCalendar = new GregorianCalendar();
			stateCalendar.setTime(stateDate);// 从昨天开始统计
			stateCalendar.set(Calendar.HOUR_OF_DAY, 0);// 00：00：00开始的时候，
			stateCalendar.set(Calendar.MINUTE, 0);
			stateCalendar.set(Calendar.SECOND, 0);
			stateCalendar.set(Calendar.MILLISECOND, 0);
			// calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			Date beginDate = stateCalendar.getTime();
			stateCalendar.set(Calendar.DATE,
					stateCalendar.get(Calendar.DATE) + 1);
			Date endDate = stateCalendar.getTime();
			// 统计云平台线数据支付数据
			// 设置数据源为云平台数据源
			DataSourceContextHolder.setDbType("ds_cloud");
			// 取上线支付笔数及金额
			projectOperateDay = this.parent.getProjectOperateDao()
					.getProjectOperateMPostpayData(project.getCode(),
							dformater.format(beginDate),
							dformater.format(endDate));
			if (projectOperateDay == null) {
				projectOperateDay = new ProjectOperateDay();
				projectOperateDay.setBizDate(stateCalendar.getTime());
				projectOperateDay.setProjectCode(this.project.getCode());
			}
			// 统计线上打折
			ProjectOperateDay mPostpayDiscount = this.parent
					.getProjectOperateDao()
					.getProjectOperateMPostpayDiscountData(project.getCode(),
							dformater.format(beginDate),
							dformater.format(endDate));

			valueCopy(mPostpayDiscount, new String[] { "discountTimes",
					"discountAmount" });
			
			ProjectOperateDay mPrepay=this.parent
			.getProjectOperateDao().getProjectOperateMPrepayData(project.getCode(),
					dformater.format(beginDate),
					dformater.format(endDate));
			valueCopy(mPrepay, new String[] { "mPrepayTimes",
			"mPrepayAmount" });
			// 设置数据源为平台数据源
			DataSourceContextHolder.setDbType(getDbType(platformCode));
			// 取线线下支付笔数及金额
			List<ProjectCarPostpayDO> carPostPayRecords = this.parent
					.getProjectOperateDao().getProjectOperateCPostpayData(
							project.getCode(), dformater.format(beginDate),
							dformater.format(endDate));
			ProjectOperateDay cPostpay = filterCPostpay(carPostPayRecords);
			valueCopy(cPostpay, new String[] { "cPostpayTimes",
					"cPostpayAmount","totalTimes" });

			// 取项目（车场）免费次次数的日度汇总数据，暂不区分临时卡和月卡
			ProjectOperateDay feeTimes = this.parent.getProjectOperateDao()
					.getProjectOperateFeeTimesData(project.getCode(),
							dformater.format(beginDate),
							dformater.format(endDate));
			valueCopy(feeTimes, new String[] { "postpayFeeTimes" });
			// 取项目（车场）每日所有支付方式的月卡缴费次数与金额的汇总数据
			ProjectOperateDay totalPrepay = this.parent.getProjectOperateDao()
					.getProjectOperateTotalPrepayData(project.getCode(),
							dformater.format(beginDate),
							dformater.format(endDate));
			valueCopy(totalPrepay, new String[] { "cPrepayTimes",
					"cPrepayAmount" });
			// 保存到数据库
			saveToDB(projectOperateDay);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parent.decreaseTask();
		}
	}

	/**
	 * 第一版先过滤出临停车出线下支付的次数与金额
	 * 
	 * @param carPostPayRecords
	 * @return
	 */
	private ProjectOperateDay filterCPostpay(
			List<ProjectCarPostpayDO> carPostPayRecords) {
		ProjectOperateDay cPostpay = new ProjectOperateDay();
		int times = 0;
		int totalTimes=0;
		float amount = 0;
		
		for (ProjectCarPostpayDO pcp : carPostPayRecords) {
			if (pcp.getPayTypeName() == null
					|| pcp.getPayTypeName().equals("现金")) {
				times += pcp.getOutTimes();
				amount += pcp.getSsAmount();
				totalTimes=times;
			}else{
				totalTimes+=pcp.getOutTimes();
			}
			
		}
		cPostpay.setCPostpayTimes(times);
		cPostpay.setCPostpayAmount(amount);
		cPostpay.setTotalTimes(totalTimes);
		return cPostpay;
	}

	/**
	 * 对象值复制
	 * 
	 * @param src
	 * @param specifiedProperties
	 *            指定的对象属性名称
	 */
	private void valueCopy(ProjectOperateDay src, String[] specifiedProperties) {
		if (src == null) {
			return;
		}
		try {
			BeanUtil.copyPropertiesInclude(src, this.projectOperateDay,
					specifiedProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据平台代码返回数据源名称
	 * 
	 * @param platformCode
	 *            集成平台编号
	 * @return
	 */
	private String getDbType(String platformCode) {
		return "ds_" + platformCode.toLowerCase();
	}

	/**
	 * 将统计结果保存到数据库
	 * 
	 * @param saveObject
	 */
	private void saveToDB(ProjectOperateDay saveObject) {

		// 使用默认的数据源（运维平台）
		DataSourceContextHolder.clearDbType();
		// 判断是否更新
		ProjectOperateDay exits = this.parent.getProjectOperateDao()
				.getProjectOperateStateDay(saveObject.getProjectCode(),
						dformater.format(saveObject.getBizDate()));

		if (exits != null) {// 如果存在，更新
			saveObject.setId(exits.getId());
			this.parent.getProjectOperateDao()
					.updateProjectOperateDayState(saveObject.getId(),
							saveObject.getProjectCode(),
							saveObject.getBizDate(),
							saveObject.getMPostpayTimes(),
							saveObject.getMPostpayAmount(),
							saveObject.getCPostpayTimes(),
							saveObject.getCPostpayAmount(),
							saveObject.getDiscountTimes(),
							saveObject.getDiscountAmount(),
							saveObject.getPostpayFeeTimes(),
							saveObject.getPrepayFeeTimes(),
							saveObject.getMPrepayTimes(),
							saveObject.getMPrepayAmount(),
							saveObject.getCPrepayTimes(),
							saveObject.getCPrepayAmount(),
							saveObject.getTotalTimes());
		} else {// 否则新增
			// 设置主键
			saveObject.setId(BeanUtil.createUUID());

			this.parent.getProjectOperateDao()
					.addProjectOperateDayState(saveObject.getId(),
							saveObject.getProjectCode(),
							saveObject.getBizDate(),
							saveObject.getMPostpayTimes(),
							saveObject.getMPostpayAmount(),
							saveObject.getCPostpayTimes(),
							saveObject.getCPostpayAmount(),
							saveObject.getDiscountTimes(),
							saveObject.getDiscountAmount(),
							saveObject.getPostpayFeeTimes(),
							saveObject.getPrepayFeeTimes(),
							saveObject.getMPrepayTimes(),
							saveObject.getMPrepayAmount(),
							saveObject.getCPrepayTimes(),
							saveObject.getCPrepayAmount(),
							saveObject.getTotalTimes());

		}

	}
}
