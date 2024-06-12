package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loolu.loolu_backend.dto.CartItemDto;
import loolu.loolu_backend.dto.UpdateCartItemDto;
import loolu.loolu_backend.models.Cart;
import loolu.loolu_backend.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.status;

@Tag(name = "Cart Controller", description = "Controller for managing cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart")
    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(@RequestBody @Valid CartItemDto cartItem) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addItemToCart(cartItem));
    }

    @Operation(summary = "Get all cart's items")
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems() {
        return ResponseEntity
                .ok(cartService.getCartItems());
    }

    @Operation(summary = "Delete item from cart")
    @DeleteMapping("/{item-id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable("item-id") Long itemId) {
        cartService.deleteItemFromCart(itemId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Update cart item")
    @PutMapping("/{item-id}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable("item-id") Long itemId, @RequestBody @Valid UpdateCartItemDto updateCartItem) {
        return ResponseEntity
                .ok(cartService.updateCartItem(itemId, updateCartItem));
    }

    @Operation(summary = "Clear cart")
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity
                .noContent()
                .build();
    }
}
