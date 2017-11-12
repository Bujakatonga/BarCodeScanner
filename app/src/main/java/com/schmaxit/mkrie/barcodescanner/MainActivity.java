package com.schmaxit.mkrie.barcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {
    int id;
    private ZXingScannerView scannerView;
    private String resultCode;
    private boolean sicherheit1 = false; //WWW --> website
    private boolean sicherheit2 = false; // .de .com .net .org --> bekannte und vertrauenswürdige domainendungen
    private boolean sicherheit3 = false;
    private boolean sicherheit4 = false;
    private boolean sicherheit5 = false;

    private int sicherZahl = 0;


    // Let me Google that for you :    http://lmgtfy.com/?q=Gefahren+durch+QR-Codes%3F

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerRersultHandler());
        setContentView(scannerView);
        scannerView.startCamera();

    }

    public void scan(){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerRersultHandler());
        setContentView(scannerView);
        scannerView.startCamera();
    }

   /* public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }*/




    class ZXingScannerRersultHandler implements ZXingScannerView.ResultHandler{

        @Override
        public void handleResult(Result result){
            resultCode = result.getText();
            if (resultCode.contains("www")){
                sicherheit1 = true;
                sicherZahl = sicherZahl + 1;
            }
            if(resultCode.contains(".de") || resultCode.contains(".com") || resultCode.contains(".de") || resultCode.contains(".org")
            || resultCode.contains(".net") || resultCode.contains(".info") || resultCode.contains(".eu")|| resultCode.contains(".at")
            || resultCode.contains(".ch")){
                sicherheit2 = true;
                sicherZahl = sicherZahl + 2;
            }
            if (resultCode.contains("https")){
                sicherheit3 = true;
                sicherZahl = sicherZahl + 3;
            }
            if (resultCode.contains("www.facebook.com") || resultCode.contains("www.google.")|| resultCode.contains("www.amazon.de")
                    || resultCode.contains("www.amazon.com")|| resultCode.contains("www.spotify.com")|| resultCode.contains("www.netflix.com")
                    || resultCode.contains("www.faz.net")|| resultCode.contains("www.zeit.de")|| resultCode.contains("www.ard.de")
                    || resultCode.contains("www.tagesschau.de")|| resultCode.contains("www.bpb.de")|| resultCode.contains("www.ard.de")
                    || resultCode.contains("www.faz.net")|| resultCode.contains("www.zeit.de")|| resultCode.contains("www.ard.de")
                    || resultCode.contains("www.faz.net")|| resultCode.contains("www.zeit.de")|| resultCode.contains("www.ard.de")
                    || resultCode.contains("www.faz.net")|| resultCode.contains("www.zeit.de")|| resultCode.contains("www.ard.de")) {
                sicherheit4 = true;
                sicherZahl = sicherZahl + 4;
            }

            scannerView.stopCamera();
            resultScan();
            /*Uri uriUrl = Uri.parse(resultCode);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);*/
            //Toast.makeText(MainActivity.this, resultCode, Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.activity_main);
            //scannerView.stopCamera();

        }
    }

    public void resultScan(){
        String sicher = "Sicherheitsstufe 0";

        if (sicherZahl == 1){
            sicher = "Sicherheitsstufe: 1";
        }
        if (sicherheit1 == true | sicherheit2 == true && sicherZahl >= 2){
            sicher = "Sicherheitsstufe: 2";
        }
        if (sicherheit1 == true | sicherheit2 == true | sicherheit3 == true && sicherZahl >= 4){
            sicher = "Sicherheitsstufe: 3";
        }
        if (sicherheit1 == true | sicherheit2 == true | sicherheit3 == true | sicherheit4 == true && sicherZahl >= 9){
            sicher = "Sicherheitsstufe: 4";
        }
        if(sicherheit1 | sicherheit2 | sicherheit3 | sicherheit4 | sicherheit5) {
            new AlertDialog.Builder(this)
                    .setTitle("Website öffnen?")
                    .setMessage(sicher + "\n" + resultCode)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, resultCode + " ..wird geöffnet!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(resultCode));
                            startActivity(intent);
                            sicherheitZurueckSetzen();
                            MainActivity.this.onRestart();
                            scan();
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sicherheitZurueckSetzen();
                            MainActivity.this.onRestart();
                            scan();

                        }
                    })
                    .show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Achtung dies ist Möglicherweise keine Website!")
                    .setMessage(sicher + "\n" + "Möchtest du wirklich fortfahren mit: \n" + resultCode)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(resultCode));
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, resultCode + " ..wird geöffnet!", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, resultCode + " Kann nicht geöffnet werden!", Toast.LENGTH_SHORT).show();
                            }
                            sicherheitZurueckSetzen();
                            MainActivity.this.onRestart();
                            scan();

                        }
                    })
                    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sicherheitZurueckSetzen();
                            MainActivity.this.onRestart();
                            scan();

                        }
                    })
                    .show();
        }
    }


        public void sicherheitZurueckSetzen(){
            sicherheit1 = false;
            sicherheit2 = false;
            sicherheit3 = false;
            sicherheit4 = false;
            sicherheit5 = false;
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        id = item.getItemId();
        if (id == R.id.gefahrQRCode) {
         /*   Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://lmgtfy.com/?q=Gefahren+durch+QR-Codes%3F"));
            startActivity(intent);
            MainActivity.this.onRestart();
            scan();
         */

        }
        return super.onOptionsItemSelected(item);


    }
}
