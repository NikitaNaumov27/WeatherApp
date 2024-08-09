package ru.naumov.WeatherApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.naumov.WeatherApp.models.Weather;


@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final ModelMapper modelMapper;


    @Autowired
    public WeatherController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public String homePage(@ModelAttribute("weather") Weather weather) {
        return "index";
    }

    @PostMapping()
    public String getWeather(@ModelAttribute("weather") Weather weather,
                         BindingResult bindingResult) throws JsonProcessingException {
        String response = getRestAPI(weather.getCity());
        System.out.println(response);

        Weather weather1 = modelMapper.map(response, Weather.class);


        System.out.println(weather1.getTemp());

        //if (bindingResult.hasErrors())
            //return "index";
        return "weather";
    }

    public String getRestAPI(String cityName){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "f84cf10279msh94834466ad24afcp1433c3jsn0e295174a2ab");
        headers.set("X-RapidAPI-Host", "open-weather13.p.rapidapi.com");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://open-weather13.p.rapidapi.com/city/" + cityName + "/%7Blang%7D";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}