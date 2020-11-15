package uk.ac.soton.dew1g18.ch8;

import com.sun.media.jai.codec.PNGEncodeParam;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FacialKeypoint;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Ellipse;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import java.awt.geom.Point2D;
import java.util.List;

public class App {


    public static void main(String[] args ) {


        try {
            VideoCapture vc = new VideoCapture(320, 240);
            VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay(vc);
            vd.addVideoListener(
                    new VideoDisplayListener<MBFImage>() {
                        public void beforeUpdate(MBFImage frame) {
//                            FaceDetector<DetectedFace,FImage> fd1 = new HaarCascadeDetector(40);
//                            List<DetectedFace> faces1 = fd1.detectFaces(Transforms.calculateIntensity(frame));
//                            for(DetectedFace face : faces1){
//                                frame.drawShape(face.getBounds(), RGBColour.GREEN);
//                            }


                            /**
                             * Adapted the second half of the tutorial into the exercise, quite simple..
                             * Took a few minutes to see that the reason there didn't seem to be any method called
                             * getKeypoints(), but then I realised I wasn't specifying that my loop was over KEDetectedFace s
                             *
                             * Once I had that sorted, I saw my keypoints being drawn in the wrong place and then
                             * realised I needed to translate them..
                             *
                             * I find it weird that the translate method doesn't return the new points..
                             */

                            FaceDetector<KEDetectedFace, FImage> fd = new FKEFaceDetector();
                            List<KEDetectedFace> faces = fd.detectFaces( Transforms.calculateIntensity( frame ) );

                            for(KEDetectedFace face: faces){

                                frame.drawShape(face.getBounds(), RGBColour.RED);

                                FacialKeypoint[] keypoints = face.getKeypoints();
                                for (FacialKeypoint kp : keypoints){
                                    Helper h = new Helper();

                                    float xOffset = h.floatFromDouble(face.getBounds().minX());
                                    float yOffset = h.floatFromDouble(face.getBounds().minY());
                                    kp.position.translate(xOffset,yOffset);
                                    frame.drawPoint(kp.position, RGBColour.GREEN ,5);
                                }
                                try{
                                    drawSpeechBubbles(face.getKeypoint(FacialKeypoint.FacialKeypointType.MOUTH_LEFT).position, frame);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
//                                  frame.drawShape();
                            }

                            /**
                             * Whilst trying to see the difference between these I decided to try running them both together,
                             * this did cause a hit to performance but it also showed me that the FKE face detector
                             * draws a smaller box around the face which shows that it has a more detailed idea of where the
                             * face is.
                             *
                             * They also would both double up on the detected faces at the same time, this was just
                             * a few frames every so often where it found a second face around the first one.
                             */

                        }


                        public void drawSpeechBubbles(Point2dImpl mouthPoint, MBFImage frame){
                            /**
                             * Wrote this method to draw the speech bubbles to keep it clean..
                             */
                            Helper h = new Helper();
                            int x = h.intFromDouble(mouthPoint.getX());
                            int y = h.intFromDouble(mouthPoint.getY());
                            for(int i = 0; i<5; i++){
                                frame.drawShapeFilled(new Ellipse(mouthPoint.getX()-i*13, mouthPoint.getY()-i*10, 5f, 5f, 0f), RGBColour.WHITE);
                            }

                            frame.drawShapeFilled(new Ellipse(x-5*13, y-5*10, 30f, 20f, 0f), RGBColour.WHITE);
                            frame.drawText("Chap 8", x-5*18, y-5*10, HersheyFont.ASTROLOGY, 10,RGBColour.BLACK);
                        }

                        public void afterUpdate(VideoDisplay<MBFImage> display) {
                        }
                    });




        }catch(VideoCaptureException e){
            e.printStackTrace();
        }

    }


}
