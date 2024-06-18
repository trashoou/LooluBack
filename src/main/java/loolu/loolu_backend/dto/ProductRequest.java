package loolu.loolu_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private String title;
    private Double price;
    private String description;
    private Long categoryId;
    private List<String> images;
}
