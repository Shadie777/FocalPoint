package com.seankeating.focalpointModel;

/**
 * Created by Sean on 02/04/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;

    public class SessionManager {
        // Shared Preferences
        SharedPreferences pref;

        // Context
        Context context;

        // Sharedpref file name
        private static final String PREFS_NAME = "session";

        // All Shared Preferences Keys
        private static final String IS_LOGIN = "IsLoggedIn";

        public SessionManager(Context context){
            this.context = context;
            pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }


        public void storeSession(){
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(IS_LOGIN, (boolean) true);
            editor.commit();
        }

        // Clear session details
        public void clearSession(){
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        }


// Get Login State
        public boolean isLoggedIn(){
            return pref.getBoolean(IS_LOGIN, false);
        }
}
