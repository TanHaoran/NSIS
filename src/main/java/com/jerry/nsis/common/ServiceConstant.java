package com.jerry.nsis.common;

/**
 * Created by Jerry on 2016/3/2.
 */
public class ServiceConstant {

    public static String SERVICE_IP = "http://192.168.0.100";

    public static final String SERVICE = ":4514/NSIS_WEBAPI/";

    public static final String VIDEO_SERVICE = ":4516/NSIS_WebUser/";

    public static final String CONTACT_SERVICE = ":4517/NSIS_WebNotify/";

    public static final String SCHEDULE_SERVICE = ":4518/NSIS_WEBOrderClass/";

    /**
     * 获取当前医院名称
     */
    public static final String GET_HOSPITAL_NAME = "GetHospitalName";

    /**
     * 获取二维码信息
     */
    public static final String GET_CHECK_CODE = "GetCheckCode";

    /**
     * 获取所有科室
     */
    public static final String FIND_OFFICE_ALL = "FindOfficeAll";

    /**
     * 检验激活码是否正确
     */
    public static final String VERITY_CHECK_CODE = "VerityCheckCode";

    /**
     * 获取护理项目信息
     */
    public static final String FIND_NURSE_ITEM = "FindNurseItem";

    /**
     * 获取护理项目明细
     */
    public static final String FIND_NURSE_RECORD_BY_OFFICE_ID = "FindNurseRecordByOfficeID";

    /**
     * 获取护士教育视频信息
     */
    public static final String FIND_NURSING_EDUCATION_BY_VIDEO = "FindNursingEducationByVideo";

    /**
     * 获取护士教育PPT信息
     */
    public static final String FIND_NURSING_EDUCATION_BY_FILE = "FindNursingEducationByFile";

    /**
     * 获取护士教育文件内容
     */
    public static final String GET_NURSING_EDUCATION_BY_ID = "GetNursingEducationById";

    /**
     * 获取床位一览信息
     */
    public static final String FIND_BED_FILE_EXIST_EMPTY_ALL = "FindBedFileExistEmptyAll";

    /**
     * 保存责任分组信息
     */
    public static final String SAVE_WEB_RESPONSIBILITY_GROUP = "SaveWebResponsibilityGroup";

    /**
     * 获取所有病人花费
     */
    public static final String FIND_PATIENT_INFO_SPEND_BY_OFFICE_ID = "FindPatientInfoSpendByOfficeid";

    /**
     * 查询病人花费信息
     */
    public static final String QUERY_PATIENT_MONEY = "QueryPatientMoney";

    /**
     * 查询病人医嘱
     */
    public static final String QUERY_MEDORD_BYINHOSID = "QueryMedordByInhosId";

    /**
     * 获取该科室下当天值班的医生信息
     */
    public static final String FIND_DOCTOR_SCHEDULING_BY_OFFICE_ID = "FindDoctorSchedulingByOfficeId";

    /**
     * 获取出院病人信息
     */
    public static final String FIND_PATIENT_INFO_BY_INHOS_ID_LEAVE_HOSPITAL = "FindPatientInfoByInhosIdLeaveHospital";

    /**
     * 更新该科室下当天值班的医生信息
     */
    public static final String MODIFY_DOCTOR_SCHEDULING_VALUE = "ModifyDoctorSchedulingValue";

    /**
     * 获取体温信息
     */
    public static final String FIND_CONDITIONS_ITEM_SET_BY_ITEM_ID = "FindConditionsItemSetByItemId";

    /**
     * 获取体温详细信息
     */
    public static final String Find_Records_By_Office_ID = "FindRecordsByOfficeID";

    /**
     * 获取所有病人信息
     */
    public static final String FIND_PATIENT_INFO_BY_OFFICE = "FindPatientInfoByOffice";

    /**
     * 获取所有医生信息
     */
    public static final String FIND_DOCTOR_BY_ALL = "FindDoctorByAll";

    /**
     * 获取备注信息
     */
    public static final String FIND_NURSING_DETAIL_BY_ITEM_ID = "FindNursingDetailByItemID";

    /**
     * 获取换床信息
     */
    public static final String FIND_TRANS_BED_RECORD_BY_TODAY = "FindTransBedRecordByToDay";

    /**
     * 新增换床信息
     */
    public static final String INSERT_TRAN_BED_RECORD = "InsertTranBedRecord";

    /**
     * 删除换床信息
     */
    public static final String DELETE_TRAN_BED_RECORD = "DeleteTranBedRecord";

    /**
     * 获取视频URL地址
     */
    public static final String GET_MOVE_ADDRESS = "GetMoveAddress";

    /**
     * 获取联系人信息
     */
    public static final String FIND_TELEPHONE_BY_OFFICEI_D = "FindTelephoneByOfficeID";

    /**
     * 获取所有便签
     */
    public static final String FIND_MEMORANDUM = "FindMemorandum";

    /**
     * 新增便签
     */
    public static final String INSERT_MEMORANDUM = "InsertMemorandum";

    /**
     * 编辑便签
     */
    public static final String UPDATE_MEMORANDUM_INFO = "UpdateMemorandumInfo";

    /**
     * 新增便签
     */
    public static final String DELETE_MEMORANDUM = "DeleteMemorandum";

    /**
     * 读取所有班次信息
     */
    public static final String FIND_ALL_SCHE_TYPE_BY_OFFICE_ID = "FindAllScheTypeByOfficeID";

    /**
     * 读取所有护士信息
     */
    public static final String FIND_SCHEDULING_NURSE_BY_OFFICE_ID = "FindSchedulingNurseByOfficeID";


    /**
     * 读取护士一周的排班
     */
    public static final String FIND_SCHDULING_WEEK = "FindSchdulingWeek";


    /**
     * 读取一天排班信息
     */
    public static final String FIND_SCHEDILING_INFO_RECORD_BY_DATE_OFFICE_ID = "FindSchedilingInfoRecordByDateOfficeID";

    /**
     * 读取责任分组
     */
    public static final String FIND_WEB_RESPONSIBILITY_GROUP_BY_OFFICE_ID = "FindWebResponsibilityGroupByOfficeId";

    /**
     * 读取护士一周排班信息
     */
    public static final String GET_ORDER_CLASS_TABLE = "GetOrderClassTable";

    /**
     * 读取护士一周排班标题
     */
    public static final String GET_ORDER_CLASS_COLUMN = "GetOrderClassColumn";

    /**
     * 读取一个护士的管床信息
     */
    public static final String GET_SCHE_BED_FILE_ALL = "GetScheBedFileAll";


    /**
     * 保存护理项目
     */
    public static final String SAVE_NURSE_ITEM = "SaveNurseItem";


    /**
     * 删除护理项目
     */
    public static final String DELETE_ITEM_RECORD_INFO = "DeleteItemRecordInfo";

    /**
     * 查询所有床位（含状态）
     */
    public static final String FIND_BED_FILE_ALL_BY_OFFICE_ID = "FindBedFileAllByOfficeID";

    /**
     * 获取所有护士一周排班备注信息
     */
    public static final String FIND_SCHEDULING_INFO_BY_REMARK = "FindSchedulingInfoByRemark";

    /**
     * 点赞
     */
    public static final String MODIFY_NURSING_EDUCATION_BY_PRAISE_NUMBER = "ModifyNursingEducationByPraiseNumber";

    /**
     * 点赞
     */
    public static final String MODIFY_NURSING_EDUCATION_BY_BAD_REVIEW_NUMBER = "ModifyNursingEducationByBadReviewNumber";


    /**
     * 获取通知消息
     */
    public static final String FIND_NOTICE_BY_OFFICE = "FindNoticeByOffice";


}
