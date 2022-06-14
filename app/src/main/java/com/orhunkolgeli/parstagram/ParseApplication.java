package com.orhunkolgeli.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("mvMUADchHWi66NbHBC2IvI156NUFjJMpKUB8JLBE")
                .clientKey("V5URBkRg9Y8LrZf17WnQgd8dau2UlNlbIqeu5yQA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
