package com.chwishay.chwsp00.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `.
//                  / \\||| : |||// \
//                / _||||| -:- |||||- \
//                  | | \\\ - /// | |
//                | \_| ''\---/'' | |
//                 \ .-\__ `-` ___/-. /
//              ______`. .' /--.--\ `. . __
//           ."" '< `.___\_<|>_/___.' >'"".
//          | | : `- \`.;`\ _ /`;.`/ - ` : | |
//            \ \ `-. \_ __\ /__ _/ .-` / /
//    ======`-.____`-.___\_____/___.-`____.-'======
//                       `=---='
//
//    .............................................
//             佛祖保佑             永无BUG
/**
 * author:RanQing
 * date:2020/8/19 0019 14:18
 * description:用户信息
 */
@Entity(tableName = "users_table")
data class UserInfo(
    @ColumnInfo(name = "_id") var id: Long = 0,
    //姓名
    var name: String? = null,
    //年龄
    var age: Byte = 0,
    //性别
    @ColumnInfo(name = "is_male") var isMale: Boolean = true,
    //病历号
    @PrimaryKey @ColumnInfo(name = "case_num") @NotNull var caseNum: String = "",
    //患侧
    @ColumnInfo(name = "affected_side") var affectedSide: String? = null,
    //护具类型
    @ColumnInfo(name = "protective_gear") var protectiveGear: Byte? = null,
    //其它护具
    @ColumnInfo(name = "other_pro") var otherPro: String? = null,
    //行走类型
    @ColumnInfo(name = "walk_type") var walkType: Int? = null,
    //其它行走类型
    @ColumnInfo(name = "other_walk_type") var otherWalkType: String? = null,
    //病种
    var diseases: String? = null
) {
}

@Entity(tableName = "record_files_table")
data class RecordFile(
    @ColumnInfo(name = "case_num") var caseNum: String? = "",
    @ColumnInfo(name = "create_time") var createTime: Long = 0,
    @ColumnInfo(name = "total_time_length") var totalTimeLength: Long = 0,
    @ColumnInfo(name = "analysis_start_time") var analysisStartTime: Long = 0,
    @ColumnInfo(name = "analysis_stop_time") var analysisStopTime: Long = 0
)

/**
 * 基础数据
 */
@Entity(tableName = "record_detail_table")
data class RecordDetailData @JvmOverloads constructor(
    @ColumnInfo(name = "case_num") var caseNum: String? = "",
    @ColumnInfo(name = "create_time") var createTime: Long = 0,

    //测试路程(m)
    var distance: Float = 0f,
    //测试时间(mm)
    var time: Int = 0,
    //总步数(步)
    @ColumnInfo(name = "step_count") var stepCount: Int = 0,
    //平均步速(m/s)
    @ColumnInfo(name = "average_speed") var averageSpeed: Float = 0f,

    //左步长(mm)
    @ColumnInfo(name = "left_step_length") var leftStepLength: Int = 0,
    //右步长(mm)
    @ColumnInfo(name = "right_step_length") var rightStepLength: Int = 0,
    //步幅(mm)
    @ColumnInfo(name = "step_stride") var stepStride: Int = 0,
    //步频(步/秒)
    @ColumnInfo(name = "step_cadence") var stepCadence: Float = 0f,
    //左侧摆动时间(s)
    @ColumnInfo(name = "left_swing_time") var leftSwingTime: Int = 0,
    //左侧摆动时间(s)
    @ColumnInfo(name = "right_swing_time") var rightSwingTime: Int = 0,
    //左侧支撑时间(s)
    @ColumnInfo(name = "left_brace_time") var leftBraceTime: Int = 0,
    //右侧支撑时间(s)
    @ColumnInfo(name = "right_brace_time") var rightBraceTime: Int = 0,
    //左侧支撑相、摆动相占比(%)
    @ColumnInfo(name = "left_brace_swing_ratio") var leftBraceSwingRatio: Int = 0,
    //右侧支撑相、摆动想占比(%)
    @ColumnInfo(name = "right_brace_swing_ratio") var rightBraceSwingRatio: Int = 0,
    //双支撑时间(s)
    @ColumnInfo(name = "double_brace_time") var doubleBraceTime: Int = 0,
    //步态周期(s)
    @ColumnInfo(name = "gait_cycle") var gaitCycle: Int = 0,

    //左侧支撑与右侧支撑时间比(%)
    @ColumnInfo(name = "lr_brace_time_ratio") var LRBraceTimeRatio: Int = 0,
    //左侧摆动与右侧摆动时间比(%)
    @ColumnInfo(name = "lr_swing_time_ratio") var LRSwingTimeRatio: Int = 0
)