package loolu.loolu_backend.services;

import loolu.loolu_backend.dto.CartItemDto;
import loolu.loolu_backend.dto.CartProductDto;
import loolu.loolu_backend.dto.UpdateCartItemDto;

import java.util.List;

public interface CartService {

    CartItemDto addItemToCart(CartItemDto cartItem);

    List<CartItemDto> getCartItems();

    CartItemDto deleteItemFromCart(Long cartId, Long itemId);

    CartItemDto clearCart();

    CartItemDto updateCartItem(Long cartId, Long itemId, UpdateCartItemDto cartItem);

    List<CartProductDto> getCartProductsDtoByCartId(Long cartId);

}
