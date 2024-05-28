package fr.esgi.al5.tayarim.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Contrôleur responsable de la gestion de l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/scrapping")
public class ScrappingController {
  @GetMapping("/test")
  public ResponseEntity<String> test() {

    String url = "https://www.airbnb.fr/api/v3/getDLSHostCalendar/7c140b9801db7d1c5fb305583079d51829039e150791a6752135a193f1fe817a?operationName=getDLSHostCalendar&locale=fr&currency=EUR&variables=%7B%22listingId%22%3A%22U3RheUxpc3Rpbmc6MTE2NTcyNzQwNTI1MTQxNzY2OA%3D%3D%22%2C%22startDate%22%3A%222024-03-31%22%2C%22endDate%22%3A%222024-06-30%22%2C%22hostCalendarViewType%22%3A%22MONTH_VIEW%22%2C%22lensTypes%22%3A%5B%22NOTE%22%2C%22PRICE%22%2C%22PROMOTION%22%5D%2C%22timeZone%22%3A%22Europe%2FParis%22%2C%22localizationContext%22%3A%7B%22timeZone%22%3A%22Europe%2FParis%22%2C%22firstDayOfWeek%22%3A1%7D%2C%22isHSTPEnabled%22%3Afalse%7D&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%227c140b9801db7d1c5fb305583079d51829039e150791a6752135a193f1fe817a%22%7D%7D";
    String res = "";
    try {
      // Créer un client HTTP
      HttpClient client = HttpClient.newHttpClient();

      // Créer une requête HTTP
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("accept","*/*")
  .header("accept-language","fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
  .header("content-type","application/json")
  .header("cookie","everest_cookie=1711317978.Q2NdFQezOnFaeOqx6vwy.UxD9nhWgrLxeOMB9YMzXNQCq9P7-TqxGb52uxFKZC1w; _airbed_session_id=0f62211f7179c0be11865b6bc45f5560; hli=1; bev=1711318130_ZjQ5ODgzNWQzODFj; tzo=120; OptanonAlertBoxClosed=2024-05-26T18:02:43.523Z; FPID=FPID2.2.hQOd0dk7K9pqawshxM9MIhhm7ZXZSqF0PDcKa21EHJc%3D.1716746562; FPAU=1.1.24564826.1716746564; _ccv=gpc%3A1%23cban%3A0_183215%3D1%2C0_200000%3D1%2C0_183243%3D1%2C0_183216%3D1%2C0_210000%3D1%2C0_210001%3D1%2C0_210002%3D1%2C0_183345%3D1%2C0_179751%3D1%2C0_183219%3D1%2C0_210010%3D1%2C0_200003%3D1%2C0_200005%3D1%2C0_179747%3D1%2C0_183241%3D1%2C0_200007%3D1%2C0_210003%3D1%2C0_210004%3D1%2C0_179754%3D0%2C0_179750%3D0%2C0_179737%3D0%2C0_179744%3D0%2C0_179739%3D0%2C0_179743%3D0%2C0_179749%3D0%2C0_200012%3D0%2C0_200011%3D0%2C0_183217%3D0%2C0_183096%3D0%2C0_179740%3D0%2C0_179752%3D0%2C0_183346%3D0%2C0_183095%3D0; cdn_exp_f1cf28f2b6bace7d4=treatment; cdn_exp_35ab349d8821ea3f9=treatment; cfrmfctr=DESKTOP; cbkp=4; auth_jitney_session_id=eb5cafb9-38b0-474c-a430-86d3ed11aa99; frmfctr=wide; _csrf_token=V4%24.airbnb.fr%24JcOhmdXTQfk%24e0x2jRbxiloattdKzvOvKJda9KpRtcHKA1LoUBUQM3E%3D; _pt=1--WyJjM2Y1NzViMzc4Yzg2ZDg1MWU5YmE0NGRlOTMxZWIwMTQwZjBlZmEyIl0%3D--b6cdebf2d15d6bf98cb5b3bcb5ab342c729651e5; rclmd=%7B%22568769036%22%3A%22google%22%7D; roles=0; _aat=0%7C8Sxs4FV5e9LF2scit0nye%2Fhj02upU4dAF5xwatyPc7WTN0zauSF4ftFSb8VspO8h; li=1; rclu=%7B%22568769036%22%3A%226zudq21PQeN4lcU%2BWvcy5gGvntKlRIB9i1fFdMJPNCw%3D%22%7D; hli=1; _cci=cban%3A2470429004; flags=131072; OptanonConsent=0_183215:1,0_200000:1,0_183243:1,0_183216:1,0_210000:1,0_210001:1,0_210002:1,0_183345:1,0_179751:1,0_183219:1,0_210010:1,0_200003:1,0_200005:1,0_179747:1,0_183241:1,0_200007:1,0_210003:1,0_210004:1,0_179754:0,0_179750:0,0_179737:0,0_179744:0,0_179739:0,0_179743:0,0_179749:0,0_200012:0,0_200011:0,0_183217:0,0_183096:0,0_179740:0,0_179752:0,0_183346:0,0_183095:0; ak_bmsc=FC1029B341EE9837696180C60718367D~000000000000000000000000000000~YAAQGz4iF7onbaSPAQAADDLmuRfMFh/xA3oEGKke+DmacVzFwjnlvAhqjcoDO3yCH0ZSKx/bgGKgScWnPAPLRKUOsOwr1HenxOK0KkQg423dP1lNFemG2+EN8I0cbLQ2anZIiyA7aE6y3B0dAzRwc0GLrAkWKodBAAJClPTjKCurL/cSVKUM0kI8y/YnPQ4WdxVCezG6HVmWsFOnIkG644h0rXE08NQVNNTWJ5hfT5aPTjAqgiTfP1+7OJKRSe1xlTATR0Pf5pRjNJmTO1+8aChKUa64RAymgOHUF+Uh6Gi1fCFHY2PS3JVtMUP/71Hm/7nQnIbIIly3EtRk1uJEz1AkuPN0M8L0JZRq+DdVu3kyjM9RZYZ+1xnyGHqSGoiVHhmv5/8=; previousTab=%7B%22id%22%3A%22ccb5ddda-8198-4389-93fa-d9f8ac1a858a%22%7D; _aaj=10%7C1%7CKwC3bfu%2FJid9HXN6zH29pNB%2FZs7Eo5rswYYMmJ3MxugfP%2BF%2BihgHLCFLPrD5jsqv4KFwEx7vc6FVd03CV4cLK0Vuly800vmCzZmaa2aYwmkfQDLh5LBhWUuwnfh4jj0Qq6%2BjslDzJzz6ZI%2FWXN5Ch%2BvvBHdvSq66CG8Xi3wYAM8%2Ffy7St9IHaQglVHJSIhZWCeFz8x%2FVAPE1A0vccKALGVC90x1Fu7%2BipghhuvE1noRMOHPrBuVY9vn9poZSs7vCMvbKQwxk07F0D939QxBaUzyChJhlCEeqU8YeYEv5mRC7KGt6j5dA7EnHit%2B8wzIulwC2%2B9VJwHKkf6CAJYIDXQLH70OheKwKtfdfP4U7%2Fs3Fol2bePEoGxqW7Fvl%2BnYSDJrXXsMoAmPBqDQ1Z%2B%2B4mc4S6np7Nsj4W4xE0G8g0IW6GeNJ80zxAVOnme%2Fz1NCcWBQHzsQHCIkGrgWl0XcIRcfgWd8gC0ADKg7nz2VGQ0OzRa%2FDpaZQ4VQqkAq16tyiOqCokCoVUrOqfPR00G5MCWwILasuGXuFMogDZhTnF4xFlmWfvMnpreN1AHp6h2VYwXxEvrFdZ3GRH45eRn99hubNLch6RESJskaM9xiuo3zzdWUyE7GLwhNBEX8RSKC%2B9JTzcF4PEDm4Nq52YPTlddahrcE4Vq4lGMm0wfd9Gek3FlCl9N4LPsG0rrI2L2RKajff2HGTAjHqRxWkWDaWFKI106JyAJpREkPHTsFv03nE%2FjQdVg9ez20YezCC1d3pIHkOlqnhGAqwi2v9OBb0zfJloC%2FE3zRFiTXx1B5djGqtOzoevfb5SoI%3D; _user_attributes=%7B%22curr%22%3A%22EUR%22%2C%22guest_exchange%22%3A0.92192%2C%22device_profiling_session_id%22%3A%221716800491-568769036-4a77fd3006c457de88b5c749%22%2C%22giftcard_profiling_session_id%22%3A%221716811375-568769036-32e1aa465b55306ed91efb4a%22%2C%22reservation_profiling_session_id%22%3A%221716811375-568769036-1508d7e9c66bca388b3e2c75%22%2C%22id%22%3A568769036%2C%22id_str%22%3A%22568769036%22%2C%22hash_user_id%22%3A%22c3f575b378c86d851e9ba44de931eb0140f0efa2%22%2C%22eid%22%3A%22POtkS__Dk6vStlci-ivuAQ%3D%3D%22%2C%22num_h%22%3A0%2C%22name%22%3A%22SnOw%22%2C%22num_action%22%3A0%2C%22is_admin%22%3Afalse%2C%22can_access_photography%22%3Afalse%2C%22travel_credit_status%22%3Anull%2C%22referrals_info%22%3A%7B%22receiver_max_savings%22%3Anull%2C%22receiver_savings_percent%22%3Anull%2C%22receiver_signup%22%3Anull%2C%22referrer_guest%22%3A%2215%E2%82%AC%22%2C%22terms_and_conditions_link%22%3A%22%2Fhelp%2Farticle%2F2269%22%2C%22wechat_link%22%3Anull%2C%22offer_discount_type%22%3Anull%7D%7D; jitney_client_session_id=2db56007-ad5d-47ad-96d6-e33ad89952ff; jitney_client_session_created_at=1716811381.274; jitney_client_session_updated_at=1716811381.274; bm_sv=6C80BF0DDD20B812B87BC3A0DE70B108~YAAQHZHdWEI9paKPAQAAfLzuuRc33TB4eyQygvhU5SQ84C0nxd9eKFA2mI7AEHD445AF7baqU2qnlIjOUPX5pgiFh7idXQWttrMdtKHdkRzjV9efgNIDaxa/IzmcXnVVtVHRnji358PkMkG0KDlcpv96di03H3QV/iF26vXGl/IYLspwVz7fsLlVdNO2lpSzo6jdM5A4ARPSW8dqo5ChxloIG220PUkTmDKDc+AYGgiXcnPtxylehOhXjOLO269p~1")
  .header("referer","https://www.airbnb.fr/multicalendar/1165727405251417668")
  .header("sec-fetch-mode","cors")
  .header("sec-fetch-site","same-origin")
  .header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
  .header("x-airbnb-api-key","d306zoyjsyarp7ifhu67rjxn52tv0t20")
          .build();

      // Envoyer la requête et obtenir la réponse
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
      res = response.body();
      // Vérifier le statut de la réponse
      if (response.statusCode() == 200) {
        String jsonResponse = response.body();
        /*

        // Analyser la réponse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Parcourir et traiter les données JSON
        JsonNode itemsNode = rootNode.path("data")
            .path("presentation")
            .path("hostCalendar")
            .path("sections")
            .path("calendarMonthYearDropdownMenuSection")
            .path("items");

        if (itemsNode.isArray()) {
          for (JsonNode item : itemsNode) {
            String value = item.path("value").asText();
            String title = item.path("title").asText();
            System.out.println("Value: " + value + " Title: " + title);
          }
        }
        */

      } else {
        System.out.println("Failed to fetch data. HTTP status code: " + response.statusCode());
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}