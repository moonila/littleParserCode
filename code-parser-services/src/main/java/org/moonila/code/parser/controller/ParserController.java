package org.moonila.code.parser.controller;

import org.moonila.code.parser.services.ParserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@CrossOrigin("http://localhost:8080")
public class ParserController {

    @Autowired
    private ParserServices parserServices;

    @PostMapping("/upload")
    public ResponseEntity<?> parseFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            String tmpdir = System.getProperty("java.io.tmpdir");
            File newFile = new File(tmpdir, file.getOriginalFilename());
            file.transferTo(newFile);
            message = parserServices.parseFile(newFile);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/parseDir")
    public ResponseEntity<?> parseDir(@RequestParam("dir") String dirPath,
                                      @RequestParam("result") String resultPath) {
        try {
            parserServices.parseDir(dirPath, resultPath);
            return ResponseEntity.status(HttpStatus.OK).body("All is ok and result available in "+resultPath);
        } catch (Exception e) {
           String message = "Could not upload the file: " + dirPath + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

}
