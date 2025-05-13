package lr1.lr1;

import com.fasterxml.jackson.databind.ObjectMapper;
import lr1.lr1.dto.ParcelDTO;
import lr1.lr1.model.Parcel;
import lr1.lr1.model.Warehouse;
import lr1.lr1.repository.ParcelRepository;
import lr1.lr1.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ParcelControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ParcelRepository parcelRepository;
    @Autowired private WarehouseRepository warehouseRepository;

    private Warehouse warehouse;

    @BeforeEach
    void setup() {
        parcelRepository.deleteAll();
        warehouseRepository.deleteAll();

        warehouse = new Warehouse();
        warehouse.setName("Тестовий склад");
        warehouse.setLocation("Київ");
        warehouse.setCapacity(1000);
        warehouse = warehouseRepository.save(warehouse);
    }

    @Test
    @DisplayName("POST /parcels створює посилку")
    void testCreateParcel() throws Exception {
        ParcelDTO dto = new ParcelDTO();
        dto.setName("Телевізор");
        dto.setWeight(8.0);
        dto.setDestination("Одеса");
        dto.setStatus("NEW");
        dto.setType("ELECTRONICS");
        dto.setWarehouseId(warehouse.getId());

        mockMvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Телевізор"));
    }

    @Test
    @DisplayName("GET /parcels повертає посилки")
    void testGetParcels() throws Exception {
        Parcel parcel = new Parcel();
        parcel.setName("Ноутбук");
        parcel.setWeight(2.5);
        parcel.setDestination("Київ");
        parcel.setStatus("NEW");
        parcel.setType("ELECTRONICS");
        parcel.setWarehouse(warehouse);
        parcelRepository.save(parcel);

        mockMvc.perform(get("/parcels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ноутбук"));
    }

    @Test
    @DisplayName("PUT /parcels/{id} оновлює посилку")
    void testUpdateParcel() throws Exception {
        Parcel parcel = new Parcel();
        parcel.setName("Документ");
        parcel.setWeight(1.0);
        parcel.setDestination("Харків");
        parcel.setStatus("NEW");
        parcel.setType("DOCUMENTS");
        parcel.setWarehouse(warehouse);
        parcel = parcelRepository.save(parcel);

        ParcelDTO dto = new ParcelDTO();
        dto.setName("Оновлений документ");
        dto.setWeight(1.2);
        dto.setDestination("Дніпро");
        dto.setStatus("SENT");
        dto.setType("DOCUMENTS");
        dto.setWarehouseId(warehouse.getId());

        mockMvc.perform(put("/parcels/" + parcel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Оновлений документ"));
    }

    @Test
    @DisplayName("DELETE /parcels/{id} видаляє посилку")
    void testDeleteParcel() throws Exception {
        Parcel parcel = new Parcel();
        parcel.setName("Старий товар");
        parcel.setWeight(3.0);
        parcel.setDestination("Запоріжжя");
        parcel.setStatus("NEW");
        parcel.setType("OTHER");
        parcel.setWarehouse(warehouse);
        parcel = parcelRepository.save(parcel);

        mockMvc.perform(delete("/parcels/" + parcel.getId()))
                .andExpect(status().isOk());

        assertThat(parcelRepository.findById(parcel.getId())).isEmpty();
    }
}
