package com.example.tickerwatchlist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {
    //Geniuenly wasn't as bad as I thought, but definitely needed that extra class period & day to work on it.
    private static final Pattern PATTERN =
            Pattern.compile("Ticker:\\s*<<\\s*([A-Za-z]+)\\s*>>");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) return;

        var msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        StringBuilder body = new StringBuilder();
        for (var m : msgs) {
            if (m != null && m.getMessageBody() != null) body.append(m.getMessageBody()).append(' ');
        }
        String text = body.toString();

        Matcher matcher = PATTERN.matcher(text);
        boolean hasPattern = matcher.find();
        String upper = null;
        boolean valid = false;

        if (hasPattern) {
            String raw = matcher.group(1);
            upper = raw.toUpperCase();
            valid = upper.matches("^[A-Z]+$");
        }

        Intent launch = new Intent(context, com.example.tickerwatchlist.MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (!hasPattern) {
            launch.putExtra("sms_result", "nope :/");
        } else if (!valid) {
            launch.putExtra("sms_result", "can't do that");
            launch.putExtra("sms_ticker", upper);
        } else {
            launch.putExtra("sms_result", "yea!");
            launch.putExtra("sms_ticker", upper);
        }

        context.startActivity(launch);
    }
}
