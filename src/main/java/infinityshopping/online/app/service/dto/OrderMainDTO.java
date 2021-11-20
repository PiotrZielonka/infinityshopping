package infinityshopping.online.app.service.dto;

import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class OrderMainDTO implements Serializable {

  private Long id;

  private String buyerLogin;

  private String buyerFirstName;

  private String buyerLastName;

  private String buyerEmail;

  private String buyerPhone;

  private BigDecimal amountOfCartNet;

  private BigDecimal amountOfCartGross;

  private BigDecimal amountOfShipmentNet;

  private BigDecimal amountOfShipmentGross;

  private BigDecimal amountOfOrderNet;

  private BigDecimal amountOfOrderGross;

  private OrderMainStatusEnum orderMainStatus;

  private Instant createTime;

  private Instant updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBuyerLogin() {
    return buyerLogin;
  }

  public void setBuyerLogin(String buyerLogin) {
    this.buyerLogin = buyerLogin;
  }

  public String getBuyerFirstName() {
    return buyerFirstName;
  }

  public void setBuyerFirstName(String buyerFirstName) {
    this.buyerFirstName = buyerFirstName;
  }

  public String getBuyerLastName() {
    return buyerLastName;
  }

  public void setBuyerLastName(String buyerLastName) {
    this.buyerLastName = buyerLastName;
  }

  public String getBuyerEmail() {
    return buyerEmail;
  }

  public void setBuyerEmail(String buyerEmail) {
    this.buyerEmail = buyerEmail;
  }

  public String getBuyerPhone() {
    return buyerPhone;
  }

  public void setBuyerPhone(String buyerPhone) {
    this.buyerPhone = buyerPhone;
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

  public OrderMainStatusEnum getOrderMainStatus() {
    return orderMainStatus;
  }

  public void setOrderMainStatus(OrderMainStatusEnum orderMainStatus) {
    this.orderMainStatus = orderMainStatus;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderMainDTO)) {
      return false;
    }

    OrderMainDTO orderMainDto = (OrderMainDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, orderMainDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "OrderMainDTO{"
        + "id=" + getId()
        + ", buyerLogin='" + getBuyerLogin() + "'"
        + ", buyerFirstName='" + getBuyerFirstName() + "'"
        + ", buyerLastName='" + getBuyerLastName() + "'"
        + ", buyerEmail='" + getBuyerEmail() + "'"
        + ", buyerPhone='" + getBuyerPhone() + "'"
        + ", amountOfCartNet=" + getAmountOfCartNet()
        + ", amountOfCartGross=" + getAmountOfCartGross()
        + ", amountOfShipmentNet=" + getAmountOfShipmentNet()
        + ", amountOfShipmentGross=" + getAmountOfShipmentGross()
        + ", amountOfOrderNet=" + getAmountOfOrderNet()
        + ", amountOfOrderGross=" + getAmountOfOrderGross()
        + ", orderMainStatus='" + getOrderMainStatus() + "'"
        + ", createTime='" + getCreateTime() + "'"
        + ", updateTime='" + getUpdateTime() + "'"
        + "}";
  }
}
