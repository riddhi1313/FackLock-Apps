package ccc.messeningLock;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class CheckMissCall {
	Context c;
	String name, number, caller_number;
	static String body;
	static boolean view_id;

	CheckMissCall(Context c) {
		this.c = c;
	}

	int getMissedCall() {
		int no_Missed = 0;
		String[] strFields = { android.provider.CallLog.Calls.CACHED_NAME,
				android.provider.CallLog.Calls.NUMBER,
				android.provider.CallLog.Calls.TYPE };
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
				+ " AND " + CallLog.Calls.NEW + "=1";

		Cursor mCallCursor = c.getContentResolver().query(
				android.provider.CallLog.Calls.CONTENT_URI, strFields, where,
				null, strOrder);

		if (mCallCursor.moveToFirst()) {

			do {

				boolean missed = mCallCursor.getInt(mCallCursor
						.getColumnIndex(CallLog.Calls.TYPE)) == CallLog.Calls.MISSED_TYPE;

				if (missed) {
					no_Missed++;
					name = mCallCursor.getString(mCallCursor
							.getColumnIndex(CallLog.Calls.CACHED_NAME));
					// Log.d("tab", "Name: " + name);
					caller_number = mCallCursor.getString(mCallCursor
							.getColumnIndex(CallLog.Calls.NUMBER));
					// Log.d("tab", "number " + number);

				}

			} while (mCallCursor.moveToNext());

		}
		return no_Missed;
	}

	int getUnreadSms() {

		final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
		ContentResolver resolver = c.getContentResolver();
		Cursor c = resolver.query(SMS_INBOX, null, "read = 0", null, null);
		int unreadMessagesCount = c.getCount();

		c.deactivate();
		return unreadMessagesCount;
	}

	String getSenderName() {
		Uri uriSMSURI = Uri.parse("content://sms/inbox");
		Cursor cur = c.getContentResolver().query(uriSMSURI, null, "read = 0",
				null, null);
		String sms = "";
		while (cur.moveToNext()) {
			number = cur.getString(2);
			body = cur.getString(cur.getColumnIndexOrThrow("body")).toString();
			if (!number.equalsIgnoreCase("")) {

				sms = getContactName(number);
			}

			break;
		}
		Log.d("main", "sms:" + sms);
		if (sms == null) {
			sms = number;
		}
		return sms;

	}

	public String getContactName(String phoneNumber) {
		ContentResolver cr = c.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}

	String getName() {

		if (name == null) {
			return "" + caller_number;
		} else {
			return "" + name;
		}

	}

	String getNumber() {

		return number;
	}

	String getCaller_number() {
		return caller_number;
	}

	// int getThreadId() {
	// int req_thread_id;
	//
	// Uri mSmsinboxQueryUri = Uri.parse("content://sms");
	// Cursor cursor1 = c.getContentResolver().query(
	// mSmsinboxQueryUri,
	// new String[] { "_id", "thread_id", "address", "person", "date",
	// "body", "type" }, null, null, null);
	//
	// if (cursor1.getCount() > 0)
	// {
	// while (cursor1.moveToNext())
	// {
	//
	// int thread_id = cursor1.getInt(1);
	// String address = cursor1.getString(cursor1
	// .getColumnIndex(ContactsContract.Contacts.));
	// if("your desired no".equals(address)){
	// req_thread_id = thread_id;
	//
	// }
	// }
	// }
	//
	// }
}
