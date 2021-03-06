package uk.ac.soton.dew1g18.ch6;


import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.FlickrAPIToken;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        try {
//            VFSListDataset<MBFImage> images =
//                    new VFSListDataset<MBFImage>("C:/Users/davew/Pictures/Image_test_set", ImageUtilities.MBFIMAGE_READER);

//            System.out.println(images.size());

//            DisplayUtilities.display(images.getRandomInstance(), "A random image from the dataset");

//            DisplayUtilities.display("My images", images);


//            VFSListDataset<FImage> faces =
//                    new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
//            DisplayUtilities.display("ATT faces", faces);

//            VFSGroupDataset<FImage> groupedFaces =
//                    new VFSGroupDataset<FImage>( "zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);

//            for (final Map.Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
//                DisplayUtilities.display(entry.getKey(), entry.getValue());
//            }

            /**
             * commented out the above as I dont need it to run every time, poor laptop
             */

            FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
            FlickrImageDataset<FImage> cats =
                    FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken, "cat", 10);
            DisplayUtilities.display("Cats", cats);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
