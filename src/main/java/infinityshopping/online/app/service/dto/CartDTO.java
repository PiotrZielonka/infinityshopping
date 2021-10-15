package infinityshopping.online.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class CartDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amountOfCartNet;

    @NotNull
    private BigDecimal amountOfCartGross;

    @NotNull
    private BigDecimal amountOfShipmentNet;

    @NotNull
    private BigDecimal amountOfShipmentGross;

    @NotNull
    private BigDecimal amountOfOrderNet;

    @NotNull
    private BigDecimal amountOfOrderGross;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountOfCartNet() {
        return amountOfCartNet;
    }

    public void setAmountOfCartNet(BigDecimal amountOfCartNet) {
        this.amountOfCartNet = amountOfCartNet;
    }

    public BigDecimal getAmountOfCartGross() {
        return amountOfCartGross;
    }

    public void setAmountOfCartGross(BigDecimal amountOfCartGross) {
        this.amountOfCartGross = amountOfCartGross;
    }

    public BigDecimal getAmountOfShipmentNet() {
        return amountOfShipmentNet;
    }

    public void setAmountOfShipmentNet(BigDecimal amountOfShipmentNet) {
        this.amountOfShipmentNet = amountOfShipmentNet;
    }

    public BigDecimal getAmountOfShipmentGross() {
        return amountOfShipmentGross;
    }

    public void setAmountOfShipmentGross(BigDecimal amountOfShipmentGross) {
        this.amountOfShipmentGross = amountOfShipmentGross;
    }

    public BigDecimal getAmountOfOrderNet() {
        return amountOfOrderNet;
    }

    public void setAmountOfOrderNet(BigDecimal amountOfOrderNet) {
        this.amountOfOrderNet = amountOfOrderNet;
    }

    public BigDecimal getAmountOfOrderGross() {
        return amountOfOrderGross;
    }

    public void setAmountOfOrderGross(BigDecimal amountOfOrderGross) {
        this.amountOfOrderGross = amountOfOrderGross;
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
        if (!(o instanceof CartDTO)) {
            return false;
        }

        CartDTO cartDto = (CartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "Cart{"
        + "id=" + getId()
        + ", amountOfCartNet=" + getAmountOfCartNet()
        + ", amountOfCartGross=" + getAmountOfCartGross()
        + ", amountOfShipmentNet=" + getAmountOfShipmentNet()
        + ", amountOfShipmentGross=" + getAmountOfShipmentGross()
        + ", amountOfOrderNet=" + getAmountOfOrderNet()
        + ", amountOfOrderGross=" + getAmountOfOrderGross()
        + "}";
  }
}
