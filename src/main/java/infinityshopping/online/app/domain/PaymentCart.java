package infinityshopping.online.app.domain;

import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "payment_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentCart implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "price_net", precision = 21, scale = 2, nullable = false)
  private BigDecimal priceNet;

  @NotNull
  @Column(name = "vat", precision = 21, scale = 2, nullable = false)
  private BigDecimal vat;

  @NotNull
  @Column(name = "price_gross", precision = 21, scale = 2, nullable = false)
  private BigDecimal priceGross;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  private PaymentStatusEnum paymentStatus;

  @OneToOne
  @JoinColumn(unique = true)
  private Cart cart;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public PaymentCart id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public PaymentCart name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPriceNet() {
    return this.priceNet;
  }

  public PaymentCart priceNet(BigDecimal priceNet) {
    this.setPriceNet(priceNet);
    return this;
  }

  public void setPriceNet(BigDecimal priceNet) {
    this.priceNet = priceNet;
  }

  public BigDecimal getVat() {
    return this.vat;
  }

  public PaymentCart vat(BigDecimal vat) {
    this.setVat(vat);
    return this;
  }

  public void setVat(BigDecimal vat) {
    this.vat = vat;
  }

  public BigDecimal getPriceGross() {
    return this.priceGross;
  }

  public PaymentCart priceGross(BigDecimal priceGross) {
    this.setPriceGross(priceGross);
    return this;
  }

  public void setPriceGross(BigDecimal priceGross) {
    this.priceGross = priceGross;
  }

  public PaymentStatusEnum getPaymentStatus() {
    return this.paymentStatus;
  }

  public PaymentCart paymentStatus(PaymentStatusEnum paymentStatus) {
    this.setPaymentStatus(paymentStatus);
    return this;
  }

  public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public Cart getCart() {
    return this.cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public PaymentCart cart(Cart cart) {
    this.setCart(cart);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PaymentCart)) {
      return false;
    }
    return id != null && id.equals(((PaymentCart) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "PaymentCart{"
        + "id=" + getId()
        + ", name='" + getName() + "'"
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", paymentStatus='" + getPaymentStatus() + "'"
        + "}";
  }
}
