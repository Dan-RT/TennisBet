package com.udes.daniel.tennisbet;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.udes.daniel.tennisbet.AsyncServerRequest.readStream;

public class NotificationManager {

    private CustomApplication application;
    private Context context;
    private int id_notif = 0;

    public NotificationManager (Context context, Application application) {
        this.context = context;
        this.application = (CustomApplication) application;
    }

    public void determineChanges(ArrayList<Match> PrevListMatchs, final ArrayList<Match> ListMatchs) {
        for (Match prevMatch:PrevListMatchs) {
            for (final Match match:ListMatchs) {
                if (prevMatch.getId() == match.getId()) {
                    int set = setWon(prevMatch.getPoints(), match.getPoints());
                    int contest = contestationDone(prevMatch.getContests(), match.getContests());

                    if (set > -1) {
                        //lancer notif set
                        if (set == 0) {
                            triggerSetNotification(match, match.getPlayer_1());
                        } else {
                            triggerSetNotification(match, match.getPlayer_2());
                        }
                    }

                    if (contest > -1) {
                        //lancer notif contestation
                        if (contest == 0) {
                            triggerContestNotification(match, match.getPlayer_1());
                        } else {
                            triggerContestNotification(match, match.getPlayer_2());
                        }
                    }

                    if (match.isOver()) {


                        new Thread() {
                            public void run() {

                                Bet bet = application.getBetMatch(match.getId());
                                if (bet != null && !bet.isNotificationTriggered()) {
                                    HttpURLConnection urlConnection = null;
                                    try {
                                        Log.i("INFO", "ON EST RENTRÉ DEDANS");
                                        URL url = new URL("http://10.0.2.2:3000/parties/resultat/" + match.getId() + "/" + bet.getPlayer() + "/" + bet.getAmount());
                                        urlConnection = (HttpURLConnection) url.openConnection(); // Open
                                        InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

                                        String result = readStream(in); // Read stream
                                        double gain = Double.valueOf(result);
                                        bet.setNotificationTriggered(true);

                                        if (gain > 0.0) {
                                            triggerWinnerNotification(match, gain);
                                        } else {
                                            triggerLoserNotification(match);
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (urlConnection != null)
                                            urlConnection.disconnect();
                                    }
                                }
                            }
                        }.start();
                        /*
                        Faire une requête GET envoyant
                            - id du match
                            - id du player
                            - montant misé
                        Renvoyant:
                            - somme gagné
                        */
                    }

                }
            }
        }
    }

    private int setWon (Points prevPoints, Points points) {
        //renvoie l'id du joueur si un set a été gagné

        ArrayList<Integer> prevSets = prevPoints.getSets();
        ArrayList<Integer> sets = points.getSets();

        if (!prevSets.get(0).equals(sets.get(0))) {
            return 0;
        }

        if (!prevSets.get(1).equals(sets.get(1))) {
            return 1;
        }

        return -1;
    }

    private int contestationDone(ArrayList<Integer> prevContests, ArrayList<Integer> contests) {
        //renvoie l'id du joueur si un set a été gagné

        if (!prevContests.get(0).equals(contests.get(0))) {
            return 0;
        }

        if (!contests.get(0).equals(contests.get(0))) {
            return 1;
        }

        return -1;
    }

    private void triggerContestNotification (Match match, Player playerContesting) {
        triggerNotification(match,"Contest by " + playerContesting.toString());
    }

    public void triggerSetNotification (Match match, Player playerContesting) {
        try {
            triggerNotification(match,"Set won by " + playerContesting.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void triggerWinnerNotification (Match match, double gain) {
        try {
            triggerNotification(match,"YOU HAVE WON " + gain + "$");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void triggerLoserNotification (Match match) {
        try {
            triggerNotification(match,"YOU ARE A LOSER, YOU HAVE WON NOTHING");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void triggerNotification (Match match, String text) {
        //https://developer.android.com/training/notify-user/build-notification#java

        Intent intent = new Intent(context, MatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id_match", (Integer) match.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, null)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tennis Match Update")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(this.id_notif, mBuilder.build());
        this.id_notif++;
    }

}
