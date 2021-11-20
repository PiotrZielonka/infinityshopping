package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.service.dto.OrderMainDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface OrderMainMapper extends EntityMapper<OrderMainDTO, OrderMain> {

  @Named("id")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  OrderMainDTO toDtoId(OrderMain orderMain);
}
