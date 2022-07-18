package com.handaebong.handaebong.Ulitility;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpClinet_Delete {
    public String HttpClinet_Delete(String token, String Web, String... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String result = "";
        try {
            //DefaultHttpClient client = new DefaultHttpClient();
            //((AbstractHttpClient) client).setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpParams params2 = new BasicHttpParams();

            params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            DefaultHttpClient client = new DefaultHttpClient(params2);

            String postURL = "http://13.124.228.201:3000/" + Web;
            HttpDelete post = new HttpDelete(postURL);
            post.addHeader("authorization", "bearer " + token);

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            for (int i = 0; i < (params.length); i+=2) {
                params1.add(new BasicNameValuePair(params[i], params[i+1]));
            }

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
            //post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            client.getCredentialsProvider().clear();
            client.getCookieStore().clear();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("테스트", e+"");
        }
        return result;
    }
}
