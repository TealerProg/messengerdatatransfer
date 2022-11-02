package com.tealer.process.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ServciesMessengerServcie extends Service {

    private static final  String TAG="ServciesMessengerSerice";

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msgFromClient) {

            switch (msgFromClient.what){
                case MyConstants.MSG_FROM_CLIENT:
                    Log.v(TAG,"receive msg from Client:"+msgFromClient.getData().getString("msg"));
                    Messenger client=msgFromClient.replyTo;
                    Message replyMessage=Message.obtain(null,MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle=new Bundle();
                    bundle.putString("reply","you message has been received，I will reply you later");
                    replyMessage.setData(bundle);

                    try{
                        client.send(replyMessage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                case MyConstants.MSG_SUM:

                    try{
                     //模拟耗时

                        for (int i=0;i<10;i++){
                            Message msgToClient=Message.obtain(msgFromClient);
                            msgToClient.what=MyConstants.MSG_SUM;
                            msgToClient.arg2=msgFromClient.arg1+ msgFromClient.arg2+i;
                            msgFromClient.replyTo.send(msgToClient);
                            Thread.sleep(2000);
                        }


                    }catch (InterruptedException | RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msgFromClient);
            }



        }
    }


    private  final Messenger mMessenger=new Messenger(new MessengerHandler());



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
