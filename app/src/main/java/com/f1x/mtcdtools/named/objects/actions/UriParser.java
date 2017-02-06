package com.f1x.mtcdtools.named.objects.actions;

import android.net.Uri;

/**
 * Created by f1x on 2017-01-15.
 */

public class UriParser {
    public static Uri fromString(String uriString) {
        return Uri.parse(uriString);
    }
}
