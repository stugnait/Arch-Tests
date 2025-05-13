package lr1.lr1;

import com.fasterxml.jackson.databind.ObjectMapper;
import lr1.lr1.model.Warehouse;
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
public class WarehouseControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        warehouseRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/warehouses створює склад")
    void testCreateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Тестовий");
        warehouse.setLocation("Київ");
        warehouse.setCapacity(1000);

        mockMvc.perform(post("/api/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Тестовий"));
    }

    @Test
    @DisplayName("GET /api/warehouses/{id} повертає склад")
    void testGetWarehouse() throws Exception {
        Warehouse w = new Warehouse();
        w.setName("Склад 1");
        w.setLocation("Київ");
        w.setCapacity(1000);
        w = warehouseRepository.save(w);

        mockMvc.perform(get("/api/warehouses/" + w.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Склад 1"));
    }

    @Test
    @DisplayName("PUT /api/warehouses/{id} оновлює склад")
    void testUpdateWarehouse() throws Exception {
        Warehouse w = new Warehouse();
        w.setName("Новий");
        w.setLocation("Київ");
        w.setCapacity(1000);
        w = warehouseRepository.save(w);

        mockMvc.perform(put("/api/warehouses/" + w.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(w)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новий"));
    }

    @Test
    @DisplayName("DELETE /api/warehouses/{id} видаляє склад")
    void testDeleteWarehouse() throws Exception {
        Warehouse w = new Warehouse();
        w.setName("Склад");
        w.setLocation("Київ");
        w.setCapacity(1000);
        w = warehouseRepository.save(w);

        mockMvc.perform(delete("/api/warehouses/" + w.getId()))
                .andExpect(status().isNoContent());

        assertThat(warehouseRepository.findById(w.getId())).isEmpty();
    }
}
