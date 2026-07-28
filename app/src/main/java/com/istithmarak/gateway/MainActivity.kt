
package com.istithmarak.gateway

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val title = TextView(this).apply {
            text = "Istithmarak GSM Gateway"
            textSize = 22f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val statusText = TextView(this).apply {
            text = "النظام يعمل بكفاءة وجاهز للمزامنة مع السنترال."
            textSize = 16f
            setPadding(0, 30, 0, 50)
        }

        val btnDefaultDialer = Button(this).apply {
            text = "تعيين كبرنامج اتصال افتراضي"
            setOnClickListener { requestDefaultDialer() }
        }

        layout.addView(title)
        layout.addView(statusText)
        layout.addView(btnDefaultDialer)
        setContentView(layout)
        
        checkAndRequestDefaultDialer()
    }

    private fun checkAndRequestDefaultDialer() {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (packageName != telecomManager.defaultDialerPackage) {
            requestDefaultDialer()
        }
    }

    private fun requestDefaultDialer() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    startActivityForResult(intent, 101)
                }
            }
        } else {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            }
            startActivity(intent)
        }
    }
}
