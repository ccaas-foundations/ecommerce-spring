package dev.revature.services;

import dev.revature.models.ShippingRegion;
import dev.revature.models.Warehouse;
import dev.revature.repositories.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    private Warehouse testWarehouse;
    private List<Warehouse> testWarehouses;

    @BeforeEach
    void setUp() {
        testWarehouse = new Warehouse("Test Warehouse", "Test Location", ShippingRegion.NORTHEAST);
        testWarehouse.setId(1);
        testWarehouse.getItemsInStock().add("SKU001");

        Warehouse warehouse2 = new Warehouse("Test Warehouse 2", "Test Location 2", ShippingRegion.NORTHEAST);
        warehouse2.setId(2);
        warehouse2.getItemsInStock().add("SKU002");

        testWarehouses = Arrays.asList(testWarehouse, warehouse2);
    }

    @Test
    void getAllWarehouses_ShouldReturnAllWarehouses() {
        when(warehouseRepository.findAll()).thenReturn(testWarehouses);

        List<Warehouse> result = warehouseService.getAllWarehouses();

        assertEquals(2, result.size());
        assertEquals(testWarehouses, result);
        verify(warehouseRepository, times(1)).findAll();
    }

    @Test
    void getWarehouseById_WhenWarehouseExists_ShouldReturnWarehouse() {
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));

        Optional<Warehouse> result = warehouseService.getWarehouseById(1);

        assertTrue(result.isPresent());
        assertEquals(testWarehouse, result.get());
        verify(warehouseRepository, times(1)).findById(1);
    }

    @Test
    void getWarehouseById_WhenWarehouseDoesNotExist_ShouldReturnEmpty() {
        when(warehouseRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Warehouse> result = warehouseService.getWarehouseById(999);

        assertFalse(result.isPresent());
        verify(warehouseRepository, times(1)).findById(999);
    }

    @Test
    void createWarehouse_ShouldReturnSavedWarehouse() {
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);

        Warehouse result = warehouseService.createWarehouse(testWarehouse);

        assertEquals(testWarehouse, result);
        verify(warehouseRepository, times(1)).save(testWarehouse);
    }

    @Test
    void updateWarehouse_WhenWarehouseExists_ShouldReturnUpdatedWarehouse() {
        Warehouse updatedWarehouse = new Warehouse("Updated Name", "Updated Location", ShippingRegion.MIDWEST);
        updatedWarehouse.getItemsInStock().add("SKU003");

        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(updatedWarehouse);

        Warehouse result = warehouseService.updateWarehouse(1, updatedWarehouse);

        assertNotNull(result);
        verify(warehouseRepository, times(1)).findById(1);
        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }

    @Test
    void updateWarehouse_WhenWarehouseDoesNotExist_ShouldReturnNull() {
        Warehouse updatedWarehouse = new Warehouse("Updated Name", "Updated Location", ShippingRegion.WEST);

        when(warehouseRepository.findById(999)).thenReturn(Optional.empty());

        Warehouse result = warehouseService.updateWarehouse(999, updatedWarehouse);

        assertNull(result);
        verify(warehouseRepository, times(1)).findById(999);
        verify(warehouseRepository, never()).save(any(Warehouse.class));
    }

    @Test
    void deleteWarehouse_WhenWarehouseExists_ShouldReturnTrue() {
        when(warehouseRepository.existsById(1)).thenReturn(true);
        doNothing().when(warehouseRepository).deleteById(1);

        boolean result = warehouseService.deleteWarehouse(1);

        assertTrue(result);
        verify(warehouseRepository, times(1)).existsById(1);
        verify(warehouseRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteWarehouse_WhenWarehouseDoesNotExist_ShouldReturnFalse() {
        when(warehouseRepository.existsById(999)).thenReturn(false);

        boolean result = warehouseService.deleteWarehouse(999);

        assertFalse(result);
        verify(warehouseRepository, times(1)).existsById(999);
        verify(warehouseRepository, never()).deleteById(anyInt());
    }

    /*
     * These test are passing but they're not really meaningfully testing the methods that they're targeting
     */
//    @Test
//    void getWarehousesByShippingRegion_ShouldReturnFilteredWarehouses() {
//        List<Warehouse> northeastWarehouses = Arrays.asList(testWarehouse);
//        when(warehouseRepository.findByShippingRegion(ShippingRegion.NORTHEAST))
//                .thenReturn(northeastWarehouses);
//
//        List<Warehouse> result = warehouseService.getWarehousesByShippingRegion(ShippingRegion.NORTHEAST);
//
//        assertEquals(1, result.size());
//        assertEquals(northeastWarehouses, result);
//        verify(warehouseRepository, times(1)).findByShippingRegion(ShippingRegion.NORTHEAST);
//    }
//
//    @Test
//    void getWarehousesByShippingRegionAndSku_ShouldReturnFilteredWarehouses() {
//        when(warehouseRepository.findByShippingRegionAndSku(ShippingRegion.WEST, "SKU001"))
//                .thenReturn(testWarehouses);
//
//        List<Warehouse> result = warehouseService.getWarehousesByShippingRegionAndSku(
//                ShippingRegion.WEST, "SKU001");
//
//        assertEquals(testWarehouses, result);
//        verify(warehouseRepository, times(1)).findByShippingRegionAndSku(
//                ShippingRegion.WEST, "SKU001");
//    }
}
