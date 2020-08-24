package ro.bogdan.shopapp;

import java.util.List;

public class Cart {
    private int id;
    private int userId;
    private List<CartProduct> cartProducts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                '}';
    }
}
