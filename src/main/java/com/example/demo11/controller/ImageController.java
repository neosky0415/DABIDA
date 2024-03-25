package com.example.demo11.controller;

import com.example.demo11.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
@Controller
public class ImageController {

    private final QuestionRepository questionRepository;

    @ResponseBody
    @GetMapping("/uploads/{files}")
    public ResponseEntity<byte[]> display(@PathVariable("files")String name)
    {
        //파일이 저장된 경로
        String savename = name;
        File file = new File("./uploads/"+ savename);

        //저장된 이미지파일의 이진데이터 형식을 구함
        byte[] result=null;//1. data
        ResponseEntity<byte[]> entity=null;

        try {
            result = FileCopyUtils.copyToByteArray(file);

            //2. header
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath())); //파일의 컨텐츠타입을 직접 구해서 header에 저장

            //3. 응답본문
            entity = new ResponseEntity<>(result,header, HttpStatus.OK);//데이터, 헤더, 상태값
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }


//    DB에 이미지 이진데이터 저장 불러오는 컨트롤러

//    @GetMapping("/question_image/{quuid}")
//    @ResponseBody
//    public HttpEntity<byte[]> question_image(@PathVariable String quuid
//
//    ) {
//        Optional<Question> questionOptional = questionRepository.findById(quuid);
//        byte[] image = null;
//        HttpHeaders headers = new HttpHeaders();;
//
//        if (questionOptional.isPresent()) {
//            Question  question = questionOptional.get();
//            try {
//                image = question.getImage();
//                //Response 헤더 작성
//                headers.setContentType(MediaType.IMAGE_PNG);
//                headers.setContentLength(image.length);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        return new HttpEntity<byte[]>(image, headers);
//    }

}
