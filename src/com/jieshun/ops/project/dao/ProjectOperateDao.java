package com.jieshun.ops.project.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

import com.jieshun.ops.project.model.ProjectOperateDay;
import com.jieshun.ops.project.model.ProjectCarPostpayDO;

/**
 * 项目运营数据统计DAO
 * 
 * @author 刘淦潮
 *
 */
public interface ProjectOperateDao {
	/**
	 * 取项目某天的线上支付次数及金额汇总
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ProjectOperateDay getProjectOperateMPostpayData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	/**
	 * 取项目（车场）某天的月卡延期线上支付笔数及金额
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ProjectOperateDay getProjectOperateMPrepayData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	/**
	 * 取项目（车场）某天的线上打折汇次数与金额
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ProjectOperateDay getProjectOperateMPostpayDiscountData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	/**
	 * 取项目（车场）存在收费的所有收费方式日度汇总数据，在程序里要分类统计出现金支付及其他方式支付的金额汇总
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<ProjectCarPostpayDO> getProjectOperateCPostpayData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	/**
	 * 取项目（车场）免费次次数的日度汇总数据，暂不区分临时卡和月卡
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ProjectOperateDay getProjectOperateFeeTimesData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	/**
	 * 取项目（车场）每日所有支付方式的月卡缴费次数与金额的汇总数据
	 * 
	 * @param projectCode
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ProjectOperateDay getProjectOperateTotalPrepayData(
			@Param("projectCode") String projectCode,
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	void addProjectOperateDayState(@Param("id") String id,
			@Param("projectCode") String projectCode,
			@Param("bizDate") Date bizDate,
			@Param("mPostpayTimes") Integer mPostpayTimes,
			@Param("mPostpayAmount") Float mPostpayAmount,
			@Param("cPostpayTimes") Integer cPostpayTimes,
			@Param("cPostpayAmount") Float cPostpayAmount,
			@Param("discountTimes") Integer discountTimes,
			@Param("discountAmount") Float discountAmount,
			@Param("postpayFeeTimes") Integer postpayFeeTimes,
			@Param("prepayFeeTimes") Integer prepayFeeTimes,
			@Param("mPrepayTimes") Integer mPrepayTimes,
			@Param("mPrepayAmount") Float mPrepayAmount,
			@Param("cPrepayTimes") Integer cPrepayTimes,
			@Param("cPrepayAmount") Float cPrepayAmount,
			@Param("totalTimes") Integer totalTimes);

	ProjectOperateDay getProjectOperateStateDay(
			@Param("projectCode") String projectCode,
			@Param("bizDate") String bizDate);

	void updateProjectOperateDayState(
			@Param("id") String id,
			@Param("projectCode") String projectCode,
			@Param("bizDate") Date bizDate,
			@Param("mPostpayTimes") Integer mPostpayTimes,
			@Param("mPostpayAmount") Float mPostpayAmount,
			@Param("cPostpayTimes") Integer cPostpayTimes,
			@Param("cPostpayAmount") Float cPostpayAmount,
			@Param("discountTimes") Integer discountTimes,
			@Param("discountAmount") Float discountAmount,
			@Param("postpayFeeTimes") Integer postpayFeeTimes,
			@Param("prepayFeeTimes") Integer prepayFeeTimes,
			@Param("mPrepayTimes") Integer mPrepayTimes,
			@Param("mPrepayAmount") Float mPrepayAmount,
			@Param("cPrepayTimes") Integer cPrepayTimes,
			@Param("cPrepayAmount") Float cPrepayAmount,
			@Param("totalTimes") Integer totalTimes);
}
