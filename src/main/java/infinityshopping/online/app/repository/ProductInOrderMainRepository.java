package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.ProductInOrderMain;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ProductInOrderMainRepository extends JpaRepository<ProductInOrderMain, Long> {
  Page<ProductInOrderMain> findByOrderMainId(Long orderMainId, Pageable pageable);

  Optional<Product> findByProductId(Long productId);
}
