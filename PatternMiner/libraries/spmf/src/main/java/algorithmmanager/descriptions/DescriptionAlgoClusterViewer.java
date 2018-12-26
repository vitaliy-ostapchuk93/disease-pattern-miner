package algorithmmanager.descriptions;

import algorithmmanager.DescriptionOfAlgorithm;
import algorithmmanager.DescriptionOfParameter;
import algorithms.clustering.clusterreader.AlgoClusterReader;
import gui.clusterviewer.ClusterViewer;
import gui.timeseriesviewer.TimeSeriesViewer;
import patterns.cluster.Cluster;

import java.io.IOException;
import java.util.List;

/**
 * This class describes the algorithm to visualize a set of clusters.
 * 
 * @see TimeSeriesViewer
 * @author Philippe Fournier-Viger
 */
public class DescriptionAlgoClusterViewer extends DescriptionOfAlgorithm {

	/**
	 * Default constructor
	 */
	public DescriptionAlgoClusterViewer(){
	}

	@Override
	public String getName() {
		return "Vizualize_clusters_of_instances";
	}

	@Override
	public String getAlgorithmCategory() {
		return "CLUSTERING";
	}

	@Override
	public String getURLOfDocumentation() {
		return "http://www.philippe-fournier-viger.com/spmf/ClusterViewer.php";
	}

	@Override
	public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

		
		// Applying the  algorithm
		AlgoClusterReader algorithm = new AlgoClusterReader();
		List<Cluster> clusters = algorithm.runAlgorithm(inputFile);
		List<String> attributeNames =  algorithm.getAttributeNames();
		
		// Otherwise, we use the cluster viewer
		ClusterViewer viewer = new ClusterViewer(clusters, attributeNames);
		viewer.setVisible(true);

	}

	@Override
	public DescriptionOfParameter[] getParametersDescription() {
        
		DescriptionOfParameter[] parameters = new DescriptionOfParameter[0];
		return parameters;
	}

	@Override
	public String getImplementationAuthorNames() {
		return "Philippe Fournier-Viger";
	}

	@Override
	public String[] getInputFileTypes() {
		return new String[]{"Clusters"};
	}


	@Override
	public String[] getOutputFileTypes() {
		return null;
	}

	
}
