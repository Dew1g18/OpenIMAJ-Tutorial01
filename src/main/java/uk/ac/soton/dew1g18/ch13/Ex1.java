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

public class Ex1 {


    public static void main(String[] args ){
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


            List<FImage> testImages = DatasetAdaptors.asList(testing);

            FImage testImage = testImages.get(1);

            DisplayUtilities.display("Original", testImage);
            DoubleFV feature = eigen.extractFeature(testImage);

            FImage recon = eigen.reconstruct(feature);
            DisplayUtilities.display("Reconstruction", recon.normalise());
            /**
             * Very cool, the image is very small but yeah you can definitely see that it gets quite close to the original
             * to the point its very easily recognisable!!
             */


        }catch(FileSystemException e){
            e.printStackTrace();
        }
    }


}

