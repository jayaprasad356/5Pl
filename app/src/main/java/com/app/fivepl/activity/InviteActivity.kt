package com.app.fivepl.activity

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.app.fivepl.databinding.ActivityInviteBinding
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase

class InviteActivity : AppCompatActivity() {

    lateinit var binding: ActivityInviteBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInviteBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }


        val refferalCode = session.getData(com.app.fivepl.helper.Constant.REFER_CODE)
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://pocketfarm.in/" + refferalCode)
            domainUriPrefix = "https://pocketfarm.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
        }

        val dynamicLinkUri = dynamicLink.uri



        with(binding) {


            ivQrcode
                .setImageBitmap(
                    QrCodeDrawable({ dynamicLinkUri.toString() })
                        .toBitmap(1024, 1024),
                )
//
//            editInput.addTextChangedListener {
//                val text = editInput.text.toString()

//                ivQrcode.setImageDrawable(
//                    QrCodeDrawable( { text }),
//                ) }

    }



        binding.tvCopy.text = session.getData(com.app.fivepl.helper.Constant.REFER_CODE)

        binding.rlCopy.setOnClickListener {
            val clipboard = android.content.Context.CLIPBOARD_SERVICE
            val clip = android.content.ClipData.newPlainText("label", session.getData(com.app.fivepl.helper.Constant.REFER_CODE))
            val clipboardManager = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboardManager.setPrimaryClip(clip)
            android.widget.Toast.makeText(activity, "Copied", android.widget.Toast.LENGTH_SHORT).show()
        }

        binding.btnInvite.setOnClickListener {

            val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
                longLink = dynamicLinkUri
            }.addOnSuccessListener { (shortLink, flowChartLink) ->
                // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
                // com.google.firebase.dynamiclinks.ktx.component2

                val shareBody = "" + shortLink
                val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))
            }.addOnFailureListener {
                // Error
                Log.d("Error", it.message.toString())
                // ...
            }
        }

        return setContentView(binding!!.root)
    }
}