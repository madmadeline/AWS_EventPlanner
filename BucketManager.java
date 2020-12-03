package caml.group.demo.model;

public class BucketManager {
	
	// Should have other way of doing this.
	public static final String bucket = "3733camlgroupproject";
	
	public static final String folder = "Choices";
	public static final String test_folder = "test_choices";
	
	static String cachedResult = null;   // compute once and then retain
	
	/**
	 * Returns the folder to use, either production one (folder) or testing (test_folder).
	 */
	public static String getChoicesFolder() {
		if (cachedResult != null) { return cachedResult; }    // won't change
		
		String test = System.getenv("lambdaTesting");
		if (test != null) {
			cachedResult = test_folder;
			System.out.println("USING TEST");
		} else {
			cachedResult = folder;
		}
		
		return cachedResult;
	}
	
}
