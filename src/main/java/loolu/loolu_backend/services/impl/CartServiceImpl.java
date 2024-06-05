package loolu.loolu_backend.services.impl;

import lombok.RequiredArgsConstructor;
import loolu.loolu_backend.dto.CartItemDto;
import loolu.loolu_backend.dto.UpdateCartItemDto;
import loolu.loolu_backend.models.Cart;
import loolu.loolu_backend.models.CartProduct;
import loolu.loolu_backend.models.Product;
import loolu.loolu_backend.models.User;
import loolu.loolu_backend.repositories.CartProductRepository;
import loolu.loolu_backend.repositories.CartRepository;
import loolu.loolu_backend.repositories.ProductRepository;
import loolu.loolu_backend.repositories.UsersRepository;
import loolu.loolu_backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final UsersRepository userRepository;

    @Override
    public CartItemDto addItemToCart(CartItemDto cartItem) {
        User user = userRepository.findById(cartItem.getCartId()).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(cartItem.getQuantity());

        cartProduct = cartProductRepository.save(cartProduct);

        return new CartItemDto(cartProduct.getId(), cartProduct.getCart().getId(), cartProduct.getProduct().getId(), cartProduct.getQuantity());
    }

    @Override
    public List<CartItemDto> getCartItems() {
        List<CartProduct> cartProducts = cartProductRepository.findAll();
        return cartProducts.stream()
                .map(cartProduct -> new CartItemDto(cartProduct.getId(), cartProduct.getCart().getId(), cartProduct.getProduct().getId(), cartProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto deleteItemFromCart(Long itemId) {
        CartProduct cartProduct = cartProductRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartProductRepository.delete(cartProduct);
        return new CartItemDto(cartProduct.getId(), cartProduct.getCart().getId(), cartProduct.getProduct().getId(), cartProduct.getQuantity());
    }

    @Override
    public CartItemDto clearCart() {
        cartProductRepository.deleteAll();
        return null;
    }

    @Override
    public CartItemDto updateCartItem(Long itemId, UpdateCartItemDto cartItem) {
        CartProduct cartProduct = cartProductRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartProduct.setQuantity(cartItem.getQuantity());
        cartProduct = cartProductRepository.save(cartProduct);
        return new CartItemDto(cartProduct.getId(), cartProduct.getCart().getId(), cartProduct.getProduct().getId(), cartProduct.getQuantity());
    }
}
