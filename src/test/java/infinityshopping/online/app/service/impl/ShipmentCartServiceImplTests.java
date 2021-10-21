package infinityshopping.online.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.InfinityshoppingApp;
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
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.ShipmentCartDTO;
import infinityshopping.online.app.service.mapper.ShipmentCartMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = InfinityshoppingApp.class)
public class ShipmentCartServiceImplTests {

  // ShipmentCart
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


  private ShipmentCart shipmentCart;

  private ShipmentCart shipmentCart2;

  private PaymentCart paymentCart;

  private PaymentCart paymentCart2;

  private User currentLoggedUser;


  @Autowired
  private ShipmentCartServiceImpl shipmentCartServiceImpl;

  @Autowired
  private ShipmentCartRepository shipmentCartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private ShipmentCartMapper shipmentCartMapper;

  @Autowired
  private EntityManager em;


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
  public void shouldFindShipmentCartById() throws Exception {
    // when
    Optional<ShipmentCartDTO> testShipmentCart
        = shipmentCartServiceImpl.findOne(shipmentCart.getId());

    // then
    assertThat(testShipmentCart.get().getId()).isEqualTo(shipmentCart.getId());
    assertThat(testShipmentCart.get().getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testShipmentCart.get().getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testShipmentCart.get().getStreet()).isEqualTo(DEFAULT_STREET);
    assertThat(testShipmentCart.get().getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    assertThat(testShipmentCart.get().getCity()).isEqualTo(DEFAULT_CITY);
    assertThat(testShipmentCart.get().getCountry()).isEqualTo(DEFAULT_COUNTRY);
    assertThat(testShipmentCart.get().getFirm()).isEqualTo(DEFAULT_FIRM);
    assertThat(testShipmentCart.get().getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldUpdateSaveShipmentCart() throws Exception {
    // given
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

    final int databaseSizeBeforeSave = shipmentCartRepository.findAll().size();

    // when
    shipmentCartServiceImpl.save(shipmentCartDto);

    // then
    assertThat(databaseSizeBeforeSave).isEqualTo(databaseSizeBeforeSave);
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    ShipmentCart testShipmentCart = shipmentCartList.get(shipmentCartList.size() - 2);
    assertThat(databaseSizeBeforeSave).isEqualTo(databaseSizeBeforeSave);
    assertThat(testShipmentCart.getId()).isEqualTo(shipmentCart.getId());
    assertThat(testShipmentCart.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testShipmentCart.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testShipmentCart.getStreet()).isEqualTo(UPDATED_STREET);
    assertThat(testShipmentCart.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    assertThat(testShipmentCart.getCity()).isEqualTo(UPDATED_CITY);
    assertThat(testShipmentCart.getCountry()).isEqualTo(UPDATED_COUNTRY);
    assertThat(testShipmentCart.getFirm()).isEqualTo(UPDATED_FIRM);
    assertThat(testShipmentCart.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);

    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getShipmentCart().getCart().getId())
        .isEqualTo(shipmentCart.getCart().getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSaveCartIdInShipmentCartAfterSavingShipmentCart() throws Exception {
    // given
    ShipmentCart updatedShipmentCart = shipmentCartRepository.findById(shipmentCart.getId()).get();
    updatedShipmentCart.setFirstName(UPDATED_FIRST_NAME);
    ShipmentCartDTO shipmentCartDto = shipmentCartMapper.toDto(updatedShipmentCart);

    // when
    shipmentCartServiceImpl.save(shipmentCartDto);

    //then
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getShipmentCart().getCart().getId())
        .isEqualTo(shipmentCart.getCart().getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void loggedUserShouldGetOnlyOneOwnShipmentCart() throws Exception {
    // given
    currentLoggedUser = checkIfUserExist();

    // when
    Optional<ShipmentCartDTO> testShipmentCart = shipmentCartServiceImpl.findByCartId();

    // then
    assertThat(testShipmentCart.get().getId())
        .isEqualTo(currentLoggedUser.getCart().getShipmentCart().getId());
    assertThat(testShipmentCart.get().getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testShipmentCart.get().getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testShipmentCart.get().getStreet()).isEqualTo(DEFAULT_STREET);
    assertThat(testShipmentCart.get().getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    assertThat(testShipmentCart.get().getCity()).isEqualTo(DEFAULT_CITY);
    assertThat(testShipmentCart.get().getCountry()).isEqualTo(DEFAULT_COUNTRY);
    assertThat(testShipmentCart.get().getFirm()).isEqualTo(DEFAULT_FIRM);
    assertThat(testShipmentCart.get().getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER);
    assertThat(testShipmentCart.get().getId())
        .isEqualTo(currentLoggedUser.getCart().getShipmentCart().getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void everyUserShouldHaveOnlyOneShipmentCart() throws Exception {
    // given
    final int databaseSizeBeforeCreate = shipmentCartRepository.findAll().size();
    currentLoggedUser = checkIfUserExist();

    // when
    shipmentCartRepository.save(currentLoggedUser.getCart().getShipmentCart());

    // then
    List<ShipmentCart> shipmentCartList = shipmentCartRepository.findAll();
    assertThat(shipmentCartList).hasSize(databaseSizeBeforeCreate);
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}
