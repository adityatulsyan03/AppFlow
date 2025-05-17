package com.example.appflow.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appflow.data.repo.NoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncNotesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val noteRepository: NoteRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val email = inputData.getString("email") ?: return Result.failure()

        return try {
            withContext(Dispatchers.IO) {
                noteRepository.syncNotes(email)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}