package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class CartDtoAmountsGross implements Serializable {

  private Long id;

  @NotNull
  private BigDecimal amountOfCartGross;

  @NotNull
  private BigDecimal amountOfShipmentGross;

  @NotNull
  private BigDecimal amountOfOrderGross;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getAmountOfCartGross() {
    return amountOfCartGross;
  }

  public void setAmountOfCartGross(BigDecimal amountOfCartGross) {
    this.amountOfCartGross = amountOfCartGross;
  }

  public BigDecimal getAmountOfShipmentGross() {
    return amountOfShipmentGross;
  }

  public void setAmountOfShipmentGross(BigDecimal amountOfShipmentGross) {
    this.amountOfShipmentGross = amountOfShipmentGross;
  }

  public BigDecimal getAmountOfOrderGross() {
    return amountOfOrderGross;
  }

  public void setAmountOfOrderGross(BigDecimal amountOfOrderGross) {
    this.amountOfOrderGross = amountOfOrderGross;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CartDTO)) {
      return false;
    }

    CartDtoAmountsGross cartDtoAmountsGross = (CartDtoAmountsGross) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, cartDtoAmountsGross.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "CartDtoAmountsGross{"
        + "id=" + getId()
        + ", amountOfCartGross=" + getAmountOfCartGross()
        + ", amountOfShipmentGross=" + getAmountOfShipmentGross()
        + ", amountOfOrderGross=" + getAmountOfOrderGross()
        + "}";
  }
}
