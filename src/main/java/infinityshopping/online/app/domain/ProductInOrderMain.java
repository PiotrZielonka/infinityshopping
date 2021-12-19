package infinityshopping.online.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "product_in_order_main")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductInOrderMain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Column(name = "category")
  private String category;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity", precision = 21, scale = 2)
  private BigDecimal quantity;

  @Column(name = "price_net", precision = 21, scale = 2)
  private BigDecimal priceNet;

  @Column(name = "vat", precision = 21, scale = 2)
  private BigDecimal vat;

  @Column(name = "price_gross", precision = 21, scale = 2)
  private BigDecimal priceGross;

  @Column(name = "total_price_net", precision = 21, scale = 2)
  private BigDecimal totalPriceNet;

  @Column(name = "total_price_gross", precision = 21, scale = 2)
  private BigDecimal totalPriceGross;

  @Column(name = "stock", precision = 21, scale = 2)
  private BigDecimal stock;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  @Column(name = "description")
  private String description;

  @Lob
  @Column(name = "image")
  private byte[] image;

  @Column(name = "image_content_type")
  private String imageContentType;

  @Column(name = "product_id")
  private Long productId;

  @ManyToOne
  @JsonIgnoreProperties("productInOrderMains")
  private OrderMain orderMain;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public ProductInOrderMain id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return this.category;
  }

  public ProductInOrderMain category(String category) {
    this.setCategory(category);
    return this;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return this.name;
  }

  public ProductInOrderMain name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getQuantity() {
    return this.quantity;
  }

  public ProductInOrderMain quantity(BigDecimal quantity) {
    this.setQuantity(quantity);
    return this;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPriceNet() {
    return this.priceNet;
  }

  public ProductInOrderMain priceNet(BigDecimal priceNet) {
    this.setPriceNet(priceNet);
    return this;
  }

  public void setPriceNet(BigDecimal priceNet) {
    this.priceNet = priceNet;
  }

  public BigDecimal getVat() {
    return this.vat;
  }

  public ProductInOrderMain vat(BigDecimal vat) {
    this.setVat(vat);
    return this;
  }

  public void setVat(BigDecimal vat) {
    this.vat = vat;
  }

  public BigDecimal getPriceGross() {
    return this.priceGross;
  }

  public ProductInOrderMain priceGross(BigDecimal priceGross) {
    this.setPriceGross(priceGross);
    return this;
  }

  public void setPriceGross(BigDecimal priceGross) {
    this.priceGross = priceGross;
  }

  public BigDecimal getTotalPriceNet() {
    return this.totalPriceNet;
  }

  public ProductInOrderMain totalPriceNet(BigDecimal totalPriceNet) {
    this.setTotalPriceNet(totalPriceNet);
    return this;
  }

  public void setTotalPriceNet(BigDecimal totalPriceNet) {
    this.totalPriceNet = totalPriceNet;
  }

  public BigDecimal getTotalPriceGross() {
    return this.totalPriceGross;
  }

  public ProductInOrderMain totalPriceGross(BigDecimal totalPriceGross) {
    this.setTotalPriceGross(totalPriceGross);
    return this;
  }

  public void setTotalPriceGross(BigDecimal totalPriceGross) {
    this.totalPriceGross = totalPriceGross;
  }

  public BigDecimal getStock() {
    return this.stock;
  }

  public ProductInOrderMain stock(BigDecimal stock) {
    this.setStock(stock);
    return this;
  }

  public void setStock(BigDecimal stock) {
    this.stock = stock;
  }

  public String getDescription() {
    return this.description;
  }

  public ProductInOrderMain description(String description) {
    this.setDescription(description);
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getImage() {
    return this.image;
  }

  public ProductInOrderMain image(byte[] image) {
    this.setImage(image);
    return this;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getImageContentType() {
    return this.imageContentType;
  }

  public ProductInOrderMain imageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
    return this;
  }

  public void setImageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
  }

  public Long getProductId() {
    return this.productId;
  }

  public ProductInOrderMain productId(Long productId) {
    this.setProductId(productId);
    return this;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public OrderMain getOrderMain() {
    return this.orderMain;
  }

  public void setOrderMain(OrderMain orderMain) {
    this.orderMain = orderMain;
  }

  public ProductInOrderMain orderMain(OrderMain orderMain) {
    this.setOrderMain(orderMain);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductInOrderMain)) {
      return false;
    }
    return id != null && id.equals(((ProductInOrderMain) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ProductInOrderMain{"
        + "id=" + getId()
        + ", category='" + getCategory() + "'"
        + ", name='" + getName() + "'"
        + ", quantity=" + getQuantity()
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", totalPriceNet=" + getTotalPriceNet()
        + ", totalPriceGross=" + getTotalPriceGross()
        + ", stock=" + getStock()
        + ", description='" + getDescription() + "'"
        + ", image='" + getImage() + "'"
        + ", imageContentType='" + getImageContentType() + "'"
        + ", productId=" + getProductId()
        + "}";
  }
}
