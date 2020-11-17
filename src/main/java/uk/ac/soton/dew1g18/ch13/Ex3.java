package uk.ac.soton.dew1g18.ch13;

import org.apache.commons.vfs2.FileSystemException;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.model.EigenImages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex3 {


    public static void main(String[] args) {

        try {


            VFSGroupDataset<FImage> dataset =
                    new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);

            int nTraining = 5;
            int nTesting = 5;
            GroupedRandomSplitter<String, FImage> splits =
                    new GroupedRandomSplitter<String, FImage>(dataset, nTraining, 0, nTesting);
            GroupedDataset<String, ListDataset<FImage>, FImage> training = splits.getTrainingDataset();
            GroupedDataset<String, ListDataset<FImage>, FImage> testing = splits.getTestDataset();

            List<FImage> basisImages = DatasetAdaptors.asList(training);
            int nEigenvectors = 100;
            EigenImages eigen = new EigenImages(nEigenvectors);
            eigen.train(basisImages);

            List<FImage> eigenFaces = new ArrayList<FImage>();
            for (int i = 0; i < 12; i++) {
                eigenFaces.add(eigen.visualisePC(i));
            }
            DisplayUtilities.display("EigenFaces", eigenFaces);


            Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();
            for (final String person : training.getGroups()) {
                final DoubleFV[] fvs = new DoubleFV[nTraining];

                for (int i = 0; i < nTraining; i++) {
                    final FImage face = training.get(person).get(i);
                    fvs[i] = eigen.extractFeature(face);
                }
                features.put(person, fvs);
            }


            double correct = 0, incorrect = 0;
            for (String truePerson : testing.getGroups()) {
                for (FImage face : testing.get(truePerson)) {
                    DoubleFV testFeature = eigen.extractFeature(face);

                    String bestPerson = null;

                    /**
                     * The best way I can see to implement a threshold is to set the minDistance to something
                     * less than the effective infinity that is Double's max vvalue.
                     *
                     * The value for thie threshold is harder to come up with. My immediate assumption would be to find
                     * the max delta of that feature from the training data and hope that the training data accounts for
                     * extremes.
                     *
                     * Another nice idea would be the magnitude of the feature vector it's checking as if its further
                     * from the feature than the feature is from 0 surely they're not close enough to be considered similar
                     *
                     * This second one is a lot easier to do as far as I can see for now.
                     *
                     * I feel that there is probably a better (smaller) value which can be chosen for the threshold but
                     * this one would certainly work to an extent, even if it might be slightly useless.
                     *
                     */
                    double minDistance = Double.MAX_VALUE;
                    for (final String person : features.keySet()) {
                        for (final DoubleFV fv : features.get(person)) {
                            double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);

                            DoubleFV zero = new DoubleFV(fv.length());
                            double threshold = fv.compare(zero, DoubleFVComparison.EUCLIDEAN);


                            if (distance < minDistance && distance<threshold) {
                                minDistance = distance;
                                bestPerson = person;
                            }
                        }
                    }

                    System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);

                    if (truePerson.equals(bestPerson))
                        correct++;
                    else
                        incorrect++;
                }
            }

            System.out.println("Accuracy: " + (correct / (correct + incorrect)));


        } catch (FileSystemException e) {
            e.printStackTrace();
        }

    }
}
