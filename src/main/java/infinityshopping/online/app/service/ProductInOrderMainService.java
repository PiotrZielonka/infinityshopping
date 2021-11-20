package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.ProductInOrderMainDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProductInOrderMainService {

  ProductInOrderMainDTO save(ProductInOrderMainDTO productInOrderMainDto);

  List<ProductInOrderMainDTO> findByOrderMainId(Long id, Pageable pageable);

  Optional<ProductInOrderMainDTO> findOne(Long id);

  void delete(Long id);
}
