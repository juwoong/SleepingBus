package kr.tamiflus.sleepingbus.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Date;

/**
 * Created by tamiflus on 16. 8. 22..
 */
public class Utils {

    public static final String hash = "4ad6103beb2cf3195633d491c5357cd4";
    /**
     * Format the given date relatively to now.
     *
     * @param date Date
     * @return formatted date
     */
    public static String formatRelative(Date date) {
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS).toString();
    }

    /**
     * Convert dp to px
     *
     * @param context
     * @param dp
     * @return converted value
     */
    public static float dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * Mix Two Colors.
     *
     * @param color1
     * @param color2
     * @param amount
     * @return
     */
    public static int mixTwoColors(int color1, int color2, float amount) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - amount;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }

    public static void sendNotification(Context context, String title, String message,
                                        int color,
                                        int iconResId,
                                        String type,
                                        Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconResId)
                .setColor(context.getResources().getColor(color))
                .setContentTitle(title)
                .setTicker(message)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .addAction(0, button, pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(type, 0, notificationBuilder.build());
    }

}
