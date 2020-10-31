package Chapter_7;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.algorithm.AnisotropicDiffusion;
import org.openimaj.image.processing.convolution.AverageBoxFilter;
import org.openimaj.image.processing.convolution.SumBoxFilter;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.edges.SUSANEdgeDetector;
import org.openimaj.image.processing.effects.DioramaEffect;
import org.openimaj.image.processing.resize.BilinearInterpolation;
import org.openimaj.math.geometry.line.Line2d;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.xuggle.XuggleVideo;

import java.io.File;
import java.net.URL;

public class App {

    public static void main(String[] args){

        try {
            Video<MBFImage> video;
//        video = new XuggleVideo(new File("/path/to/keyboardcat.flv"));


            video = new XuggleVideo(new URL("http://static.openimaj.org/media/tutorial/keyboardcat.flv"));

//            video = new VideoCapture(320, 240);
            /**
             * The above is for using a camera raher than loading a downloaded clip
             */


//            VideoDisplay<MBFImage> noEdgeDis = VideoDisplay.createVideoDisplay(video);


//            for (MBFImage mbfImage : video) {
//                DisplayUtilities.displayName(mbfImage.process(new CannyEdgeDetector()), "videoFrames");
//            }

            VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
            display.addVideoListener(
                    new VideoDisplayListener<MBFImage>() {
                        public void beforeUpdate(MBFImage frame) {
                            frame.processInplace(new SumBoxFilter(2));
                        }

                        /**
                         * Exercise 7.1
                         *
                         * Used a few edge detectors, Susan pretty good, very thick white lines displays its strong
                         * at telling what's not noise.
                         *
                         * SumBoxFilter will incresse the brightness till its a white screen moments after the video
                         * starts for any dimming value greater than 1... 2 brings it down at intervals though..
                         * Not really sure what this is supposed to do..
                         *
                         *
                         *
                         *
                         * @param display
                         */

                        public void afterUpdate(VideoDisplay<MBFImage> display) {
                        }
                    });





        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
