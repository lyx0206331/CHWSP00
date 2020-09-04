package com.chwishay.chwsp00.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chwishay.chwsp00.model.RecordDetailData
import com.chwishay.chwsp00.model.RecordFile
import com.chwishay.chwsp00.model.UserInfo

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
 * date:2020/9/2 0002 15:32
 * description:
 */
@Database(entities = [UserInfo::class, RecordFile::class, RecordDetailData::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun recordFileDao(): RecordFileDao
    abstract fun recordDetailDao(): RecordDetailDao

    companion object {
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, "chws_p00_db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        val request =OneTimeWork
                    }
                }).build()
        }
    }
}

@Dao
abstract class UserInfoDao {

    @Query("select * from users_table")
    abstract fun queryAll(): List<UserInfo>

    @Query("select * from users_table where case_num = :caseNum")
    abstract fun queryByCaseNum(caseNum: String): UserInfo?

    fun queryByCaseNumWithFiles(caseNum: String): UserInfo? {
        return queryByCaseNum(caseNum)?.apply {
//            recordFiles = AppDataBase.getInstance()
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg userInfo: UserInfo): LongArray

    @Update
    abstract fun update(userInfo: UserInfo)

    @Delete
    abstract fun delete(vararg userInfo: UserInfo): Int

    @Query("delete from users_table")
    abstract fun deleteAll()
}

@Dao
abstract class RecordFileDao {

    @Query("select * from record_files_table")
    abstract fun queryAll(): List<RecordFile>

    @Query("select * from record_files_table where case_num = :caseNum")
    abstract fun queryByCaseNum(caseNum: String): List<RecordFile>?

    @Query("select * from record_files_table where create_time = :createTime")
    abstract fun queryByCreateTime(createTime: Long): RecordFile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg recordFile: RecordFile): LongArray

    @Update
    abstract fun update(recordFile: RecordFile)

    @Delete
    abstract fun delete(vararg recordFile: RecordFile): Int

    @Query("delete from record_files_table")
    abstract fun deleteAll()
}

@Dao
abstract class RecordDetailDao {

    @Query("select * from record_detail_table")
    abstract fun queryAll(): List<RecordDetailData>

    @Query("select * from record_detail_table where create_time = :createTime")
    abstract fun queryByCreateTime(createTime: Long): RecordDetailData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg recordDetailData: RecordDetailData): LongArray

    @Update
    abstract fun update(recordDetailData: RecordDetailData)

    @Delete
    abstract fun delete(vararg recordDetailData: RecordDetailData): Int

    @Query("delete from record_detail_table")
    abstract fun deleteAll()
}