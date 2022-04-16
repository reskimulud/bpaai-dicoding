package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentAndUniversity
import com.dicoding.mystudentdata.database.StudentDao
import com.dicoding.mystudentdata.helper.SortType
import com.dicoding.mystudentdata.helper.SortUtils

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>>{
        val query = SortUtils.getSortedQuery(sortType)
        val student = studentDao.getAllStudent(query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPageSize(10)
            .build()

        return LivePagedListBuilder(student, config).build()
    }
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()

//    suspend fun insertAllData() {
//        studentDao.insertStudent(InitialDataSource.getStudents())
//        studentDao.insertUniversity(InitialDataSource.getUniversities())
//        studentDao.insertCourse(InitialDataSource.getCourses())
//    }
}