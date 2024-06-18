package loolu.loolu_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private Long categoryId;
    private String categoryName;
    private List<String> imageUrls;
}
