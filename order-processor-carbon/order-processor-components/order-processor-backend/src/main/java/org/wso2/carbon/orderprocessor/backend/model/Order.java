package org.wso2.carbon.orderprocessor.backend.model;

/**
 * Created by hasithad on 4/9/14.
 */
public class Order {

    private String _id;
    private Long createdDate;
    private Long deliveryDate;
    private Fee fee;
    private ProductOrder[] products;
    private String customerId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public ProductOrder[] getProducts() {
        return products;
    }

    public void setProducts(ProductOrder[] products) {
        this.products = products;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
