package infinityshopping.online.app.domain;

import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "order_main")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderMain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Column(name = "buyer_login")
  private String buyerLogin;

  @Column(name = "buyer_first_name")
  private String buyerFirstName;

  @Column(name = "buyer_last_name")
  private String buyerLastName;

  @Column(name = "buyer_email")
  private String buyerEmail;

  @Column(name = "buyer_phone")
  private String buyerPhone;

  @Column(name = "amount_of_cart_net", precision = 21, scale = 2)
  private BigDecimal amountOfCartNet;

  @Column(name = "amount_of_cart_gross", precision = 21, scale = 2)
  private BigDecimal amountOfCartGross;

  @Column(name = "amount_of_shipment_net", precision = 21, scale = 2)
  private BigDecimal amountOfShipmentNet;

  @Column(name = "amount_of_shipment_gross", precision = 21, scale = 2)
  private BigDecimal amountOfShipmentGross;

  @Column(name = "amount_of_order_net", precision = 21, scale = 2)
  private BigDecimal amountOfOrderNet;

  @Column(name = "amount_of_order_gross", precision = 21, scale = 2)
  private BigDecimal amountOfOrderGross;

  @Enumerated(EnumType.STRING)
  @Column(name = "order_main_status")
  private OrderMainStatusEnum orderMainStatus;

  @Column(name = "create_time")
  @CreationTimestamp
  private Instant createTime;

  @Column(name = "update_time")
  @UpdateTimestamp
  private Instant updateTime;

  @OneToMany(mappedBy = "orderMain", orphanRemoval = true)
  private Set<ProductInOrderMain> productInOrderMains = new HashSet<>();

  @OneToOne(mappedBy = "orderMain", orphanRemoval = true)
  private PaymentOrderMain paymentOrderMain;

  @OneToOne(mappedBy = "orderMain", orphanRemoval = true)
  private ShipmentOrderMain shipmentOrderMain;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public OrderMain id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBuyerLogin() {
    return this.buyerLogin;
  }

  public OrderMain buyerLogin(String buyerLogin) {
    this.setBuyerLogin(buyerLogin);
    return this;
  }

  public void setBuyerLogin(String buyerLogin) {
    this.buyerLogin = buyerLogin;
  }

  public String getBuyerFirstName() {
    return this.buyerFirstName;
  }

  public OrderMain buyerFirstName(String buyerFirstName) {
    this.setBuyerFirstName(buyerFirstName);
    return this;
  }

  public void setBuyerFirstName(String buyerFirstName) {
    this.buyerFirstName = buyerFirstName;
  }

  public String getBuyerLastName() {
    return this.buyerLastName;
  }

  public OrderMain buyerLastName(String buyerLastName) {
    this.setBuyerLastName(buyerLastName);
    return this;
  }

  public void setBuyerLastName(String buyerLastName) {
    this.buyerLastName = buyerLastName;
  }

  public String getBuyerEmail() {
    return this.buyerEmail;
  }

  public OrderMain buyerEmail(String buyerEmail) {
    this.setBuyerEmail(buyerEmail);
    return this;
  }

  public void setBuyerEmail(String buyerEmail) {
    this.buyerEmail = buyerEmail;
  }

  public String getBuyerPhone() {
    return this.buyerPhone;
  }

  public OrderMain buyerPhone(String buyerPhone) {
    this.setBuyerPhone(buyerPhone);
    return this;
  }

  public void setBuyerPhone(String buyerPhone) {
    this.buyerPhone = buyerPhone;
  }

  public BigDecimal getAmountOfCartNet() {
    return this.amountOfCartNet;
  }

  public OrderMain amountOfCartNet(BigDecimal amountOfCartNet) {
    this.setAmountOfCartNet(amountOfCartNet);
    return this;
  }

  public void setAmountOfCartNet(BigDecimal amountOfCartNet) {
    this.amountOfCartNet = amountOfCartNet;
  }

  public BigDecimal getAmountOfCartGross() {
    return this.amountOfCartGross;
  }

  public OrderMain amountOfCartGross(BigDecimal amountOfCartGross) {
    this.setAmountOfCartGross(amountOfCartGross);
    return this;
  }

  public void setAmountOfCartGross(BigDecimal amountOfCartGross) {
    this.amountOfCartGross = amountOfCartGross;
  }

  public BigDecimal getAmountOfShipmentNet() {
    return this.amountOfShipmentNet;
  }

  public OrderMain amountOfShipmentNet(BigDecimal amountOfShipmentNet) {
    this.setAmountOfShipmentNet(amountOfShipmentNet);
    return this;
  }

  public void setAmountOfShipmentNet(BigDecimal amountOfShipmentNet) {
    this.amountOfShipmentNet = amountOfShipmentNet;
  }

  public BigDecimal getAmountOfShipmentGross() {
    return this.amountOfShipmentGross;
  }

  public OrderMain amountOfShipmentGross(BigDecimal amountOfShipmentGross) {
    this.setAmountOfShipmentGross(amountOfShipmentGross);
    return this;
  }

  public void setAmountOfShipmentGross(BigDecimal amountOfShipmentGross) {
    this.amountOfShipmentGross = amountOfShipmentGross;
  }

  public BigDecimal getAmountOfOrderNet() {
    return this.amountOfOrderNet;
  }

  public OrderMain amountOfOrderNet(BigDecimal amountOfOrderNet) {
    this.setAmountOfOrderNet(amountOfOrderNet);
    return this;
  }

  public void setAmountOfOrderNet(BigDecimal amountOfOrderNet) {
    this.amountOfOrderNet = amountOfOrderNet;
  }

  public BigDecimal getAmountOfOrderGross() {
    return this.amountOfOrderGross;
  }

  public OrderMain amountOfOrderGross(BigDecimal amountOfOrderGross) {
    this.setAmountOfOrderGross(amountOfOrderGross);
    return this;
  }

  public void setAmountOfOrderGross(BigDecimal amountOfOrderGross) {
    this.amountOfOrderGross = amountOfOrderGross;
  }

  public OrderMainStatusEnum getOrderMainStatus() {
    return this.orderMainStatus;
  }

  public OrderMain orderMainStatus(OrderMainStatusEnum orderMainStatus) {
    this.setOrderMainStatus(orderMainStatus);
    return this;
  }

  public void setOrderMainStatus(OrderMainStatusEnum orderMainStatus) {
    this.orderMainStatus = orderMainStatus;
  }

  public Instant getCreateTime() {
    return this.createTime;
  }

  public OrderMain createTime(Instant createTime) {
    this.setCreateTime(createTime);
    return this;
  }

  public void setCreateTime(Instant createTime) {
    this.createTime = createTime;
  }

  public Instant getUpdateTime() {
    return this.updateTime;
  }

  public OrderMain updateTime(Instant updateTime) {
    this.setUpdateTime(updateTime);
    return this;
  }

  public void setUpdateTime(Instant updateTime) {
    this.updateTime = updateTime;
  }

  public Set<ProductInOrderMain> getProductInOrderMains() {
    return this.productInOrderMains;
  }

  public void setProductInOrderMains(Set<ProductInOrderMain> productInOrderMains) {
    if (this.productInOrderMains != null) {
      this.productInOrderMains.forEach(i -> i.setOrderMain(null));
    }
    if (productInOrderMains != null) {
      productInOrderMains.forEach(i -> i.setOrderMain(this));
    }
    this.productInOrderMains = productInOrderMains;
  }

  public OrderMain productInOrderMains(Set<ProductInOrderMain> productInOrderMains) {
    this.setProductInOrderMains(productInOrderMains);
    return this;
  }

  public OrderMain addProductInOrderMain(ProductInOrderMain productInOrderMain) {
    this.productInOrderMains.add(productInOrderMain);
    productInOrderMain.setOrderMain(this);
    return this;
  }

  public OrderMain removeProductInOrderMain(ProductInOrderMain productInOrderMain) {
    this.productInOrderMains.remove(productInOrderMain);
    productInOrderMain.setOrderMain(null);
    return this;
  }

  public PaymentOrderMain getPaymentOrderMain() {
    return paymentOrderMain;
  }

  public OrderMain paymentOrderMain(PaymentOrderMain paymentOrderMain) {
    this.paymentOrderMain = paymentOrderMain;
    return this;
  }

  public void setPaymentOrderMain(PaymentOrderMain paymentOrderMain) {
    this.paymentOrderMain = paymentOrderMain;
  }

  public ShipmentOrderMain getShipmentOrderMain() {
    return shipmentOrderMain;
  }

  public OrderMain shipmentOrderMain(ShipmentOrderMain shipmentOrderMain) {
    this.shipmentOrderMain = shipmentOrderMain;
    return this;
  }

  public void setShipmentOrderMain(ShipmentOrderMain shipmentOrderMain) {
    this.shipmentOrderMain = shipmentOrderMain;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderMain)) {
      return false;
    }
    return id != null && id.equals(((OrderMain) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "OrderMain{"
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
