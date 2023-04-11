package com.example.Backend.controller.boards;

import com.example.Backend.controller.boards.form.QuestionRegisterForm;
import com.example.Backend.service.boards.request.BoardRequest;
import com.example.Backend.service.boards.QuestionService;
import com.example.Backend.service.boards.response.BoardListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/questionBoard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
public class QuestionBoardController {

    final private QuestionService questionService;

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean questionRegister (@ModelAttribute QuestionRegisterForm form) throws IOException {
        log.info("questionRegister() 동작" + form);
        log.info("Files received: " + form.getFileList().size());

        List<String> saveImageFiles = saveImageFiles(form.getFileList());

        BoardRequest request = new BoardRequest(form.getTitle(), form.getWriter(), form.getContent(), saveImageFiles);

        return questionService.register(request);
    }

    private List<String> saveImageFiles(List<MultipartFile> fileList) {
        List<String> savedFilePaths = new ArrayList<>();
        String basePath = "../../../finalProject/WMC-Front/src/assets/questionImages/";

        for (MultipartFile multipartFile : fileList) {
            log.info("saveFiles() - filename: " + multipartFile.getOriginalFilename());
            log.info("saveFiles() - file size: " + multipartFile.getSize());

            String savedFileName = basePath + multipartFile.getOriginalFilename();
            savedFilePaths.add("assets/questionImages/"+multipartFile.getOriginalFilename());

            try{
                FileOutputStream writer = new FileOutputStream(savedFileName);
                writer.write(multipartFile.getBytes());
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return savedFilePaths;
    }

    @GetMapping("/list")
    public List<BoardListResponse> getAllNotice() {
        log.info("questionBoardList()");
        return questionService.getAllQuestion();
    }
}