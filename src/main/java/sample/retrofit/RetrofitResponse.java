package sample.retrofit;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.List;

import com.google.firebase.database.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class RetrofitResponse {

    public static <type> void getResponse(String body, Object c, String callMethod, Object toGlide) {
        RetrofitInterface service = RetrofitSingleton.getRetrofit().create(RetrofitInterface.class);
        try {
            Method methodRetrofit = service.getClass().getMethod(callMethod, String.class);
            Call<type> call = (Call<type>) methodRetrofit.invoke(service, body);
            call.enqueue(new Callback<type>() {
                @Override
                public void onResponse(@NotNull Call<type> call, @NotNull Response<type> response) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response.body() instanceof List) {
                                    Method methodClassCalled = c.getClass().getMethod("setList", List.class);
                                    methodClassCalled.invoke(c, response.body());
                                } else if (response.body() instanceof String) {
                                    System.out.println(response.body());
                                } else if (response.body() instanceof Boolean && toGlide != null) {
                                    //Method methodClassCalled = c.getClass().getMethod("glideObject", Boolean.class, Object.class);
                                    //noinspection JavaReflectionInvocation
                                    //methodClassCalled.invoke(c, response.body(), toGlide);
                                }else if (response.body() instanceof Integer) {
                                    Method methodClassCalled = c.getClass().getMethod("setOnlineUsers", Integer.class);
                                    methodClassCalled.invoke(c, response.body());
                                }
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onFailure(@NotNull Call<type> call, @NotNull Throwable t) {
                    if (!(t instanceof SocketTimeoutException)) {
                        //Make a popUp
                    }
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void getResponse(String body, Object c, String callMethod) {
        getResponse(body, c, callMethod, null);
    }

}
