package com.tealer.messengerdatatransfer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tealer.process.MyConstants
import com.tealer.messengerdatatransfer.databinding.ActivityClientMessengerBinding

class ClientMessengerActivity : AppCompatActivity() {

    val TAG: String = "ClientMessengerActivity"
    var mServcie: Messenger? = null
    var isConn: Boolean = false

    var mA: Int = 1

    var mMessenger: Messenger = Messenger(object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MyConstants.MSG_SUM -> {

                    var textView=binding.llContainer.findViewById<TextView>(msg.arg1)
                    textView.text="${textView.text} => ${msg.arg2}"

                }
                MyConstants.MSG_FROM_SERVICE -> {
                    var data = msg.data
                    var msgContent = data.getString("reply")
                    Log.v(
                        TAG,
                        "client received msg:" + msgContent
                    )
                }
                else -> {
                    super.handleMessage(msg)
                }
            }


        }
    })


    private var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            mServcie = Messenger(service)
            var msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT)
            var data = Bundle().apply {
                putString("msg", "hello this is a client");
            }
            msg.data = data
            msg.replyTo = mMessenger
            isConn = true
            try {
                mServcie?.send(msg)
            } catch (e: RemoteException) {

            }

            binding.txtState.text = "connected"
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mServcie = null
            isConn = false
            binding.txtState.text = "disconnected"
        }

    }

    private lateinit var binding: ActivityClientMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //开始绑定服务
        bindServiceInvoked()
        binding.btnConnect.setOnClickListener {
            bindServiceInvoked()
        }

        binding.btnSend.setOnClickListener {
            try {
                var a = mA++
                var b = (Math.random() * 100).toInt()
                var textView=TextView(this@ClientMessengerActivity).apply {
                    text="$a + $b = caculating ..."
                    id=a
                }
                binding.llContainer.addView(textView)

                var msgFromClient=Message.obtain(null, MyConstants.MSG_SUM,a,b)
                msgFromClient.replyTo=mMessenger

                if(isConn){
                    //往服务端发送消息
                    mServcie?.send(msgFromClient)
                }
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    /**
     * 开始绑定服务
     */
    private fun bindServiceInvoked() {
        var intent = Intent("com.tealer.process.messengerservice")
        intent.`package`="com.tealer.simple_service"
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }
}