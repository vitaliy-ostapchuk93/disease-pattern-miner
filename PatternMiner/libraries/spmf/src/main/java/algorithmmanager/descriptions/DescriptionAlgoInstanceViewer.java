package algorithmmanager.descriptions;

import algorithmmanager.DescriptionOfAlgorithm;
import algorithmmanager.DescriptionOfParameter;
import algorithms.clustering.instancereader.AlgoInstanceFileReader;
import gui.instanceviewer.InstanceViewer;
import gui.timeseriesviewer.TimeSeriesViewer;
import patterns.cluster.DoubleArray;

import java.io.IOException;
import java.util.List;

/**
 * This class describes the algorithm to visualize a time series.
 * 
 * @see TimeSeriesViewer
 * @author Philippe Fournier-Viger
 */
public class DescriptionAlgoInstanceViewer extends DescriptionOfAlgorithm {

	/**
	 * Default constructor
	 */
	public DescriptionAlgoInstanceViewer(){
	}

	@Override
	public String getName() {
		return "Vizualize_instances";
	}

	@Override
	public String getAlgorithmCategory() {
		return "CLUSTERING";
	}

	@Override
	public String getURLOfDocumentation() {
		return "http://www.philippe-fournier-viger.com/spmf/InstanceViewer.php";
	}

	@Override
	public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

		String separator;
		if (parameters.length > 0 && "".equals(parameters[0]) == false) {
			separator = getParamAsString(parameters[0]);
		}else{
			separator = " ";
		}
		
		AlgoInstanceFileReader reader = new AlgoInstanceFileReader();
		List<DoubleArray> instances = reader.runAlgorithm(inputFile, separator);
		List<String> attributes = reader.getAttributeNames();

		InstanceViewer viewer = new InstanceViewer(instances, attributes);
		viewer.setVisible(true);
	}

	@Override
	public DescriptionOfParameter[] getParametersDescription() {
        
		DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
		parameters[0] = new DescriptionOfParameter("separator", "(e.g. ',' , default: ' ')", String.class, true);
		return parameters;
	}

	@Override
	public String getImplementationAuthorNames() {
		return "Philippe Fournier-Viger";
	}

	@Override
	public String[] getInputFileTypes() {
		return new String[]{"Instances"};
	}

	@Override
	public String[] getOutputFileTypes() {
		return null;
	}

	
}
