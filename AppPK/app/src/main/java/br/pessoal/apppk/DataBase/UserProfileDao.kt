package br.pessoal.apppk.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserProfile?

    @Query("SELECT * FROM user_profiles")
    suspend fun getAllUsers(): List<UserProfile>

    @Query("DELETE FROM user_profiles WHERE username = :username")
    suspend fun deleteUserByUsername(email: String)


}
