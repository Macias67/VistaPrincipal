package com.astrodev.chris.vistaprincipal.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.astrodev.chris.vistaprincipal.R;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private TextView usuarioID;
    private TextView tokenFB;
    private TextView nombreUsuario;
    private ImageView imagenDePerfil;
    private LoginButton botonLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicia el SDK de Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        printKeyHash();

        // establecer devoluciones de la llamada

        botonLogin = (LoginButton) findViewById(R.id.botonLogin);
        usuarioID = (TextView) findViewById(R.id.usuarioID);
        tokenFB = (TextView) findViewById(R.id.token);
        nombreUsuario = (TextView) findViewById(R.id.nombre_de_usuario);
        imagenDePerfil = (ImageView) findViewById(R.id.imagen_perfil);

        botonLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String usuarioId = loginResult.getAccessToken().getUserId();
                String accessToken = loginResult.getAccessToken().getToken();

                Profile profile = Profile.getCurrentProfile();

                nombreUsuario.setText(profile.getName());
                usuarioID.setText(usuarioId);
                tokenFB.setText(accessToken);

                String perfilImgUrl = "https://graph.facebook.com/" + usuarioId + "/picture?type=large";

                Glide.with(LoginActivity.this).load(perfilImgUrl).into(imagenDePerfil);
            }

            @Override
            public void onCancel() {
                String cancelado = "Inicio de sesión cancelado !!";
                usuarioID.setText(cancelado);
            }

            @Override
            public void onError(FacebookException error) {
                String errorText = "Error en el inicio de sesión !!";
                usuarioID.setText( errorText );
            }
        });

        if( estasLogeado() ){
            usuarioID.setText(AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    private boolean estasLogeado(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null) && (!accessToken.isExpired());
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.astrodev.chris.loginwhitfb",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("Mi KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data)
        ;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs de'app desactivada' App Eventos.
        AppEventsLogger.deactivateApp(this);
    }
}
