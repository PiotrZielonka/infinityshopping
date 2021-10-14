package infinityshopping.online.app.domain;

import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category_enum", nullable = false)
    private ProductCategoryEnum productCategoryEnum;

    @NotNull
    @Size(min = 0, max = 5000)
    @Column(name = "name", length = 5000, nullable = false)
    private String name;

    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000000")
    @Column(name = "price_net", precision = 21, scale = 2, nullable = false)
    private BigDecimal priceNet;

    @NotNull
    @DecimalMin(value = "5")
    @DecimalMax(value = "100")
    @Column(name = "vat", precision = 21, scale = 2, nullable = false)
    private BigDecimal vat;

    @Column(name = "price_gross", precision = 21, scale = 2)
    private BigDecimal priceGross;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000000")
    @Column(name = "stock", precision = 21, scale = 2, nullable = false)
    private BigDecimal stock;

    @Size(min = 0, max = 10000)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", length = 10000, nullable = false)
    private String description;

    @Column(name = "create_time")
    @CreationTimestamp
    private Instant createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    private Instant updateTime;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @NotNull
    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductCategoryEnum getProductCategoryEnum() {
        return this.productCategoryEnum;
    }

    public Product productCategoryEnum(ProductCategoryEnum productCategoryEnum) {
        this.setProductCategoryEnum(productCategoryEnum);
        return this;
    }

    public void setProductCategoryEnum(ProductCategoryEnum productCategoryEnum) {
        this.productCategoryEnum = productCategoryEnum;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public Product quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceNet() {
        return this.priceNet;
    }

    public Product priceNet(BigDecimal priceNet) {
        this.setPriceNet(priceNet);
        return this;
    }

    public void setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }

    public BigDecimal getVat() {
        return this.vat;
    }

    public Product vat(BigDecimal vat) {
        this.setVat(vat);
        return this;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getPriceGross() {
        return this.priceGross;
    }

    public Product priceGross(BigDecimal priceGross) {
        this.setPriceGross(priceGross);
        return this;
    }

    public void setPriceGross(BigDecimal priceGross) {
        this.priceGross = priceGross;
    }

    public BigDecimal getStock() {
        return this.stock;
    }

    public Product stock(BigDecimal stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public Product createTime(Instant createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return this.updateTime;
    }

    public Product updateTime(Instant updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "Product{"
        + "id=" + getId()
        + ", productCategoryEnum='" + getProductCategoryEnum() + "'"
        + ", name='" + getName() + "'"
        + ", quantity=" + getQuantity()
        + ", priceNet=" + getPriceNet()
        + ", vat=" + getVat()
        + ", priceGross=" + getPriceGross()
        + ", stock=" + getStock()
        + ", description='" + getDescription() + "'"
        + ", createTime='" + getCreateTime() + "'"
        + ", updateTime='" + getUpdateTime() + "'"
        + ", image='" + getImage() + "'"
        + "}";
  }
}
