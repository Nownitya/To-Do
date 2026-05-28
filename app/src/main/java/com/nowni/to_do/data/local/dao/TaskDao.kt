package com.nowni.to_do.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nowni.to_do.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("""SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR DESCRIPTION LIKE '%' || :searchQuery || '%' ORDER BY isCompleted ASC, id DESC """)
    fun getTaskByCreatedDate(searchQuery: String): Flow<List<TaskEntity>>

    @Query("""SELECT * FROM tasks WHERE title LIKE'%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY isCompleted ASC, dueDate ASC """)
    fun getTaskByDueDate(searchQuery: String): Flow<List<TaskEntity>>

    @Query("""SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' ||:searchQuery || '%' ORDER BY isCompleted ASC, CASE WHEN priority = 'HIGH' THEN 1 When priority = 'MEDIUM' THEN 2 When priority = 'LOW' THEN 3 End """)
    fun getTasksByPriority(searchQuery: String): Flow<List<TaskEntity>>

    @Query("""SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY isCompleted ASC, title COLLATE NOCASE ASC """)
    fun getTasksByTitle(searchQuery: String): Flow<List<TaskEntity>>

    @Query("""SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY isCompleted ASC, reminderDateTime ASC """)
    fun getTasksByReminderDate(searchQuery: String): Flow<List<TaskEntity>>

    /*@Query("SELECT * FROM tasks ORDER BY isCompleted ASC, id DESC")
    fun getTasks(): Flow<List<TaskEntity>>*/

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: TaskEntity): Long

    @Update
    suspend fun updateTask(tasks: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}