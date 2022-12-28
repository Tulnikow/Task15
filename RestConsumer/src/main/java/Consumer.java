import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.Person;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class Consumer {

    public static void main(String[] args) throws JsonProcessingException {

        String url = "http://94.198.50.185:7081/api/users";
        String result = "";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println(responseEntity.getHeaders().toString());
        String cookie = String.valueOf(responseEntity.getHeaders().get("Set-Cookie"));
        cookie = (String) cookie.subSequence(1, cookie.length() - 1);

        // Посылаем на запись пользователя
        headers.set("Cookie", cookie);
        Person person = new Person(3, "James", "Brown", 45);

        HttpEntity<Person> entity1 = new HttpEntity<Person>(person, headers);

        responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity1, String.class);
        result += responseEntity.getBody().toString();

        // Посылаем на исправление записи  пользователя
        person.setName("Thomas");
        person.setLastName("Shelby");

        entity1 = new HttpEntity<>(person, headers);

        responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity1, String.class);
        result += responseEntity.getBody().toString();

        // Удаляем  пользователя
        responseEntity = restTemplate.exchange(url + "/" + person.getId(), HttpMethod.DELETE, entity1, String.class);
        result += responseEntity.getBody().toString();

        System.out.println("Результат: " + result);
    }
}
