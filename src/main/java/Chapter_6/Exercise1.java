package Chapter_6;

import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

import java.util.ArrayList;
import java.util.Map;

public class Exercise1 {

    public static void main(String[] args){

        try{



            VFSListDataset<FImage> faces =
                    new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
            DisplayUtilities.display("ATT faces", faces);

            VFSGroupDataset<FImage> groupedFaces =
                    new VFSGroupDataset<FImage>( "zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);


            VFSListDataset<FImage> randFaces = new VFSListDataset<FImage>("C:/Users/davew/Pictures/Empty", ImageUtilities.FIMAGE_READER);

            ArrayList<FImage> randomFaces = new ArrayList<>();
            //Constructing empty dataset

            for (final Map.Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
//                DisplayUtilities.display(entry.getKey(), entry.getValue());
                FImage rand = entry.getValue().getRandomInstance();
                randomFaces.add(rand);
//                randFaces.(rand);
            }


            DisplayUtilities.display("One Random per person", randomFaces);


            /**
             * .add wasnt liked by an empty VFSList but you can display ArrayLists which is great so... I'll be doing that a lot I guess.
             */

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
