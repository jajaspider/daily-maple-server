package support.maple.daily.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import support.maple.daily.Service.ParsingService;

@Controller
public class CharacterController {

  ParsingService parsingService = new ParsingService();

  @GetMapping(value = "/character/{name}")
  public String parsing(@PathVariable String name) {
    System.out.println(name);
    parsingService.getData(name);
    return "redirect:http://localhost:8080/";
  }

}
