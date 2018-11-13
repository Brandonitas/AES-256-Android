package com.example.brandon.aes;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView textView;
    private String aboutText = "En esta aplicación tú puedes cifrar/descifrar textos y archivos almacenados en tu dispositivo mediante el algoritmo AES 256. Los pasos son los siguientes:     \n" +
            "\n" +
            "Para cifrar:         \n" +
            "1.- Selecciona una contraseña única, la cual será la base para el cifrado         \n" +
            "2.- Ingresa el texto que quieres cifrar         \n" +
            "3.- Dar click en el botón “Encriptar” que se encuentra en la parte posterior izquierda         \n" +
            "4.- Ahora puedes enviar tu mensaje cifrado a quien tú quieras      \n" +
            "\n" +
            "\n" +
            "Para descifrar:         \n" +
            "1.- Ingresa la contraseña con la cual se generó el cifrado, de lo contrario no podrás hacer nada         \n" +
            "2.- Ingresa el texto cifrado         \n" +
            "3.- Dar click en el botón “Desencriptar” que se encuentra en la parte posterior derecha         \n" +
            "4.- Ahora en pantalla se mostrará el mensaje original      \n" +
            "\n" +
            "Para guardar/cargar archivos:         \n" +
            "1.- Ingresa el nombre del archivo que quieres guardar/cargar con su extensión (.txt por ejemplo)         \n" +
            "2.- Dar click en el botón Guardar o Cargar         \n" +
            "3.- Se te mostrará un mensaje en pantalla informándote si fue exitoso el proceso y se guardará el mensaje o se desplegará el mensaje que cargaste \n" +
            "\n" +
            "Aplicación realizada por: \n" +
            "Brandon Reyes\n" +
            "A01335537@itesm.mx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textView = findViewById(R.id.textView);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Orbitron-Regular.ttf");
        textView.setText(aboutText);
        textView.setTypeface(myCustomFont);

    }
}
