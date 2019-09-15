package e.caioluis.testeinloco

import android.app.Application
import com.google.firebase.iid.FirebaseInstanceId
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.InLocoEngagementOptions
import com.inlocomedia.android.engagement.request.FirebasePushProvider

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val options = InLocoEngagementOptions.getInstance(this)

        options.applicationId = "48df73b4-1c67-4b4d-9ace-3c1c98aadd3f"
        options.isLogEnabled = true

        InLocoEngagement.init(this, options)

        val firebaseToken = FirebaseInstanceId.getInstance().token

        if (firebaseToken != null && !firebaseToken.isEmpty()) {
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