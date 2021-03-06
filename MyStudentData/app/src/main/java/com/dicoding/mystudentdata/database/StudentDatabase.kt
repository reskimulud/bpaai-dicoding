package com.dicoding.mystudentdata.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.mystudentdata.helper.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, University::class, Course::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = StudentDatabase.MyAutoMigration::class)
                     ],
    exportSchema = true)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    @RenameColumn(tableName = "University", fromColumnName = "name", toColumnName = "universityName")
    class MyAutoMigration : AutoMigrationSpec

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context, applicationScope: CoroutineScope): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(object: Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                INSTANCE?.let { database ->
                                    applicationScope.launch {
                                        val studentDao = database.studentDao()
                                        studentDao.insertCourse(InitialDataSource.getCourses())
                                        studentDao.insertStudent(InitialDataSource.getStudents())
                                        studentDao.insertUniversity(InitialDataSource.getUniversities())
                                    }
                                }
                            }
                        })
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}