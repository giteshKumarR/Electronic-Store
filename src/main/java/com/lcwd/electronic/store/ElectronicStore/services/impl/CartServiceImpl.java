package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartItemDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        // Fetch the user with userId
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User not found with given User ID"));logger.info("User : {}", user);
        // Take out the information of product quantity and product ID from AddItemToCartRequest request
        Integer quantity = request.getQuantity();
        String productId = request.getProductId();

        // Fetch the product with productId
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException(("User with given Id not found!!")));

        // Now using the user we will fetch the cart
        Optional<Cart> cart = cartRepository.findByUser(user);
        if(cart.isEmpty()) {
            // We don't find any cart associated with the given user.
            // We need to create a new Cart and assign all the things to that cart
            // 1. Create a new Cart
            Cart newCart = new Cart();
            // Set an Id for this newly created cart
            String uniqueCartId = UUID.randomUUID().toString();
            newCart.setCartId(uniqueCartId);
            // Attach the user with the new cart
            newCart.setUser(user);
            // 2. Now our job is to set the cart items
//            List<CartItem> cartItems = newCart.getCartItems(); // this will be an empty list
            List<CartItem> cartItems = new ArrayList<>(); // this will be an empty list
            logger.info("Cart Item list : {}", cartItems);

            CartItem newCartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(product.getProductPrice() * quantity)
                    .cart(newCart)
                    .build();
            logger.info("New Cart Item : {}", newCartItem);
            cartItems.add(newCartItem);
            logger.info("Modified Cart Items list : {}", cartItems);
            // attached the list of cart Items to the newly created Cart
            newCart.setCartItems(cartItems);
            newCart.setTotalCartItems(cartItems.size());
            newCart.setTotalCartPrice(
                    cartItems.stream()
                            .mapToDouble(CartItem::getTotalPrice)
                            .sum()
            );
            Cart savedCart = cartRepository.save(newCart);
            return mapper.map(savedCart, CartDto.class);

        } else {
            // Cart for a user is already present so just make a new Cart item and assign to it...
            // But before that check if the user is adding the same product again or not..
            boolean isProductAlreadyPresentInCart = false;

            Cart existingCart = cart.get();
            List<CartItem> cartItems = existingCart.getCartItems();  // This will have previous cart items too.
            for(CartItem cartItem: cartItems) {
                //  If we find any product already in the cart just update the quantity and total price
                if(cartItem.getProduct().getProductId().equalsIgnoreCase(productId)) {
                    logger.info("Already present Cart Item : {}", cartItem);
                    isProductAlreadyPresentInCart = true;
                    cartItem.setQuantity(cartItem.getQuantity()+quantity);
                    cartItem.setTotalPrice(cartItem.getQuantity()*product.getProductPrice());
                    logger.info("Already present Cart Item modified quantity: {}", cartItem);

                    double sum = cartItems.stream()
                            .mapToDouble(CartItem::getTotalPrice)
                            .sum();
                    existingCart.setTotalCartPrice(sum);
                    logger.info("Modified total price of the Cart : {}", sum);
                }
            }
            if(!isProductAlreadyPresentInCart) {
                // This part executes when the product is not there in the cart
                // So create a new cart item.
                CartItem newCartItem = CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .totalPrice(product.getProductPrice() * quantity)
                        .cart(existingCart)
                        .build();
                cartItems.add(newCartItem);
                // attached the list of cart Items to the newly created Cart
                existingCart.setCartItems(cartItems);
                logger.info("Cart Item list : {}", cartItems);
                existingCart.setTotalCartItems(cartItems.size());
                double sum = cartItems.stream()
                        .mapToDouble(CartItem::getTotalPrice)
                        .sum();
                existingCart.setTotalCartPrice(sum);
            }
            Cart savedCart = cartRepository.save(existingCart);
            return Helper.mapCartEntityToCartDto(savedCart);
        }
    }

    @Override
    public void removeItemFromCart(String userId, Long cartItemId) {
        Cart cart = cartRepository.findByUser_UserId(userId).orElseThrow(() -> new ResourseNotFoundException("Cart not found for the given user !!"));
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            throw new ResourseNotFoundException("No items in Cart!!");
        } else {
            boolean isCartItemPresent = cartItems.stream()
                    .anyMatch(item -> item.getCartItemId().equals(cartItemId));
            if(!isCartItemPresent) {
                throw new ResourseNotFoundException("CartItem not present with given Id not present in the cart!!");
            }
            //NOTE:  We will be sure that the CartItem with cartItemId is present, now our job is to completely
            // delete from the DB but before this we have to remove its relation with cart and product.

            // Make a new list excluding the cart item to be removed
            List<CartItem> modifiedCartItemList = cartItems.stream()
                    .filter(item -> !item.getCartItemId().equals(cartItemId))
                    .collect(Collectors.toList());
            cart.setCartItems(modifiedCartItemList);
            cart.setTotalCartItems(modifiedCartItemList.size());
            double sum = modifiedCartItemList.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            cart.setTotalCartPrice(sum);

            // We are good to delete the to be deleted Cart Item from the DB
            CartItem cartItemToBeRemoved = cartItemRepository.findById(cartItemId).get();

            cartItemToBeRemoved.setCart(null);
            cartItemToBeRemoved.setProduct(null);
            cartItemRepository.delete(cartItemToBeRemoved);
            // Save the modified cart to DB
            cartRepository.save(cart);
        }
    }

    @Override
    public void removeAllFromCart(String userId) {
        Cart cart = cartRepository.findByUser_UserId(userId).orElseThrow(() -> new ResourseNotFoundException("Cart not found for the given user !!"));
        List<CartItem> cartItemList = cart.getCartItems();
        cartItemList.forEach(cartItem -> {
            removeItemFromCart(userId, cartItem.getCartItemId());
            logger.info("Deleted successfully");
        });
        cart.setCartItems(Collections.emptyList());

        // Save the modified cart to DB
        cartRepository.save(cart);
    }

    @Override
    public CartDto searchCartByUser(String userId) {
        Cart cart = cartRepository.findByUser_UserId(userId).orElseThrow(() -> new ResourseNotFoundException("Cart not fund with given UserId"));
        return Helper.mapCartEntityToCartDto(cart);
    }
}
