package com.project.project2;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public class InventoryTool {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(Vehicle vehicle){
        entityManager.persist(vehicle);
        return;
    }
    public Vehicle getVehicle(int id) {
        return entityManager.find(Vehicle.class, id);
    }
    public void updateVehicle(Vehicle v){
        //entityManager.getTransaction().begin();
        entityManager.merge(v);
        return;
        //entityManager.getTransaction().commit();

    }
    public void deleteVehicle(int id){
        Vehicle v = entityManager.find(Vehicle.class, id);
        entityManager.remove(v);
        return;
    }
    public List<Integer> getMaxIds() {

        String sql = "SELECT id FROM inventory";
        List<Integer> result = entityManager.createNativeQuery(sql).getResultList();

        return result;
    }

}
