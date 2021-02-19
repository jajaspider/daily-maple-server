package support.maple.daily.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import support.maple.daily.Service.KakaoService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

  @Value("${KAKAO_REDIRECT_URL}")
  private String KAKAO_REDIRECT_URL;

  @Value("${KAKAO_RESTAPI_KEY}")
  private String KAKAO_RESTAPI_KEY;

  Environment environment;
  KakaoService kakaoService;

  public LoginController(Environment environment, KakaoService kakaoService) {
    this.environment = environment;
    this.kakaoService = kakaoService;
  }

  private final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @GetMapping(value = "/oauth")
  public String kakaoConnect() {
    String url = "https://kauth.kakao.com/oauth/authorize?" +
        "client_id=" + KAKAO_RESTAPI_KEY +
        "&redirect_uri=" + KAKAO_REDIRECT_URL +
        "&response_type=code";
    return "redirect:" + url;
  }

  @RequestMapping(value = "/callback", produces = "application/json", method = {RequestMethod.GET,
      RequestMethod.POST})
  public String kakaoLogin(@RequestParam("code") String code, RedirectAttributes ra,
      HttpSession session, HttpServletResponse response, Model model) throws IOException {
    logger.info("kakao code:" + code);
    JsonNode access_token = kakaoService.getKakaoAccessToken(code);
    JsonNode userInfo = kakaoService.getKakaoUserInfo(access_token.get("access_token"));
    String member_id = userInfo.get("id").asText();
    String member_name = null;

    JsonNode properties = userInfo.path("properties");
    member_name = properties.path("nickname").asText();
    logger.info("id : " + member_id);
    logger.info("name : " + member_name);

    return "redirect:index";

  }


}
