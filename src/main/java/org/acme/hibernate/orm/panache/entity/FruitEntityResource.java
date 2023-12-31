package org.acme.hibernate.orm.panache.entity;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.acme.hibernate.orm.panache.repository.Fruit;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.panache.common.Sort;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;


@Path("entity/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitEntityResource {

    private static final Logger LOGGER = Logger.getLogger(FruitEntityResource.class.getName());

    @GET
    public List<FruitEntity> get() {
        return FruitEntity.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public FruitEntity getSingle(Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    //BreaultS: paging example
    @GET
    @Path("page/{itemCount}")
    public List<FruitEntity> getPageOfFruit(Integer itemCount) {
        PanacheQuery<FruitEntity> fruits = FruitEntity.findAll();
        
        if(fruits != null){
            fruits.page(Page.ofSize(itemCount));

            return fruits.list();
        }else{
            //error
            throw new WebApplicationException("Page item count " + itemCount + " invalid.", 404);
        }
    }   
    
    //BreaultS: sort by sku example
    @GET
    @Path("bysku")    
    public List<FruitEntity> getAllSortBySku(){
        return FruitEntity.listAll(Sort.by("sku"));
    }

    //BreaultS: range example
    @GET
    @Path("range/{maxItemId}")
    public List<FruitEntity> getRangeOfFruit(Integer maxItemId) {
        PanacheQuery<FruitEntity> fruits = FruitEntity.findAll();
        
        if(fruits != null){
            fruits.range(0, maxItemId - 1); //subtract 1 because of zero indexing

            return fruits.list();
        }else{
            //error
            throw new WebApplicationException("Max item id " + maxItemId + " invalid.", 404);
        }
    }  

    //BreaultS: named query example
    @GET
    @Path("name/{name}")
    public FruitEntity getSingleByQuery(String name) {
        FruitEntity entity = FruitEntity.findByName(name);
        if (entity == null) {
            throw new WebApplicationException("Fruit with name of " + name + " does not exist.", 404);
        }
        return entity;
    }    

    @POST
    @Transactional
    public Response create(FruitEntity fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        if(fruit.sku == null) fruit.sku = java.util.UUID.randomUUID().toString().substring(0, 8);

        fruit.persist();
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public FruitEntity update(Long id, Fruit fruit) {
        if (fruit.name == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        FruitEntity entity = FruitEntity.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.name = fruit.name;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
