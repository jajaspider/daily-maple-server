package support.maple.daily.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class KakaoService {

  @Value("${KAKAO_REDIRECT_URL}")
  private String KAKAO_REDIRECT_URL;

  @Value("${KAKAO_RESTAPI_KEY}")
  private String KAKAO_RESTAPI_KEY;

  Environment environment;

  public KakaoService(Environment environment) {
    this.environment = environment;
  }

  private final Logger logger = LoggerFactory.getLogger(KakaoService.class);

  public JsonNode getKakaoUserInfo(JsonNode accessToken) {

    final String requestUrl = "https://kapi.kakao.com/v2/user/me";
    final HttpClient client = HttpClientBuilder.create().build();
    final HttpPost post = new HttpPost(requestUrl);

    // add header
    post.addHeader("Authorization", "Bearer " + accessToken);  //토큰으로 authorization권한 얻는것.

    JsonNode returnNode = null;

    try {
      final HttpResponse response = client.execute(post);
      final int responseCode = response.getStatusLine().getStatusCode();
      final String msg = response.getStatusLine().getReasonPhrase();
      logger.info("Sending 'POST' request to URL : " + requestUrl);
      logger.info("Response Code : " + responseCode);
      logger.info("Response Code : " + msg);

      //HttpEntity entity = response.getEntity();  이 주석처리 되어있는 코드들은 혹시 오류가 나는 상황이라면 주석 없애고 실행 ㄱㄱ 무슨 오류인지 알려줄거임.
      //String responseString = EntityUtils.toString(entity, "UTF-8");
      //logger.info("responseString----->"+responseString);
      // JSON 형태 반환값 처리
      ObjectMapper mapper = new ObjectMapper();
      returnNode = mapper.readTree(response.getEntity().getContent());

    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // clear resources
    }
    return returnNode;
  }

  public JsonNode getKakaoAccessToken(String code) {
    final String requestUrl = "https://kauth.kakao.com/oauth/token"; // Host
    final List<NameValuePair> postParams = new ArrayList<NameValuePair>();

    postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
    postParams.add(new BasicNameValuePair("client_id", KAKAO_RESTAPI_KEY)); // REST API KEY
    postParams.add(new BasicNameValuePair("redirect_uri", KAKAO_REDIRECT_URL)); // 리다이렉트 URI
    postParams.add(new BasicNameValuePair("code", code)); // 로그인 과정중 얻은 code 값
    logger.info("Post parameters : " + postParams);
    final HttpClient client = HttpClientBuilder.create().build();
    final HttpPost post = new HttpPost(requestUrl);

    JsonNode returnNode = null;

    try {
      post.setEntity(new UrlEncodedFormEntity(postParams));

      final HttpResponse response = client.execute(post);
      final int responseCode = response.getStatusLine().getStatusCode();
      logger.info("Sending 'POST' request to URL : " + requestUrl);
      logger.info("Post parameters : " + postParams);
      logger.info("Response Code : " + responseCode);

      // JSON 형태 반환값 처리
      ObjectMapper mapper = new ObjectMapper();

      returnNode = mapper.readTree(response.getEntity().getContent());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return returnNode;
  }
}