package vivaladev.com.smtpmessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Context mainContext;

    private String title;
    private String text;
    private String receiver;
    private String attach = "";

    private static final String sender = "smtp.test.sstu";
    private static final String login = "smtp.test.sstu";
    private static final String password = "fodaftosapBost4";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mainContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // attach button
        findViewById(R.id.buttonAttach).setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        });

        // send button
        findViewById(R.id.buttonSend).setOnClickListener(v -> {
            AsyncTask asyncTask = new AsyncTask();
            asyncTask.execute();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    //attach = data.getData().getEncodedPath();
                    final Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null );
                    cursor.moveToFirst();
                    attach = cursor.getString(0);
                    cursor.close();
                }
                break;
            }
        }
    }

    private class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(mainContext, "Message successfully send!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                title = ((EditText) findViewById(R.id.titleText)).getText().toString();
                text = ((EditText) findViewById(R.id.messageText)).getText().toString();

                receiver = ((EditText) findViewById(R.id.receiverText)).getText().toString();

                MailSender sender = new MailSender(login, password);

                //send message
                if (attach != "") {
                    sender.sendMail(title, text, MainActivity.this.sender, receiver, attach);
                } else {
                    sender.sendMail(title, text, MainActivity.this.sender, receiver, "");
                }

            } catch (Exception e) {
                Toast.makeText(mainContext, "Error sending message!", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}