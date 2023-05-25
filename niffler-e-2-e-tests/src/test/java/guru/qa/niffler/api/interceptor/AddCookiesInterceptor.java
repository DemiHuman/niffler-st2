package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieContext;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();

    final CookieContext cookieContext = CookieContext.getInstance();
    String cookieXsrf = cookieContext.getCookie("XSRF-TOKEN");
    String jsessionId = cookieContext.getCookie("JSESSIONID");

   final Builder builder = originalRequest.headers().newBuilder();
    if (jsessionId != null) {
      builder.add("JSESSIONID", jsessionId);
    }
    if (cookieXsrf != null) {
      builder.add("XSRF-TOKEN", cookieXsrf);
    }

    final Headers headers = builder.build();

    return chain.proceed(originalRequest.newBuilder()
        .headers(headers)
        .url(originalRequest.url())
        .build());
  }
}
