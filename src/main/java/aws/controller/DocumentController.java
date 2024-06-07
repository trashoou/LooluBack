package aws.controller;

import aws.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents") // Базовый путь для всех методов в этом контроллере
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public void saveImage(@RequestParam("file") MultipartFile file) {
        documentService.upload(file);
    }

}
