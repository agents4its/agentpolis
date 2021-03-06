/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.agentpolis.simulator.visualization.visio.VisioUtils;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 * @param <E>
 */
public abstract class EntityLayer<E extends AgentPolisEntity> extends AbstractLayer{
    
    private final EntityStorage<E> entityStorage;
    
    private final boolean showStackedEntitiesCount;
    
    protected PositionUtil positionUtil;
    
    private HashMap<Point2d,ArrayList<E>> entityPositionMap;
    
    
    private static final Double DEFAULT_TEXT_MARGIN_BOTTOM = 5.0;
    
    private static final Color DEFAULT_TEXT_BACKGROUND_COLOR = Color.WHITE;
    
    
    

    public EntityLayer(EntityStorage<E> entityStorage) {
        this(entityStorage, false);
    }
    
    public EntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount) {
        this.entityStorage = entityStorage;
        this.showStackedEntitiesCount = showStackedEntitiesCount;
    }
    
    @Inject
    public void init(PositionUtil positionUtil){
        this.positionUtil = positionUtil;
    }
    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        Dimension dim = Vis.getDrawingDimension();
        
        if(showStackedEntitiesCount){
            entityPositionMap = new HashMap<>();
        }

        EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
        E entity;
        while((entity = entityIterator.getNextEntity()) != null){
            Point2d entityPosition = getEntityPosition(entity);
            
            if(showStackedEntitiesCount){
                if(!entityPositionMap.containsKey(entityPosition)){
                    entityPositionMap.put(entityPosition, new ArrayList<>());
                }
                entityPositionMap.get(entityPosition).add(entity);
                }
            else{
                drawEntity(entity, entityPosition, canvas, dim);
            }
        }
        
        if(showStackedEntitiesCount){
            for (Map.Entry<Point2d, ArrayList<E>> entry : entityPositionMap.entrySet()) {
                Point2d entityPosition = entry.getKey();
                ArrayList<E> entities = entry.getValue();
                drawEntities(entities, entityPosition, canvas, dim);
            }
        }
    }

    protected abstract Point2d getEntityPosition(E entity);
    
    protected void drawEntity(E entity, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor();
        canvas.setColor(color);
        int radius = getEntityDrawRadius();
		int width = radius * 2;

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
        }
    }
    
    protected void drawEntities(ArrayList<E> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor();
        canvas.setColor(color);
        int radius = getEntityDrawRadius();
		int width = radius * 2;

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
        }
        if(entities.size() > 1){
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                new Point((int) (x1 - DEFAULT_TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color, 
                DEFAULT_TEXT_BACKGROUND_COLOR);
        }
    }

    protected abstract Color getEntityDrawColor();

    protected abstract int getEntityDrawRadius();

}
