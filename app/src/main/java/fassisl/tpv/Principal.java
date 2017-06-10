package fassisl.tpv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    //Se inician todos los elementos que necesitamos y que estan en la vista
    TextView ipView;
    TextView puertoView;
    CheckBox checkRecord;
    SharedPreferences sharedPref; //Se inicia la clase que es para crear el fichero de preferencias

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //Se le asignan los elementos de la vista a las variables creadas anteriormente
        checkRecord = (CheckBox)findViewById(R.id.checkRecuerdame);
        ipView = (TextView)findViewById(R.id.ip);
        puertoView = (TextView)findViewById(R.id.puerto);
    }


    /**
     * Método del botón OK que es para iniciar la aplicación del TPV
     * @param v
     */
    public void iniciar(View v){
        //Se crean las variables para posteriormente utilizarlas
        boolean checkRecordar = checkRecord.isChecked();
        String ip = ipView.getText().toString();
        String puerto = puertoView.getText().toString();

        //Se crea el fichero datosConexion con todos los datos de ip y puerto para posteriormente acceder a ellos
        sharedPref = this.getSharedPreferences("datosConexion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //Si está seleccionado el check de recordar se guardarán los datos en el fichero
        if (checkRecordar == true){
            editor.putString("ip", ip);
            editor.putString("puerto", puerto);
            editor.putBoolean("recordar", checkRecordar);
            editor.commit();

        } else { //Si no está pulsado no se guardan los datos y se borran los que estuvieran
            editor.putString("ip", "");
            editor.putString("puerto", "");
            editor.putBoolean("recordar", false);
            editor.commit();
        }
        //Se carga el siguiente activity
        setContentView(R.layout.activity_principal);

        //Se carga un webview conectandose a la aplicación de TPV con los datos que se han introducido
        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://"+ip+":"+puerto+"/TPV_BAR_PRIMEFACES");

    }

    /**
     * Método para cuando se inicia la aplicación
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        //Se vuelve a seleccionar el fichero
        sharedPref = this.getSharedPreferences("datosConexion", Context.MODE_PRIVATE);
        //Se obtienen los datos del fichero
        boolean recordarFichero = sharedPref.getBoolean("recordar", false);
        String ipFichero = sharedPref.getString("ip", "");
        String puertoFichero = sharedPref.getString("puerto", "");
        //Se cogen esos datos obtenidos del fichero y se editan los elementos de la vista
        checkRecord.setChecked(recordarFichero);
        ipView.setText(ipFichero);
        puertoView.setText(puertoFichero);

    }


    /**
     * Método para que el botón de volver atras de la aplicación sea para volver a la página anterior
     * dentro del webview, y así no se cierre la aplicación al volver atrás
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView mWebView;
        mWebView = (WebView) findViewById(R.id.webView);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
