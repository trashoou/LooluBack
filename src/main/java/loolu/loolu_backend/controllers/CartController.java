package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loolu.loolu_backend.dto.CartItemDto;
import loolu.loolu_backend.dto.CartProductDto;
import loolu.loolu_backend.dto.UpdateCartItemDto;
import loolu.loolu_backend.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart Controller", description = "Controller for managing cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get cart products by cart id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cart products returned successfully"),
            @ApiResponse(responseCode = "404", description = "No cart products found for cart")
    })
    @GetMapping("/{cart-id}")
    public ResponseEntity<List<CartProductDto>> getCartProductsByCartId(@PathVariable("cart-id") Long cartId) {
        List<CartProductDto> cartProducts = cartService.getCartProductsDtoByCartId(cartId);
        if (cartProducts.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(cartProducts);
        }
        return ResponseEntity
                .ok(cartProducts);
    }


    @Operation(summary = "Add item to cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cart items returned successfully"),
            @ApiResponse(responseCode = "404", description = "No items found in cart")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted from cart successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @DeleteMapping("/{cart-id}/{item-id}")
    public ResponseEntity<CartItemDto> deleteItemFromCart(@PathVariable("cart-id") Long cartId, @PathVariable("item-id") Long itemId) {
        try {
            CartItemDto deletedItem = cartService.deleteItemFromCart(cartId, itemId);
            return ResponseEntity.ok(deletedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Update cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/{cart-id}/{item-id}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable("cart-id") Long cartId, @PathVariable("item-id") Long itemId, @RequestBody @Valid UpdateCartItemDto updateCartItem) {
        try {
            return ResponseEntity
                    .ok(cartService.updateCartItem(cartId, itemId, updateCartItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @Operation(summary = "Clear cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
