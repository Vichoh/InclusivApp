package com.example.adrian.avance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import back.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {


    private CallbackManager callbackManager;
    private LoginButton loginButton;
    public static Usuario usuario ;

    TextView textViewFullName;
    private CircleImageView circleImageView;
    private TextView nombre_usuario, email_usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);


        textViewFullName = (TextView) findViewById(R.id.text_view_full_name);



        loginButton = (LoginButton) findViewById(R.id.loginFace);

        //Pedimos permiso para poder obtener el email
        loginButton.setReadPermissions("email");

        usuario = new Usuario();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               goMainScreen();

                //getFaceBookProfileDetails(loginResult.getAccessToken());




            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void goMainScreen() {
        Intent intent = new Intent(this, Principal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }





    private void getFaceBookProfileDetails(final AccessToken accessToken) {



        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //object retorna lo indicado en paramters.putString("fields", "email") en este caso, solo contiene el email
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                try {



                    Profile profileDefault = Profile.getCurrentProfile();


                    Login.usuario.setNombre(profileDefault.getLastName()+", "+profileDefault.getFirstName());
                    Login.usuario.setCorreo(object.getString("email"));
                   Login.usuario.setImagenPerfil(profileDefault.getProfilePictureUri(100,100));

                    Toast.makeText(getApplicationContext(), "asdas"+usuario.getNombre(), Toast.LENGTH_SHORT).show();
                    textViewFullName.setText(profileDefault.getLastName()+", "+profileDefault.getFirstName());
                    Toast.makeText(getApplicationContext(), "asdas"+usuario.getCorreo(), Toast.LENGTH_SHORT).show();



                } catch (Exception e) {
                    Log.e("E-MainActivity", "getFaceBook" + e.toString());
                }
            }
        });
        Bundle parameters = new Bundle();
        //solicitando el campo email
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
