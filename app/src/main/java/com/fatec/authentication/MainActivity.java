package com.fatec.authentication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
    EditText emailInput, passwordInput;

    String PRIMARY_CHANNEL_ID = "primary_channel_id";
    NotificationManager notificationManager = null;

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

        createChannel();

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String messageErrorEmail = "Por favor, insira um endereço de e-mail válido.";
                String messageErrorPassword = "A senha deve ter pelo menos 6 caracteres.";

                // Validação dos campos de entrada
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(MainActivity.this, messageErrorEmail, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty() || password.length() < 6) {
                    Toast.makeText(MainActivity.this, messageErrorPassword, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Criação do usuário
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Context context = MainActivity.this;
                                int sizeMessage = Toast.LENGTH_SHORT;
                                String tag = "authentication-teste";
                                String messageSuccess = "Cadastro efetuado com sucesso!!";
                                String messageFail = "Erro ao criar usuário:";
                                String errorForExistingUser = "The email address is already in use by another account.";
                                String messageErrorForExistingUser = "Um usuário com esse endereço de e-mail já existe.";
                                String othersErrors = "Erro ao criar usuário. Por favor, tente novamente.";


                                if (task.isSuccessful()) {
                                    Toast.makeText(
                                            context,
                                            messageSuccess,
                                            sizeMessage
                                    ).show();
                                    Log.i(tag, messageSuccess);
                                    sendNotification("Cadastrado com Sucesso",messageSuccess);
                                } else {
                                    // Tratamento de erros específicos
                                    if (task.getException() != null) {
                                        Log.w(tag, messageFail, task.getException());
                                        if (task.getException().getMessage().contains(errorForExistingUser)) {
                                            Toast.makeText(context, messageErrorForExistingUser, sizeMessage).show();
                                            sendNotification("Cadastrado com Erro",messageErrorForExistingUser);
                                        } else {
                                            Toast.makeText(context, othersErrors, sizeMessage).show();
                                            sendNotification("Cadastrado com Erro",othersErrors);
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }
    private void createChannel() {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "notification_channel_name",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("setDescription");

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(String notificationName, String contentText) {
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(notificationName,contentText);

        //Delivery Notification
        Random notificationId = new Random();
        notificationManager.notify(notificationId.nextInt(1000), notificationBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(String notificationName, String contentText) {
        Log.i("SEND_NOTIFICATION", "getNotificationBuilder");
        return new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(notificationName)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

    }
}