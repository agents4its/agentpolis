/* 
 * AgentSCAI
 */
package cz.agents.agentpolis.simmodel;

import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.typed.AliteEntity;
import java.util.logging.Level;

/**
 * This class represent agent's activity. 
 * @author F.I.D.O.
 * @param <A> Agent type
 */
public abstract class Activity<A extends Agent> extends AliteEntity{
	
	/**
	 * Agent who performs this activity.
	 */
	protected final A agent;
    
	/**
	 * Parent activity.
	 */
    private Activity parrentActivity;
    
	/**
	 * Child activity.
	 */
    private Activity childActivity;
    
    private boolean failed;
    
    
    

    public boolean failed() {
        return failed;
    }
    
    
    
	/**
	 * Agent getter.
	 * @return Returns agent who performs this activity.
	 */
	public final A getAgent() {
		return agent;
	}
	


	/**
	 * Standard constructor
     * @param activityInitializer
	 * @param agent Agent who performs this activity.
	 */
	public Activity(ActivityInitializer activityInitializer, A agent) {
		this.agent = agent;
        init(activityInitializer.getSimulation());
        failed = false;
	}


    
    
    @Override
    public void handleEvent(Event event) {
        if(parrentActivity != null){
            parrentActivity.handleEvent(event); 
        }
    }
	
	
	/**
	 * Action logic.
	 */
	protected abstract void performAction();

	
	/**
	 * This method finishes activity and calls parent activity if it exists or agent if it is not. If your activity 
	 * should be finished, this is the rigt place.
	 */
	protected final void finish(){
        if(parrentActivity != null){
            agent.currentActivity = parrentActivity;
            parrentActivity.onChildActivityFinish(this);
			parrentActivity.childActivity = null;
        }
        else{
            agent.currentActivity = null;
            agent.onActivityFinish(this);
        }
    }
	
	/**
	 * If something gets wrong in your activity that cannot be fixed, it should call this method.
	 * @param reason Reason of the failure
	 */
    protected final void fail(String reason){
        failed = true;
        agent.onActionFailed(this, reason);
    }
	
	/**
	 * This method is called when child activity is finished.
	 * @param activity Child activity.
	 */
	protected void onChildActivityFinish(Activity activity) {
        Log.log(this, Level.FINEST, "{0}: Child action finished: {1}", this.getClass(), childActivity);
    }
	
	/**
	 * Runs child activity.
	 * @param childActivity Child activity. 
	 */
	protected final void runChildActivity(Activity childActivity){
        this.childActivity = childActivity;
        childActivity.parrentActivity = this;
        this.childActivity.run();
    }
	
	
	/**
	 * Main activity method. This method is called automaticaly by framework.
	 */   
    public final void run(){
        Log.log(this, Level.FINEST, "{0}: run() START", this.getClass());
        agent.currentActivity = this;
        runActityLogic();
        Log.log(this, Level.FINEST, "{0}: run() END", this.getClass());
    }

    void runActityLogic() {
        performAction();
    }
    
	public void processMessage(Message message){
		if(parrentActivity != null){
            parrentActivity.processMessage(message);
        }
	}
}
