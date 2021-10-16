package infinityshopping.online.app.service.dto;

import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductDTO implements Serializable {

  private Long id;

  @NotNull
  private ProductCategoryEnum productCategoryEnum;

  @NotNull
  @Size(min = 0, max = 5000)
  private String name;

  private BigDecimal quantity;

  @NotNull
  @DecimalMin(value = "0")
  @DecimalMax(value = "1000000")
  private BigDecimal priceNet;

  @NotNull
  @DecimalMin(value = "5")
  @DecimalMax(value = "100")
  private BigDecimal vat;

  private BigDecimal priceGross;

  @NotNull
  @DecimalMin(value = "0")
  @DecimalMax(value = "1000000")
  private BigDecimal stock;

  @Size(min = 0, max = 10000)
  @Lob
  private String description;

  private Instant createTime;

  private Instant updateTime;

  @Lob
  private byte[] image;

  private String imageContentType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductCategoryEnum getProductCategoryEnum() {
    return productCategoryEnum;
  }

  public void setProductCategoryEnum(ProductCategoryEnum productCategoryEnum) {
    this.productCategoryEnum = productCategoryEnum;
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

  public Instant getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Instant createTime) {
    this.createTime = createTime;
  }

  public Instant getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Instant updateTime) {
    this.updateTime = updateTime;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductDTO)) {
      return false;
    }

    ProductDTO productDto = (ProductDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, productDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ProductDTO{"
        + "id=" + getId()
        + ", productCategoryEnum='" + getProductCategoryEnum() + "'"
        + ", name='" + getName() + "'"
        + ", quantity=" + getQuantity()
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", stock=" + getStock()
        + ", description='" + getDescription() + "'"
        + ", createTime='" + getCreateTime() + "'"
        + ", updateTime='" + getUpdateTime() + "'"
        + ", image='" + getImage() + "'"
        + "}";
  }
}
