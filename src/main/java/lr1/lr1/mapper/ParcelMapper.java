package lr1.lr1.mapper;

import lr1.lr1.exception.*;
import lr1.lr1.dto.ParcelDTO;
import lr1.lr1.model.Parcel;
import lr1.lr1.model.Warehouse;
import lr1.lr1.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@Component
public class ParcelMapper {
    private final WarehouseRepository warehouseRepository;

    public ParcelMapper(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Parcel toEntity(ParcelDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Кімната не знайдена"));

        return new Parcel(dto.getId(), dto.getName(), dto.getType(), dto.isStatus(), warehouse);
    }

    public ParcelDTO toDTO(Parcel parcel) {
        return new ParcelDTO(parcel.getId(), parcel.getName(), parcel.getType(), parcel.isStatus(), parcel.getWarehouse().getId());
    }
}
