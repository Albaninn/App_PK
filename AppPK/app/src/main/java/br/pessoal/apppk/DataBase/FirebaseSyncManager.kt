package br.pessoal.apppk.DataBase

import br.pessoal.apppk.database.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseSyncManager {

    suspend fun syncFirestoreToRoom(db: AppDatabase) {
        val firestore = FirebaseFirestore.getInstance()

        val userProfilesSnapshot = firestore.collection("user_profiles").get().await()

        val userProfiles = userProfilesSnapshot.documents.mapNotNull { document ->
            val username = document.getString("username") ?: return@mapNotNull null
            val fullName = document.getString("fullName") ?: return@mapNotNull null
            val password = document.getString("password") ?: return@mapNotNull null
            val email = document.getString("email") ?: return@mapNotNull null
            val accessLevel = document.getString("accessLevel") ?: return@mapNotNull null

            UserProfile(username, fullName, password, email, accessLevel)
        }

        userProfiles.forEach { userProfile ->
            db.userProfileDao().insertUser(userProfile)
        }
    }

    suspend fun syncRoomToFirestore(db: AppDatabase) {
        val firestore = FirebaseFirestore.getInstance()

        val userProfiles = db.userProfileDao().getAllUsers()

        userProfiles.forEach { userProfile ->
            firestore.collection("user_profiles")
                .document(userProfile.username)
                .set(userProfile)
                .await()
        }
    }
}
