package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PaymentDtoNamePriceGross implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 0, max = 1000)
  private String name;

  private BigDecimal priceGross;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PaymentDtoNamePriceGross)) {
      return false;
    }

    PaymentDtoNamePriceGross paymentDtoNamePriceGross = (PaymentDtoNamePriceGross) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, paymentDtoNamePriceGross.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "PaymentDtoNamePriceGross{"
        + "id=" + getId()
        + ", name='" + getName() + "'"
        + ", priceGross=" + getPriceGross()
        + "}";
  }
}
