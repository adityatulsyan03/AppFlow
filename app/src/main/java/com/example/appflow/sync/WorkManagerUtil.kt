package com.example.appflow.sync

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkManagerUtil {

    fun scheduleNoteSync(context: Context, email: String) {
        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("email", email)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncNotesWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "NoteSyncWorker_$email",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}