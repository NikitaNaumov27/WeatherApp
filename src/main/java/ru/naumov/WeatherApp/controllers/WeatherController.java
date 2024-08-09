package ru.naumov.WeatherApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import ru.naumov.WeatherApp.models.WeatherResponse;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping()
    public String homePage(@ModelAttribute("weather") WeatherResponse weather) {
        return "index";
    }

    @PostMapping()
    public String getWeather(@ModelAttribute("weather") WeatherResponse weather, Model model) throws JsonProcessingException {
        if (weather.getName().isEmpty()){
            model.addAttribute("errorMessage", "Введите, пожалуйста, название города");
            return "index";
        }
        String response = getRestAPI(weather.getName());
        System.out.println(response);
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse weather1 = mapper.readValue(response, WeatherResponse.class);
        weather1.setName(weather.getName());
        model.addAttribute("weather", weather1);
        if (weather1.getCod() != 200){
            model.addAttribute("errorMessage", "Город не найден, пожалуйста, введите корректное название города");
            return "index";
        }
        return "weather";
    }

    public String getRestAPI(String cityName){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "5f9c70690dmsh581d9cea45af0efp1d0415jsn9f11c7f0208b");
        headers.set("X-RapidAPI-Host", "open-weather13.p.rapidapi.com");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://open-weather13.p.rapidapi.com/city/" + cityName + "/%7Blang%7D";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}