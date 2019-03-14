package util;

import okhttp3.*;

import java.io.IOException;

public class HttpUtil{

    private static final OkHttpClient okHttpClient=new OkHttpClient();
    private static MediaType mediaType = MediaType.parse("application/json");
    private static String HTTP_HEAD="http://";


    public static String get(String url) throws Exception{
        Request request=new Request.Builder()
                .get().url(HTTP_HEAD+url).build();
        Response response = okHttpClient.newCall(request).execute() ;
        return (null==response)?"":response.body().string();
    }

    public static void post(String url,String body)throws Exception{
        Request request=new Request.Builder()
                .url(HTTP_HEAD+url).post(RequestBody.create(mediaType,body)).build();
        Response response = okHttpClient.newCall(request).execute() ;
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
    }
}
