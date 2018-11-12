package com.telecom.sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SMSFragment extends Fragment {
    private static final String TAG = "SMSFragment";
    private static final String SMS_STATUS = "com.telecom.sms.SMS_STATUS";
    SmsManager mSmsManager;
    EditText mSmsContentEditText;
    EditText mPhoneNumEditText;
    TextView mSmsStatusTextView;
    TextView mSmsReceivedTextView;
    TextView mSmsInboxTextView;
    Button mSendSmsButton;
    Button mSmsInboxButton;
    BroadcastReceiver mSmsStatusReceiver;
    BroadcastReceiver mSmsReceivedReceiver;

    public static SMSFragment newInstance() {
        return new SMSFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSmsManager = SmsManager.getDefault();
        /*if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setLogo(R.drawable.sms_32);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/

        mSmsStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                mSmsStatusTextView.setText(intent.getStringExtra("msg"));
            }
        };
        mSmsReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                IntentUtils.printIntentInfo(intent);
                if (intent != null && intent.getAction() != null) {
                    Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
                    SmsMessage[] messages = new SmsMessage[pduArray.length];
                    for (int i = 0; i < pduArray.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                        mSmsReceivedTextView.append(messages[i].getOriginatingAddress());
                        mSmsReceivedTextView.append(" : ");
                        mSmsReceivedTextView.append(messages[i].getMessageBody());
                        mSmsReceivedTextView.append("\r\n");
                    }
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sms, container, false);
        mSmsContentEditText = v.findViewById(R.id.sms_content_edit_text);
        mPhoneNumEditText = v.findViewById(R.id.phone_num_edit_text);
        mSmsStatusTextView = v.findViewById(R.id.sms_status_text_view);
        mSmsReceivedTextView = v.findViewById(R.id.sms_received_text_view);
        mSmsInboxTextView = v.findViewById(R.id.sms_inbox_text_view);
        mSendSmsButton = v.findViewById(R.id.send_sms_button);
        mSendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sIntent = new Intent();
                sIntent.setAction(SMS_STATUS);
                sIntent.putExtra("msg", "短信已发送!");
                PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(), 1, sIntent, 0);

                Intent dIntent = new Intent();
                dIntent.setAction(SMS_STATUS);
                dIntent.putExtra("msg", "对方已接收!");
                PendingIntent deliveryIntent = PendingIntent.getBroadcast(getActivity(), 2, dIntent, 0);
                mSmsManager.sendTextMessage(
                        mPhoneNumEditText.getText().toString(),
                        null,
                        mSmsContentEditText.getText().toString(),
                        sentIntent,
                        deliveryIntent);
                mSmsStatusTextView.setText("指令已经发出!");

            }
        });
        mSmsInboxButton = v.findViewById(R.id.sms_inbox_button);
        mSmsInboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmsInboxTextView.setText("");
                Uri SMS_INBOX = Uri.parse("content://sms/inbox");
                Cursor c = getActivity().getContentResolver().query(SMS_INBOX, null, null, null, null);
                if (c != null) {
                    /*for(int i=0;i<c.getColumnCount();i++){
                        System.out.println(i+"="+c.getColumnName(i));
                    }*/
                    while (c.moveToNext()) {
                        mSmsInboxTextView.append(c.getString(2)+":"+c.getString(13)+"\r\n");
                    }
                    c.close();
                }
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter smsStatusIntentFilter = new IntentFilter(SMS_STATUS);
        getActivity().registerReceiver(mSmsStatusReceiver, smsStatusIntentFilter);
        IntentFilter smsReceivedIntentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        getActivity().registerReceiver(mSmsReceivedReceiver, smsReceivedIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSmsStatusReceiver != null)
            getActivity().unregisterReceiver(mSmsStatusReceiver);
        if (mSmsReceivedReceiver != null)
            getActivity().unregisterReceiver(mSmsReceivedReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
