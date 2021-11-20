package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

public class ProductInOrderMainDTO implements Serializable {

  private Long id;

  private String category;

  private String name;

  private BigDecimal quantity;

  private BigDecimal priceNet;

  private BigDecimal vat;

  private BigDecimal priceGross;

  private BigDecimal totalPriceNet;

  private BigDecimal totalPriceGross;

  private BigDecimal stock;

  @Lob
  private String description;

  @Lob
  private byte[] image;

  private String imageContentType;
  private Long productId;

  private OrderMainDTO orderMain;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPriceNet() {
    return priceNet;
  }

  public void setPriceNet(BigDecimal priceNet) {
    this.priceNet = priceNet;
  }

  public BigDecimal getVat() {
    return vat;
  }

  public void setVat(BigDecimal vat) {
    this.vat = vat;
  }

  public BigDecimal getPriceGross() {
    return priceGross;
  }

  public void setPriceGross(BigDecimal priceGross) {
    this.priceGross = priceGross;
  }

  public BigDecimal getTotalPriceNet() {
    return totalPriceNet;
  }

  public void setTotalPriceNet(BigDecimal totalPriceNet) {
    this.totalPriceNet = totalPriceNet;
  }

  public BigDecimal getTotalPriceGross() {
    return totalPriceGross;
  }

  public void setTotalPriceGross(BigDecimal totalPriceGross) {
    this.totalPriceGross = totalPriceGross;
  }

  public BigDecimal getStock() {
    return stock;
  }

  public void setStock(BigDecimal stock) {
    this.stock = stock;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getImageContentType() {
    return imageContentType;
  }

  public void setImageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public OrderMainDTO getOrderMain() {
    return orderMain;
  }

  public void setOrderMain(OrderMainDTO orderMain) {
    this.orderMain = orderMain;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductInOrderMainDTO)) {
      return false;
    }

    ProductInOrderMainDTO productInOrderMainDto = (ProductInOrderMainDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, productInOrderMainDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ProductInOrderMainDTO{"
        + "id=" + getId()
        + ", category='" + getCategory() + "'"
        + ", name='" + getName() + "'"
        + ", quantity=" + getQuantity()
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", totalPriceNet=" + getTotalPriceNet()
        + ", totalPriceGross=" + getTotalPriceGross()
        + ", stock=" + getStock()
        + ", description='" + getDescription() + "'"
        + ", image='" + getImage() + "'"
        + ", imageContentType='" + getImageContentType() + "'"
        + ", productId=" + getProductId()
        + "}";
  }
}
