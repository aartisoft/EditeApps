package domain.com.recipes;

/**
 * Created by Ahmed on 6/13/2018.
 */
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class connectionDetect {

    Context Context;

    public connectionDetect(Context Context){

        this.Context = Context;

    }
    public Boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) Context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
