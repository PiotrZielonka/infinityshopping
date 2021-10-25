package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.ProductInCartDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProductInCartService {

  ProductInCartDTO save(ProductInCartDTO productInCartDto);

  Optional<ProductInCartDTO> findOne(Long id);

  List<ProductInCartDTO> findByCartId(Pageable pageable);

  void delete(Long id);
}
