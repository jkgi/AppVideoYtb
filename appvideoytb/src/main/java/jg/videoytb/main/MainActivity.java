package jg.videoytb.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends FragmentActivity {

    private final String USER_NAME = "USER_NAME";

    LoginButton loginButton;
    CallbackManager callbackManager;
    Button buttonT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        buttonT=(Button)findViewById(R.id.button_t);
        buttonT.setOnClickListener(onClickListener);

        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                Log.i("FaceBook", "Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("FaceBook", "Login attempt failed.");
            }
        });

    }



    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(AccessToken.getCurrentAccessToken()==null){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Login please!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            startNvgDrawerActivity();
        }
    };

    private void startNvgDrawerActivity(){
        Intent intent=new Intent(getApplicationContext(), NvgDrawerActivity.class);
        intent.putExtra(USER_NAME, Profile.getCurrentProfile().getName());
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
