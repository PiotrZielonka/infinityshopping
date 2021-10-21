package infinityshopping.online.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import infinityshopping.online.app.service.mapper.ShipmentCartMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
class ShipmentCartResourceIT {

  private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_STREET = "AAAAAAAAAA";
  private static final String UPDATED_STREET = "BBBBBBBBBB";

  private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_CITY = "AAAAAAAAAA";
  private static final String UPDATED_CITY = "BBBBBBBBBB";

  private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

  private static final String DEFAULT_PHONE_TO_THE_RECEIVER = "AAAAAAAAAA";
  private static final String UPDATED_PHONE_TO_THE_RECEIVER = "BBBBBBBBBB";

  private static final String DEFAULT_FIRM = "AAAAAAAAAA";
  private static final String UPDATED_FIRM = "BBBBBBBBBB";

  private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/shipment-cart";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static final String DEFAULT_FIRST_NAME_2 = "CCCCCCCCCC";

  private static final String DEFAULT_LAST_NAME_2 = "CCCCCCCCCC";

  private static final String DEFAULT_STREET_2 = "CCCCCCCCCC";

  private static final String DEFAULT_POSTAL_CODE_2 = "CCCCCCCCCC";

  private static final String DEFAULT_CITY_2 = "CCCCCCCCCC";

  private static final String DEFAULT_COUNTRY_2 = "CCCCCCCCCC";

  private static final String DEFAULT_PHONE_TO_THE_RECEIVER_2 = "CCCCCCCCCC";

  private static final String DEFAULT_FIRM_2 = "CCCCCCCCCC";

  private static final String DEFAULT_TAX_NUMBER_2 = "CCCCCCCCCC";

  // PaymentCart
  private static final String DEFAULT_NAME = "AAAAAAAAAA";

  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal("100");

  private static final BigDecimal DEFAULT_VAT = new BigDecimal("23");

  private static final BigDecimal DEFAULT_PRICE_GROSS = new BigDecimal("123");

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private ShipmentCartRepository shipmentCartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ShipmentCartMapper shipmentCartMapper;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restShipmentCartMockMvc;

  private ShipmentCart shipmentCart;

  private ShipmentCart shipmentCart2;

  private PaymentCart paymentCart;

  private PaymentCart paymentCart2;

  public static ShipmentCart createEntity(EntityManager em) {
    ShipmentCart shipmentCart = new ShipmentCart()
        .firstName(DEFAULT_FIRST_NAME)
        .lastName(DEFAULT_LAST_NAME)
        .street(DEFAULT_STREET)
        .postalCode(DEFAULT_POSTAL_CODE)
        .city(DEFAULT_CITY)
        .country(DEFAULT_COUNTRY)
        .phoneToTheReceiver(DEFAULT_PHONE_TO_THE_RECEIVER)
        .firm(DEFAULT_FIRM)
        .taxNumber(DEFAULT_TAX_NUMBER);
    return shipmentCart;
  }

  public static ShipmentCart createEntity2(EntityManager em) {
    ShipmentCart shipmentCart2 = new ShipmentCart()
        .firstName(DEFAULT_FIRST_NAME_2)
        .lastName(DEFAULT_LAST_NAME_2)
        .street(DEFAULT_STREET_2)
        .postalCode(DEFAULT_POSTAL_CODE_2)
        .city(DEFAULT_CITY_2)
        .country(DEFAULT_COUNTRY_2)
        .phoneToTheReceiver(DEFAULT_PHONE_TO_THE_RECEIVER_2)
        .firm(DEFAULT_FIRM_2)
        .taxNumber(DEFAULT_TAX_NUMBER_2);
    return shipmentCart2;
  }

  public static PaymentCart createEntityPaymentCart(EntityManager em) {
    PaymentCart paymentCart = new PaymentCart()
        .name(DEFAULT_NAME)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(DEFAULT_PRICE_GROSS);
    return paymentCart;
  }

  @BeforeEach
  public void initUser() throws Exception {
    // given
    // given User
    User user = new User();
    user.setLogin("alice");
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setEmail("alice@example.com");
    user.setFirstName("Alice");
    user.setLastName("Something");
    user.setImageUrl("http://placehold.it/50x50");
    user.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Cart for User
    Cart cart = new Cart();
    cart.setUser(user);
    cart.setAmountOfCartNet(BigDecimal.ZERO);
    cart.setAmountOfCartGross(BigDecimal.ZERO);
    cart.setAmountOfShipmentNet(BigDecimal.ZERO);
    cart.setAmountOfShipmentGross(BigDecimal.ZERO);
    cart.setAmountOfOrderNet(BigDecimal.ZERO);
    cart.setAmountOfOrderGross(BigDecimal.ZERO);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.saveAndFlush(user);

    // given PaymentCart for User
    paymentCart = createEntityPaymentCart(em);
    paymentCart.setCart(cart);
    paymentCartRepository.save(paymentCart);
    cart.setPaymentCart(paymentCart);
    cartRepository.save(cart);

    // given ShipmentCart
    shipmentCart = createEntity(em);
    shipmentCart.setCart(cart);
    shipmentCartRepository.save(shipmentCart);
    cart.setShipmentCart(shipmentCart);
    cartRepository.save(cart);

    // given User2
    User user2 = new User();
    user2.setLogin("caroline");
    user2.setPassword(RandomStringUtils.random(60));
    user2.setActivated(true);
    user2.setEmail("caroline@example.com");
    user2.setFirstName("Caroline");
    user2.setLastName("Something");
    user2.setImageUrl("http://placehold.it/50x50");
    user2.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Cart2 for User2
    Cart cart2 = new Cart();
    cart2.setUser(user2);
    cart2.setAmountOfCartNet(BigDecimal.ZERO);
    cart2.setAmountOfCartGross(BigDecimal.ZERO);
    cart2.setAmountOfShipmentNet(BigDecimal.ZERO);
    cart2.setAmountOfShipmentGross(BigDecimal.ZERO);
    cart2.setAmountOfOrderNet(BigDecimal.ZERO);
    cart2.setAmountOfOrderGross(BigDecimal.ZERO);
    cartRepository.save(cart2);
    user2.setCart(cart2);
    userRepository.saveAndFlush(user2);

    // given PaymentCart2
    paymentCart2 = createEntityPaymentCart(em);
    paymentCart2.setCart(cart2);
    paymentCartRepository.save(paymentCart2);
    cart2.setPaymentCart(paymentCart2);
    cartRepository.save(cart2);

    // given shipmentCart2
    shipmentCart2 = createEntity2(em);
    shipmentCart2.setCart(cart2);
    shipmentCartRepository.save(shipmentCart2);
    cart2.setShipmentCart(shipmentCart2);
    cartRepository.save(cart2);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void getShipmentCart() throws Exception {
    // Get the shipmentCart
    restShipmentCartMockMvc.perform(get(ENTITY_API_URL_ID, shipmentCart.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(shipmentCart.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
        .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
        .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
        .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
        .andExpect(jsonPath("$.phoneToTheReceiver").value(DEFAULT_PHONE_TO_THE_RECEIVER))
        .andExpect(jsonPath("$.firm").value(DEFAULT_FIRM))
        .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void getShipmentCartOfCurrentLoggedUser() throws Exception {
    // Get only the shipmentCart of current user
    restShipmentCartMockMvc.perform(get(ENTITY_API_URL + "/userShipmentCart"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(shipmentCart.getId()))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
        .andExpect(jsonPath("$.firm").value(DEFAULT_FIRM))
        .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
        .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
        .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
        .andExpect(jsonPath("$.phoneToTheReceiver").value(DEFAULT_PHONE_TO_THE_RECEIVER));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void checkFirstNameIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.setFirstName(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkLastNameIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.setLastName(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkStreetIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.street(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkPostalCodeIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.postalCode(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkCityIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.setCity(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkCountryIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.setCountry(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void checkPhoneToTheReceiverIsRequired() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    em.detach(updatedShipmentCart);
    updatedShipmentCart.setPhoneToTheReceiver(null);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put("/api/shipment-cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingShipmentCart() throws Exception {
    // Get the shipmentCart
    restShipmentCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void userShouldNotEditAnotherShipmentCartOfAnotherUser() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    updatedShipmentCart.setLastName(UPDATED_LAST_NAME);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);
    shipmentCartDto.setId(shipmentCart2.getId());

    restShipmentCartMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
    ShipmentCart testShipmentCart = shipmentCartList.get(shipmentCartList.size() - 1);
    assertThat(testShipmentCart.getId()).isEqualTo(shipmentCart2.getId());
    assertThat(testShipmentCart.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME_2);
    assertThat(testShipmentCart.getLastName()).isEqualTo(DEFAULT_LAST_NAME_2);
    assertThat(testShipmentCart.getStreet()).isEqualTo(DEFAULT_STREET_2);
    assertThat(testShipmentCart.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE_2);
    assertThat(testShipmentCart.getCity()).isEqualTo(DEFAULT_CITY_2);
    assertThat(testShipmentCart.getCountry()).isEqualTo(DEFAULT_COUNTRY_2);
    assertThat(testShipmentCart.getFirm()).isEqualTo(DEFAULT_FIRM_2);
    assertThat(testShipmentCart.getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER_2);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void putNewShipmentCart() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentCartRepository.findAll().size();
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    updatedShipmentCart.setFirstName(UPDATED_FIRST_NAME);
    updatedShipmentCart.setLastName(UPDATED_LAST_NAME);
    updatedShipmentCart.setStreet(UPDATED_STREET);
    updatedShipmentCart.setPostalCode(UPDATED_POSTAL_CODE);
    updatedShipmentCart.setCity(UPDATED_CITY);
    updatedShipmentCart.setCountry(UPDATED_COUNTRY);
    updatedShipmentCart.setPhoneToTheReceiver(UPDATED_PHONE_TO_THE_RECEIVER);
    updatedShipmentCart.setFirm(UPDATED_FIRM);
    updatedShipmentCart.setTaxNumber(UPDATED_TAX_NUMBER);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    restShipmentCartMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentCartDto)))
        .andExpect(status().isOk());

    // Validate the ShipmentCart in the database
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeUpdate);
    ShipmentCart testShipmentCart = shipmentCartList.get(shipmentCartList.size() - 2);
    assertThat(testShipmentCart.getId()).isEqualTo(shipmentCart.getId());
    assertThat(testShipmentCart.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testShipmentCart.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testShipmentCart.getStreet()).isEqualTo(UPDATED_STREET);
    assertThat(testShipmentCart.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    assertThat(testShipmentCart.getCity()).isEqualTo(UPDATED_CITY);
    assertThat(testShipmentCart.getCountry()).isEqualTo(UPDATED_COUNTRY);
    assertThat(testShipmentCart.getPhoneToTheReceiver()).isEqualTo(UPDATED_PHONE_TO_THE_RECEIVER);
    assertThat(testShipmentCart.getFirm()).isEqualTo(UPDATED_FIRM);
    assertThat(testShipmentCart.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
    assertThat(testShipmentCart.getCart().getId()).isEqualTo(shipmentCart.getCart().getId());
  }
}
