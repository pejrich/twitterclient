package com.codepath.apps.mytwitterclient;

import java.util.Date;

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DateFormatter {

  private static final int SECOND_MILLIS = 1000;
  private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
  private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
  private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


  public static String timeAgoInWords(Date date) {
    Date n = new Date(System.currentTimeMillis());
    // adapted from Google IO 2012
    long time = date.getTime();
    long now = System.currentTimeMillis();
    if (time <= 0) {
      time = 1;
    }
    long diff = now - time;
    boolean future = false;
    if (time > now) {
      diff = time - now;
      future = true;
    }

    if (diff < MINUTE_MILLIS) {
      return (future) ? "in a moment" : "just now";
    } else if (diff < 2 * MINUTE_MILLIS) {
      return (future) ? "in a minute" : "a minute ago";
    } else if (diff < 50 * MINUTE_MILLIS) {
      long mins = diff / MINUTE_MILLIS;
      return (future) ? "in " + mins + " minutes" : mins + " minutes ago";
    } else if (diff < 90 * MINUTE_MILLIS) {
      return (future) ? "in an hour" : "an hour ago";
    } else if (diff < 24 * HOUR_MILLIS) {
      long hours = diff / HOUR_MILLIS;
      return (future) ? "in " + hours + " hours" : hours + " hours ago";
    } else if (diff < 48 * HOUR_MILLIS) {
      return (future) ? "tomorrow" : "yesterday";
    } else {
      long days = diff / DAY_MILLIS;
      return (future) ? "in " + days + " days" : days + " days ago";
    }
  }
}
