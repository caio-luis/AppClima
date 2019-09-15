package e.caioluis.testeinloco.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.PushMessage
import com.inlocomedia.android.engagement.request.FirebasePushProvider
import com.inlocomedia.android.engagement.request.PushProvider

class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data : Map <String, String>  = remoteMessage.data
        val pushContent : PushMessage = InLocoEngagement.decodeReceivedMessage(this, data)

        InLocoEngagement.presentNotification(
            this,
            pushContent,
            android.R.drawable.ic_notification_overlay,
            1111111
        )
    }

    override fun onNewToken(firebaseToken : String)
    {
        if (firebaseToken.isNotEmpty()) {
            val pushProvider : PushProvider = FirebasePushProvider.Builder()
                .setFirebaseToken(firebaseToken)
                .build()
            InLocoEngagement.setPushProvider(this, pushProvider)
        }
    }
}