package com.example.brandon.aes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evgenii.jsevaluator.interfaces.JsCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private JsEncryptor mJsEncryptor;
    private TextView encriptar, desencriptar, about;
    private EditText contrasena, mensaje, archivo;
    private Button save, read;
    private CheckBox check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        encriptar = findViewById(R.id.encriptar);
        desencriptar = findViewById(R.id.desencriptar);
        contrasena = findViewById(R.id.contrasena);
        mensaje = findViewById(R.id.mensaje);
        archivo = findViewById(R.id.archivo);
        save = findViewById(R.id.save);
        read = findViewById(R.id.read);
        about = findViewById(R.id.about);
        check = findViewById(R.id.checkBox);

        mJsEncryptor = JsEncryptor.evaluateAllScripts(this);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        encriptar.setTypeface(myCustomFont);
        desencriptar.setTypeface(myCustomFont);
        contrasena.setTypeface(myCustomFont);
        mensaje.setTypeface(myCustomFont);
        archivo.setTypeface(myCustomFont);
        save.setTypeface(myCustomFont);
        read.setTypeface(myCustomFont);
        about.setTypeface(myCustomFont);


        check.setOnCheckedChangeListener(this);


    }

    public void onEncryptButton(View view) {
        if(!isEncryptable()){
            Toast.makeText(this, "Ingresa una contraseña y texto a encriptar", Toast.LENGTH_SHORT).show();
            return;
        }

        mJsEncryptor.encrypt(mensajeEncript(), passwordEncript(),
                new JsCallback() {
                    @Override
                    public void onResult(final String encryptedMessage) {
                        mensaje.setText(encryptedMessage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void onDecryptButton(View view) {
        if(!isDecryptable()){
            Toast.makeText(this, "Ingresa contrasña", Toast.LENGTH_SHORT).show();
            return;
        }
        mJsEncryptor.decrypt(mensajeEncript(), passwordEncript(),
                new JsCallback() {
                    @Override
                    public void onResult(final String decryptedTextFromJs) {
                        if (decryptedTextFromJs != null && !decryptedTextFromJs.trim().isEmpty()) {
                            mensaje.setText(decryptedTextFromJs);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void verPassword(View view) {

    }

    public boolean isDecryptable(){
        if(!hasPassword()){
            return false;
        }
        return true;
    }

    public boolean isEncryptable(){
        return hasMessage() && hasPassword();
    }


    public JsEncryptor getEncryptor() {
        return mJsEncryptor;
    }



    public boolean hasMessage() {
        return mensajeEncript().length() > 0;
    }


    public boolean hasPassword() {
        return passwordEncript().length() > 0;
    }



    public String mensajeEncript() {
        return mensaje.getText().toString().trim();
    }


    public String passwordEncript() {
        return contrasena.getText().toString().trim();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(!isChecked){
            contrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else{
            contrasena.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }


    private boolean isExternaStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Se puede escribir");
            return true;
        }else{
            return false;
        }
    }

    public void writeFile(View v){
        if(isExternaStorageWritable() && checkedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File textFile = new File(Environment.getExternalStorageDirectory(), archivo.getText().toString());
            try{
                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(mensaje.getText().toString().getBytes());
                fos.close();
                Toast.makeText(this, "Archivo guardado con éxito", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No se cuentan con los permisos de External Storage", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;
    public boolean checkedPermission(String permission){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
        }else{
            return true;
        }
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }



    public void readFile(View v){
        if(isExternalStorageReadable()){
            StringBuilder sb = new StringBuilder();
            try {
                File textFile = new File(Environment.getExternalStorageDirectory(), archivo.getText().toString());
                FileInputStream fis = new FileInputStream(textFile);

                if(fis!=null){
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line= null;
                    while((line = buff.readLine())!= null){
                        sb.append(line + "\n");
                    }
                    fis.close();
                }

                mensaje.setText(sb);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No se cuentan con los permisos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageReadable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.i("State","Se puede leer");
            return true;
        }else {
            return false;
        }
    }

    public void aboutClick(View v){
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }
}