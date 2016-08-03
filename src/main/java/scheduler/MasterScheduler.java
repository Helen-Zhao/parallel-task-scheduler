package scheduler;

public class MasterScheduler {
	private static MasterScheduler masterScheduler = new MasterScheduler();
	
	private MasterScheduler() {}
	
	private static MasterScheduler getInstance() {
		if(masterScheduler == null) {
			masterScheduler = new MasterScheduler();
		}
		
		return masterScheduler;
	}
}
