package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductDtoImageNamePriceGross implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 0, max = 5000)
  private String name;

  private BigDecimal priceGross;

  @Lob
  private byte[] image;

  private String imageContentType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPriceGross() {
    return priceGross;
  }

  public void setPriceGross(BigDecimal priceGross) {
    this.priceGross = priceGross;
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
    if (!(o instanceof ProductDtoImageNamePriceGross)) {
      return false;
    }

    ProductDtoImageNamePriceGross productDtoImageNamePriceGross = (ProductDtoImageNamePriceGross) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, productDtoImageNamePriceGross.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ProductDtoImageNamePriceGross{"
        + "id=" + getId()
        + ", name='" + getName() + "'"
        + ", priceGross=" + getPriceGross()
        + ", image='" + getImage() + "'"
        + "}";
  }
}
