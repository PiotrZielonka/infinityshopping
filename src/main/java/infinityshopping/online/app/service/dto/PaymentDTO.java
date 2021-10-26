package infinityshopping.online.app.service.dto;

import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PaymentDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 0, max = 1000)
  private String name;

  @NotNull
  @DecimalMin(value = "0")
  @DecimalMax(value = "10000")
  private BigDecimal priceNet;

  @NotNull
  @DecimalMin(value = "0")
  @DecimalMax(value = "100")
  private BigDecimal vat;

  private BigDecimal priceGross;

  @NotNull
  private PaymentStatusEnum paymentStatus;

  private Instant createTime;

  private Instant updateTime;

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

  public PaymentStatusEnum getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
    this.paymentStatus = paymentStatus;
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
    if (!(o instanceof PaymentDTO)) {
      return false;
    }

    PaymentDTO paymentDto = (PaymentDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, paymentDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "PaymentDTO{"
        + "id=" + getId()
        + ", name='" + getName() + "'"
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", paymentStatus='" + getPaymentStatus() + "'"
        + ", createTime='" + getCreateTime() + "'"
        + ", updateTime='" + getUpdateTime() + "'"
        + "}";
  }
}
