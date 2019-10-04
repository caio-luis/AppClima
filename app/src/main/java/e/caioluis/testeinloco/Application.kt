package e.caioluis.testeinloco

import android.app.Application
import com.google.firebase.iid.FirebaseInstanceId
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.InLocoEngagementOptions
import com.inlocomedia.android.engagement.request.FirebasePushProvider

open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createInLocoIntegration()
    }

    private fun createInLocoIntegration() {
        val options = InLocoEngagementOptions.getInstance(this)

        options.applicationId = Constants.APPLICATION_ID
        options.isLogEnabled = true

        InLocoEngagement.init(this, options)

        val firebaseToken = FirebaseInstanceId.getInstance().token

        if (firebaseToken != null && firebaseToken.isNotEmpty()) {
            val pushProvider = FirebasePushProvider.Builder()
                .setFirebaseToken(firebaseToken)
                .build()
            InLocoEngagement.setPushProvider(this, pushProvider)
        }

        /*FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = token
                Log.d(TAG, msg)
            })*/
    }
}