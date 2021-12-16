package com.project.project2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.*;
import java.lang.*;
import java.io.IOException;
import org.apache.commons.io.*;
//import org.apache.commons.lang3.CharEncoding;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import java.nio.channels.Channel;
import com.google.gson.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.*;
import static org.apache.commons.io.FileUtils.*;
import static org.apache.tomcat.util.http.fileupload.FileUtils.*;
import javax.persistence.*;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


@RestController
public class ProjectController {

    @Autowired
    private InventoryTool inventoryTool;

    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        inventoryTool.create(newVehicle);
        return newVehicle;
    }

    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {

        return inventoryTool.getVehicle(id);
    }
    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        System.out.println(newVehicle.toString());
        int id = newVehicle.getId();
        Vehicle updateVehicle= inventoryTool.getVehicle(id);
        updateVehicle.setMakeModel(newVehicle.getMakeModel());
        updateVehicle.setYear(newVehicle.getYear());
        updateVehicle.setRetailPrice(newVehicle.getRetailPrice());
        inventoryTool.updateVehicle(newVehicle);

        return updateVehicle;
    }
    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {

        Vehicle returnV = inventoryTool.getVehicle(id);
        inventoryTool.deleteVehicle(id);
        return new ResponseEntity<String>(returnV.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException {
        List<Integer> ids = inventoryTool.getMaxIds();

        List inventory;
        Vehicle newVehicle = new Vehicle();

        if(ids.size()>10){
            inventory = new ArrayList<Vehicle>();
            int save = ids.size()-11;
            for(int i=ids.size()-1;i>save;i--){
                inventory.add(inventoryTool.getVehicle(ids.get(i)));
            }
        }else{
            inventory = new ArrayList<Vehicle>();
            for(int i=ids.size()-1;i>=0;i--){
                inventory.add(inventoryTool.getVehicle(ids.get(i)));
            }
        }

        return inventory;
    }
}

