package org.wso2.carbon.orderprocessor.backend.model;

/**
 * Created by hasithad on 4/21/14.
 */
public class ProductOrder {

    private String productId;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
