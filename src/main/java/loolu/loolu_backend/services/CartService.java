package loolu.loolu_backend.services;

import loolu.loolu_backend.dto.CartItemDto;
import loolu.loolu_backend.dto.UpdateCartItemDto;

import java.util.List;

public interface CartService {

    CartItemDto addItemToCart(CartItemDto cartItem);

    List<CartItemDto> getCartItems();

    CartItemDto deleteItemFromCart(Long itemId);

    CartItemDto clearCart();

    CartItemDto updateCartItem(Long itemId, UpdateCartItemDto cartItem);
}
