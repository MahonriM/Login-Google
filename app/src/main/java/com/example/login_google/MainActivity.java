package com.example.login_google;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.*;
public class MainActivity extends AppCompatActivity {
    FirebaseAuth mfirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    public static final int REQUEST_CODE=54643;
    List<AuthUI.IdpConfig> provider= Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    TextView status;
    TextView correo;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseAuth =FirebaseAuth.getInstance();
        mAuthListener= firebaseAuth -> {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null){
                String name=user.getDisplayName();
                String mail=user.getEmail();
                Uri image=user.getPhotoUrl();
                status=(TextView) findViewById(R.id.status);
                correo=(TextView) findViewById(R.id.correo);
                Toast.makeText(MainActivity.this,"Sesion inciada con exito",Toast.LENGTH_SHORT).show();
                status.setText("Bienvenido  "+name);
                correo.setText("El correo es " + mail);

            }
            else{
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(provider)
                        .setIsSmartLockEnabled(false)
                        .build(),REQUEST_CODE
                );
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mAuthListener);
    }
    public void cerrar(View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"Sesion cerrada",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}