package loolu.loolu_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;

    @NotNull
    private Long cartId;

    @NotNull
    private Long productId;

    @NotNull
    private Long quantity;
}

