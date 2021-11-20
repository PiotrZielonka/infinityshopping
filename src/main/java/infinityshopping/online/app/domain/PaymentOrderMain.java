package infinityshopping.online.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "payment_order_main")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentOrderMain implements Serializable {

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

  @OneToOne
  @JoinColumn(unique = true)
  private OrderMain orderMain;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public PaymentOrderMain id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public PaymentOrderMain name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPriceNet() {
    return this.priceNet;
  }

  public PaymentOrderMain priceNet(BigDecimal priceNet) {
    this.setPriceNet(priceNet);
    return this;
  }

  public void setPriceNet(BigDecimal priceNet) {
    this.priceNet = priceNet;
  }

  public BigDecimal getVat() {
    return this.vat;
  }

  public PaymentOrderMain vat(BigDecimal vat) {
    this.setVat(vat);
    return this;
  }

  public void setVat(BigDecimal vat) {
    this.vat = vat;
  }

  public BigDecimal getPriceGross() {
    return this.priceGross;
  }

  public PaymentOrderMain priceGross(BigDecimal priceGross) {
    this.setPriceGross(priceGross);
    return this;
  }

  public void setPriceGross(BigDecimal priceGross) {
    this.priceGross = priceGross;
  }

  public OrderMain getOrderMain() {
    return this.orderMain;
  }

  public void setOrderMain(OrderMain orderMain) {
    this.orderMain = orderMain;
  }

  public PaymentOrderMain orderMain(OrderMain orderMain) {
    this.setOrderMain(orderMain);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PaymentOrderMain)) {
      return false;
    }
    return id != null && id.equals(((PaymentOrderMain) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "PaymentOrderMain{"
        + "id=" + getId()
        + ", name='" + getName() + "'"
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + "}";
  }
}
