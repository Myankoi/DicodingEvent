package com.example.dicodingevent.ui.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.workmanager.ReminderWorker

class CustomWorkerFactory(
    private val repository: EventRepository,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            ReminderWorker::class.java.name ->
                ReminderWorker(appContext, workerParameters, repository)
            else -> null
        }
    }
}