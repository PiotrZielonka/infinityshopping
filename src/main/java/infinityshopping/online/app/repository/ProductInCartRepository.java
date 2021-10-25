package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.ProductInCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {

  Page<ProductInCart> findByCartId(Long cartId, Pageable pageable);
}
