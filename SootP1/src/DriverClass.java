
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.Transform;
import soot.options.Options;


public class DriverClass {
	
	private static final BodyTransformer analysis = new Wrapper();
	private static final String SRC_DIR = "D:\\KeplerWorkspace\\SootP1\\src;";

	/**
	 * Main function to run to start the analysis.
	 * 
	 * @param args
	 *            such that args[0] is the test class.
	 */
	public static void main(String[] args) {
		// Check the length of the arguments.
		if (args.length < 1) 
		{
			throw new IllegalArgumentException(
					"The name of the test class was not provided.");
		}

		// Add a new phase.
		//Scene.v().loadClassAndSupport("Wrapper");
		PackManager.v().getPack("jtp").add(new Transform("jtp.instrumenter", DriverClass.analysis));

		// Set options to keep the original variable names, output Jimple, use
		// the Java source and update the class path.
		Options.v().setPhaseOption("jb", "use-original-names:true");
		Options.v().set_output_format(Options.output_format_J);
		Options.v().set_src_prec(Options.src_prec_java);
		Scene.v().setSootClassPath(
				DriverClass.SRC_DIR + Scene.v().getSootClassPath());

		// Load the given class.
		SootClass class_analyze = Scene.v().loadClassAndSupport(args[0]);

		// Get the class name.
		//System.out.println("Loaded Class: " + class_analyze.getName() + "\n");

		// Start the analysis.
		soot.Main.main(args);
	}
}
