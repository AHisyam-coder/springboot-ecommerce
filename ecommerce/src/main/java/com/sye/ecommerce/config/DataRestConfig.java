package com.sye.ecommerce.config;

import com.sye.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST,
                                           HttpMethod.DELETE, HttpMethod.PATCH};

        disableHttpMethods(Product.class, config, unsupportedActions);
        disableHttpMethods(ProductCategory.class, config, unsupportedActions);
        disableHttpMethods(Country.class, config, unsupportedActions);
        disableHttpMethods(State.class, config, unsupportedActions);
        disableHttpMethods(Order.class, config, unsupportedActions);

        //call an internal helper method
        exposeIds(config);

        //configure cors mapping
        cors.addMapping(config.getBasePath()+"/**").allowedOrigins(allowedOrigins);
    }

    private static void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity ids

        //get a list of all entity classes from entity manager
        Set<EntityType<?>> entityTypeSet = entityManager.getMetamodel().getEntities();

        //create array of entity types
        List<Class> classes = new ArrayList<>();

        //get entity types for entities
        for(EntityType entityType :entityTypeSet){
            classes.add(entityType.getJavaType());
        }

        // expose entity ids for array of entity domain types
        Class[] domainTypes = classes.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }

}
