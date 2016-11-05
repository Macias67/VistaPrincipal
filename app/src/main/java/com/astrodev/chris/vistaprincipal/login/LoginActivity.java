package com.astrodev.chris.vistaprincipal.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astrodev.chris.vistaprincipal.R;

import com.astrodev.chris.vistaprincipal.VistaPrincipal;
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

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button btn_logear, btn_registrar;
    private LoginButton botonLogin;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicia el SDK de Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //Instancia de Firebase
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);
        printKeyHash();

        // establecer devoluciones de la llamada

        botonLogin = (LoginButton) findViewById(R.id.botonLoginFB);
        botonLogin.setReadPermissions(Arrays.asList("email"));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        btn_logear = (Button) findViewById(R.id.btn_logear);
        btn_registrar = (Button) findViewById(R.id.btn_registrar);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = auth.getCurrentUser();
                if(usuario != null){
                    // Ve a Vista Principal.
                    startActivity(new Intent(LoginActivity.this, VistaPrincipal.class));
                    finish();
                }
            }
        };

// Boton de Logeo con email y password

        btn_logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Falta direccion de email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Falta password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(!task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Password o Email incorrectos !!" , Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    startActivity(new Intent(LoginActivity.this, VistaPrincipal.class));
                                    finish();
                                }
                            }
                        });
            }
        });

// Boton de Logeo con Facebook
        botonLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }
            @Override
            public void onCancel() {
                String cancelado = "Inicio de sesión cancelado !!";
            }
            @Override
            public void onError(FacebookException error) {
                String errorText = "Error en el inicio de sesión !!";

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        progressBar.setVisibility(View.VISIBLE);
        botonLogin.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Error con Facebook", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                        botonLogin.setVisibility(View.VISIBLE);
                    }
                });
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

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }
}
