package infinityshopping.online.app.domain;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "shipment_order_main")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShipmentOrderMain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Size(max = 500)
  @Column(name = "first_name", length = 500, nullable = false)
  private String firstName;

  @NotNull
  @Size(max = 500)
  @Column(name = "last_name", length = 500, nullable = false)
  private String lastName;

  @NotNull
  @Size(max = 500)
  @Column(name = "street", length = 500, nullable = false)
  private String street;

  @NotNull
  @Size(max = 20)
  @Column(name = "postal_code", length = 20, nullable = false)
  private String postalCode;

  @NotNull
  @Size(max = 500)
  @Column(name = "city", length = 500, nullable = false)
  private String city;

  @NotNull
  @Size(max = 500)
  @Column(name = "country", length = 500, nullable = false)
  private String country;

  @NotNull
  @Size(max = 30)
  @Column(name = "phone_to_the_receiver", length = 30, nullable = false)
  private String phoneToTheReceiver;

  @Size(max = 10000)
  @Column(name = "firm", length = 10000)
  private String firm;

  @Size(max = 50)
  @Column(name = "tax_number", length = 50)
  private String taxNumber;

  @OneToOne
  @JoinColumn(unique = true)
  private OrderMain orderMain;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public ShipmentOrderMain id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public ShipmentOrderMain firstName(String firstName) {
    this.setFirstName(firstName);
    return this;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public ShipmentOrderMain lastName(String lastName) {
    this.setLastName(lastName);
    return this;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getStreet() {
    return this.street;
  }

  public ShipmentOrderMain street(String street) {
    this.setStreet(street);
    return this;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public ShipmentOrderMain postalCode(String postalCode) {
    this.setPostalCode(postalCode);
    return this;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCity() {
    return this.city;
  }

  public ShipmentOrderMain city(String city) {
    this.setCity(city);
    return this;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return this.country;
  }

  public ShipmentOrderMain country(String country) {
    this.setCountry(country);
    return this;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPhoneToTheReceiver() {
    return this.phoneToTheReceiver;
  }

  public ShipmentOrderMain phoneToTheReceiver(String phoneToTheReceiver) {
    this.setPhoneToTheReceiver(phoneToTheReceiver);
    return this;
  }

  public void setPhoneToTheReceiver(String phoneToTheReceiver) {
    this.phoneToTheReceiver = phoneToTheReceiver;
  }

  public String getFirm() {
    return this.firm;
  }

  public ShipmentOrderMain firm(String firm) {
    this.setFirm(firm);
    return this;
  }

  public void setFirm(String firm) {
    this.firm = firm;
  }

  public String getTaxNumber() {
    return this.taxNumber;
  }

  public ShipmentOrderMain taxNumber(String taxNumber) {
    this.setTaxNumber(taxNumber);
    return this;
  }

  public void setTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
  }

  public OrderMain getOrderMain() {
    return this.orderMain;
  }

  public void setOrderMain(OrderMain orderMain) {
    this.orderMain = orderMain;
  }

  public ShipmentOrderMain orderMain(OrderMain orderMain) {
    this.setOrderMain(orderMain);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ShipmentOrderMain)) {
      return false;
    }
    return id != null && id.equals(((ShipmentOrderMain) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ShipmentOrderMain{"
        + "id=" + getId()
        + ", firstName='" + getFirstName() + "'"
        + ", lastName='" + getLastName() + "'"
        + ", street='" + getStreet() + "'"
        + ", postalCode='" + getPostalCode() + "'"
        + ", city='" + getCity() + "'"
        + ", country='" + getCountry() + "'"
        + ", phoneToTheReceiver='" + getPhoneToTheReceiver() + "'"
        + ", firm='" + getFirm() + "'"
        + ", taxNumber='" + getTaxNumber() + "'"
        + "}";
  }
}
