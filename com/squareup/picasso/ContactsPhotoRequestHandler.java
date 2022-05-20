package com.squareup.picasso;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import java.io.IOException;
import java.io.InputStream;

class ContactsPhotoRequestHandler extends RequestHandler {
  private static final int ID_CONTACT = 3;
  
  private static final int ID_DISPLAY_PHOTO = 4;
  
  private static final int ID_LOOKUP = 1;
  
  private static final int ID_THUMBNAIL = 2;
  
  private static final UriMatcher matcher = new UriMatcher(-1);
  
  private final Context context;
  
  static {
    matcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
    matcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
    matcher.addURI("com.android.contacts", "contacts/#/photo", 2);
    matcher.addURI("com.android.contacts", "contacts/#", 3);
    matcher.addURI("com.android.contacts", "display_photo/#", 4);
  }
  
  ContactsPhotoRequestHandler(Context paramContext) {
    this.context = paramContext;
  }
  
  private InputStream getInputStream(Request paramRequest) throws IOException {
    ContentResolver contentResolver = this.context.getContentResolver();
    Uri uri2 = paramRequest.uri;
    Uri uri1 = uri2;
    switch (matcher.match(uri2)) {
      default:
        throw new IllegalStateException("Invalid uri: " + uri2);
      case 1:
        uri2 = ContactsContract.Contacts.lookupContact(contentResolver, uri2);
        uri1 = uri2;
        if (uri2 == null)
          return null; 
      case 3:
        return (Build.VERSION.SDK_INT < 14) ? ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri1) : ContactPhotoStreamIcs.get(contentResolver, uri1);
      case 2:
      case 4:
        break;
    } 
    return contentResolver.openInputStream(uri2);
  }
  
  public boolean canHandleRequest(Request paramRequest) {
    Uri uri = paramRequest.uri;
    return ("content".equals(uri.getScheme()) && ContactsContract.Contacts.CONTENT_URI.getHost().equals(uri.getHost()) && matcher.match(paramRequest.uri) != -1);
  }
  
  public RequestHandler.Result load(Request paramRequest, int paramInt) throws IOException {
    null = getInputStream(paramRequest);
    return (null != null) ? new RequestHandler.Result(null, Picasso.LoadedFrom.DISK) : null;
  }
  
  @TargetApi(14)
  private static class ContactPhotoStreamIcs {
    static InputStream get(ContentResolver param1ContentResolver, Uri param1Uri) {
      return ContactsContract.Contacts.openContactPhotoInputStream(param1ContentResolver, param1Uri, true);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/ContactsPhotoRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */