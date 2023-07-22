package org.moonila.code.parser.ui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "${apiPrefix}")
public class CodeParserController {

  @Value("${parserServiceUrl}")
  private String parserServiceUrl;

  @GetMapping("/resource")
  @ResponseBody
  public Map<String, Object> home() {
    Map<String, Object> model = new HashMap<>();
    model.put("id", UUID.randomUUID().toString());
    model.put("content", "Load a source file");
    return model;
  }

  @PostMapping("/file-upload")
  @ResponseBody
  public String uploadFile(@RequestParam("file") MultipartFile file) {
    String urlUpload = parserServiceUrl + "/upload";
    String result = null;
    try {
      //Define multipart header
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      //Transform the multipart file as resource
      ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename(); // Filename has to be returned in order to be able to post.
        }
      };

      //Define the body
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", contentsAsResource);

      //Define the entity to be post
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      //Post the request using RestTemplate
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> resultTmp = restTemplate.postForEntity(urlUpload, requestEntity, String.class);
      result = resultTmp.getBody();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  @GetMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
    return "forward:/";
  }
}
