package infinityshopping.online.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.InfinityshoppingApp;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.CartDtoAmountOfCartGross;
import infinityshopping.online.app.service.dto.CartDtoAmountsGross;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = InfinityshoppingApp.class)
class CartServiceImplTests {

  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET = new BigDecimal("100");
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_GROSS = new BigDecimal("123");

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET = new BigDecimal("10");
  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_GROSS = new BigDecimal("10.8");

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET = new BigDecimal("110");
  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_GROSS = new BigDecimal("133.8");

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartServiceImpl cartServiceImp;

  private User currentLoggedUser;

  @BeforeEach
  public void initUsers() throws Exception {
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
    cart.setAmountOfCartNet(DEFAULT_AMOUNT_OF_CART_NET);
    cart.setAmountOfCartGross(DEFAULT_AMOUNT_OF_CART_GROSS);
    cart.setAmountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    cart.setAmountOfShipmentGross(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    cart.setAmountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET);
    cart.setAmountOfOrderGross(DEFAULT_AMOUNT_OF_ORDER_GROSS);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.saveAndFlush(user);

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
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldGetOnlyAmountsGrossOfCurrentUser() throws Exception {
    // given
    currentLoggedUser = checkIfUserExist();

    // when
    Optional<CartDtoAmountsGross> dbCart = cartServiceImp.findByUserIdAllAmountsGross();

    // then
    assertThat(dbCart.get().getId()).isEqualTo(currentLoggedUser.getCart().getId());
    assertThat(dbCart.get().getAmountOfCartGross()).isEqualTo(
        currentLoggedUser.getCart().getAmountOfCartGross());
    assertThat(dbCart.get().getAmountOfShipmentGross()).isEqualTo(
        currentLoggedUser.getCart().getAmountOfShipmentGross());
    assertThat(dbCart.get().getAmountOfOrderGross()).isEqualTo(
        currentLoggedUser.getCart().getAmountOfOrderGross());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldGetOnlyAmountOfCartGrossOfCurrentUser() throws Exception {
    // given
    currentLoggedUser = checkIfUserExist();

    // when
    Optional<CartDtoAmountOfCartGross> dbCart = cartServiceImp.findByUserIdAmountOfCartGross();

    // then
    assertThat(dbCart.get().getId()).isEqualTo(currentLoggedUser.getCart().getId());
    assertThat(dbCart.get().getAmountOfCartGross()).isEqualTo(
        currentLoggedUser.getCart().getAmountOfCartGross());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void everyUserShouldHaveOnlyOneCart() throws Exception {
    // given
    final int databaseSizeBeforeCreate = cartRepository.findAll().size();
    currentLoggedUser = checkIfUserExist();

    // when
    cartRepository.save(currentLoggedUser.getCart());

    // then
    List<Cart> cartList = cartRepository.findAll();
    assertThat(cartList).hasSize(databaseSizeBeforeCreate);
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
