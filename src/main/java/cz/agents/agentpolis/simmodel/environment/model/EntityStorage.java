package cz.agents.agentpolis.simmodel.environment.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;

/**
 * The general storage for entity in a simulation model (e.g. for vehicle,
 * agents).
 * 
 * */
@Singleton
public class EntityStorage<TEntity extends AgentPolisEntity> {

    private final Map<String, TEntity> entities;
    private final Map<EntityType, Set<String>> entitiesByType;

    public EntityStorage(Map<String, TEntity> entities, Map<EntityType, Set<String>> entitiesByType) {
        super();
        this.entities = entities;
        this.entitiesByType = entitiesByType;
    }

    /**
     * 
     * Adds entity into the storage, its id has to be unique
     * 
     * @param entity
     */
    public void addEntity(TEntity entity) {
        checkArgument(entities.containsKey(entity.getId()) == false, "Duplicate entity i storage: "
                + entity.getId());

        entities.put(entity.getId(), entity);

        EntityType type = entity.getType();
        if (!entitiesByType.containsKey(type)) {
            entitiesByType.put(type, new HashSet<String>());
        }

        entitiesByType.get(type).add(entity.getId());
    }

    /**
     * 
     * Returns entity base on given id
     * 
     * @param entityId
     * @return
     */
    public TEntity getEntityById(String entityId) {
        return entities.get(checkNotNull(entityId));
    }

    /** Returns all entity ids */
    public ImmutableSet<String> getEntityIds() {
        return ImmutableSet.copyOf(entities.keySet());
    }

}
