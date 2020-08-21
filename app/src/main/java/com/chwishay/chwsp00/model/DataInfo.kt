package com.chwishay.chwsp00.model

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
class UserInfo @JvmOverloads constructor(
    val id: Int,
    //姓名
    name: String,
    //性别
    age: Int,
    //年龄
    sex: Byte,
    //病历号
    caseNum: String,
    //患侧
    affectedSide: Byte,
    //护具类型
    protectiveGear: Int? = null,
    //其它护具
    otherPro: String? = null,
    //行走类型
    walkType: Int? = null,
    //其它行走类型
    otherWalkType: String? = null,
    //病种
    diseases: String
) {
}

/**
 * 基础数据
 */
class BaseData @JvmOverloads constructor(
    //测试路程(m)
    var distance: Float,
    //测试时间(mm)
    var time: Int,
    //总步数(步)
    var stepCount: Int,
    //平均步速(m/s)
    var averageSpeed: Float
)

/**
 * 空间参数
 */
class SpaceParams @JvmOverloads constructor(
    //左步长(mm)
    var leftStepLength: Int,
    //右步长(mm)
    var rightStepLength: Int,
    //步幅(mm)
    var StepStride: Int,
    //步频(步/秒)
    var stepCadence: Float,
    //左侧摆动时间(s)
    var leftSwingTime: Int,
    //左侧摆动时间(s)
    var rightSwingTime: Int,
    //左侧支撑时间(s)
    var leftBraceTime: Int,
    //右侧支撑时间(s)
    var rightBraceTime: Int,
    //左侧支撑相、摆动相占比(%)
    var leftBraceSwingRatio: Int,
    //右侧支撑相、摆动想占比(%)
    var rightBraceSwingRatio: Int,
    //双支撑时间(s)
    var doubleBraceTime: Int,
    //步态周期(s)
    var gaitCycle: Int
)

/**
 * 对称性
 */
class Symmetry @JvmOverloads constructor(
    //左侧支撑与右侧支撑时间比(%)
    var LRBraceTimeRatio: Int,
    //左侧摆动与右侧摆动时间比(%)
    var LRSwingTimeRatio: Int
)