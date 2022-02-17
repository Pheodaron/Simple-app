package com.example.springapp1.controller;

import com.example.springapp1.domain.Training;
import com.example.springapp1.domain.User;
import com.example.springapp1.repos.TrainingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private TrainingRepo trainingRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/addTrain")
    public String addTrain(
        Model model) {
        Iterable<Training> trainings = trainingRepo.findAll();
        model.addAttribute("trainings", trainings);
        return "addTrain";
    }

    @PostMapping("/addTrain")
    public String abbNewUser (
            @AuthenticationPrincipal User user,
            @RequestParam String exercise_name,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        Training train = new Training(exercise_name, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = URLEncoder.encode(
                uuidFile + "." + file.getOriginalFilename(),
                StandardCharsets.UTF_8.toString()
            );

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            train.setFilename(resultFilename);
        }

        trainingRepo.save(train);
        Iterable<Training> trainings = trainingRepo.findAll();
        model.addAttribute("trainings", trainings);
        return "addTrain";
    }
}
