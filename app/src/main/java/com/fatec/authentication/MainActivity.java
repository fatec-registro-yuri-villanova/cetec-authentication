package com.fatec.authentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button createUser;
    EditText emailInput,passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);

        createUser = findViewById(R.id.btn_create_user);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        int seed = new Random().nextInt();
        String email = "email"+seed+"@gmail.com";
        String password = "123456";

//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
//                email,
//                password
//        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                Context context = MainActivity.this;
//                int sizeMessage = Toast.LENGTH_SHORT;
//                String tag = "MainActivity";
//                String messageSuccess = "Cadastro efetuado com sucesso!!";
//                String messageFail = "Erro!!";
//
//                if (task.isSuccessful()) {
//                    Toast.makeText(
//                            context,
//                            messageSuccess,
//                            sizeMessage
//                    ).show();
//                    Log.i(tag, messageSuccess);
//                } else {
//                    Toast.makeText(
//                            context,
//                            "Erro!!",
//                            sizeMessage
//                    ).show();
//                    Log.i(tag, messageFail);
//                }
//            }
//        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("teste"," email: "+email+"\n password:"+password);
            }
        });
    }
}