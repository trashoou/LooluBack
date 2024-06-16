package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "File Upload Controller", description = "Endpoints for uploading files")
public class FileUploadController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String UPLOAD_DIR = "/path/to/upload/dir"; // Путь к каталогу загрузки файлов на сервер

    @Operation(
            summary = "Upload a photo",
            description = "Upload a photo file to the server"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain")
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request - File is empty or exceeds the size limit"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error - Error occurred while uploading the file"
    )
    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds the limit of 5MB");
        }

        try {
            Path path = Paths.get(UPLOAD_DIR + "/" + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            // Возвращаем URL загруженного файла, который будет использоваться на фронтенде
            String fileUrl = "/api/upload/photo/" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading the file: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get a photo by filename",
            description = "Get a photo file by its filename"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File found and returned",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/octet-stream")
    )
    @ApiResponse(
            responseCode = "404",
            description = "File not found"
    )
    @GetMapping("/photo/{filename:.+}")
    public ResponseEntity<Resource> getPhotoByFilename(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", filename);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/file")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        // Logic to handle file upload
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }
        // Process the file
        return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
    }
}
