package gui.clusterviewer;

import algorithms.clustering.clusterreader.AlgoClusterReader;
import patterns.cluster.Cluster;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * Example of how to view clusters from the source code of SPMF.
 *
 * @author Philippe Fournier-Viger, 2016.
 */
public class MainTestClusterViewerFile {

    public static void main(String[] arg) throws IOException {

        // the input file
        String input = fileToPath("clustersDBScan.txt");

        // Applying the  algorithm
        AlgoClusterReader algorithm = new AlgoClusterReader();
        List<Cluster> clusters = algorithm.runAlgorithm(input);
        List<String> attributeNames = algorithm.getAttributeNames();
        algorithm.printStats();

        ClusterViewer viewer = new ClusterViewer(clusters, attributeNames);
        viewer.setVisible(true);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestClusterViewerFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
