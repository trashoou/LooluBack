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
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(cartService.addItemToCart(cartItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @Operation(summary = "Get all cart's items")
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems() {
        List<CartItemDto> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(cartItems);
        }
        return ResponseEntity
                .ok(cartItems);
    }

    @Operation(summary = "Delete item from cart")
    @DeleteMapping("/{item-id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable("item-id") Long itemId) {
        try {
            cartService.deleteItemFromCart(itemId);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Operation(summary = "Update cart item")
    @PutMapping("/{item-id}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable("item-id") Long itemId, @RequestBody @Valid UpdateCartItemDto updateCartItem) {
        try {
            return ResponseEntity
                    .ok(cartService.updateCartItem(itemId, updateCartItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @Operation(summary = "Clear cart")
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        try {
            cartService.clearCart();
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
