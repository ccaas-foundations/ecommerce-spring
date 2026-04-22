package dev.revature.services;

import dev.revature.models.Warehouse;
import dev.revature.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(int id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(int id, Warehouse warehouseDetails) {
        Optional<Warehouse> existingWarehouse = warehouseRepository.findById(id);
        if (existingWarehouse.isPresent()) {
            Warehouse warehouse = existingWarehouse.get();
            warehouse.setName(warehouseDetails.getName());
            warehouse.setLocation(warehouseDetails.getLocation());
            warehouse.setShippingRegion(warehouseDetails.getShippingRegion());
            warehouse.setItemsInStock(warehouseDetails.getItemsInStock());
            return warehouseRepository.save(warehouse);
        }
        return null;
    }

    public boolean deleteWarehouse(int id) {
        if (warehouseRepository.existsById(id)) {
            warehouseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Warehouse> getWarehousesByShippingRegion(dev.revature.models.ShippingRegion shippingRegion) {
        return warehouseRepository.findByShippingRegion(shippingRegion);
    }

    public List<Warehouse> getWarehousesByShippingRegionAndSku(dev.revature.models.ShippingRegion shippingRegion, String sku) {
        return warehouseRepository.findByShippingRegionAndSku(shippingRegion, sku);
    }
}
