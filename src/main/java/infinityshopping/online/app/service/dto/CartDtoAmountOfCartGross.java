package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class CartDtoAmountOfCartGross implements Serializable {

  private Long id;

  @NotNull
  private BigDecimal amountOfCartGross;

  private UserDTO user;

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

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CartDtoAmountOfCartGross)) {
      return false;
    }

    CartDtoAmountOfCartGross cartDtoAmountOfCartGross = (CartDtoAmountOfCartGross) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, cartDtoAmountOfCartGross.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "CartDtoAmountOfCartGross{"
        + "id=" + getId()
        + ", amountOfCartGross=" + getAmountOfCartGross()
        + "}";
  }
}
