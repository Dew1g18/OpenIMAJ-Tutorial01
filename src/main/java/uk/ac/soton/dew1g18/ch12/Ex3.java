package uk.ac.soton.dew1g18.ch12;

import de.bwaldvogel.liblinear.SolverType;
import org.openimaj.data.DataSource;
import org.openimaj.data.dataset.Dataset;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.experiment.dataset.sampling.GroupedUniformRandomisedSampler;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.evaluation.classification.ClassificationEvaluator;
import org.openimaj.experiment.evaluation.classification.ClassificationResult;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMAnalyser;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMResult;
import org.openimaj.feature.DiskCachingFeatureExtractor;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.feature.SparseIntFV;
import org.openimaj.feature.local.data.LocalFeatureListDataSource;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.feature.dense.gradient.dsift.ByteDSIFTKeypoint;
import org.openimaj.image.feature.dense.gradient.dsift.DenseSIFT;
import org.openimaj.image.feature.dense.gradient.dsift.PyramidDenseSIFT;
import org.openimaj.image.feature.local.aggregate.BagOfVisualWords;
import org.openimaj.image.feature.local.aggregate.BlockSpatialAggregator;
import org.openimaj.image.feature.local.aggregate.PyramidSpatialAggregator;
import org.openimaj.io.IOUtils;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator;
import org.openimaj.ml.clustering.ByteCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.ByteKMeans;
import org.openimaj.ml.kernel.HomogeneousKernelMap;
import org.openimaj.util.pair.IntFloatPair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ex3{



    public static void main(String[] args) {

        try {

            GroupedDataset<String, VFSListDataset<Caltech101.Record<FImage>>, Caltech101.Record<FImage>> allData =
                    Caltech101.getData(ImageUtilities.FIMAGE_READER);

//            GroupedDataset<String, ListDataset<Caltech101.Record<FImage>>, Caltech101.Record<FImage>> data =
//                    GroupSampler.sample(allData, 101, false);

            System.out.println(allData.size());

            GroupedRandomSplitter<String, Caltech101.Record<FImage>> splits =
                    new GroupedRandomSplitter<String, Caltech101.Record<FImage>>(allData, 15, 0, 15);



            DenseSIFT dsift = new DenseSIFT(3, 7);
            PyramidDenseSIFT<FImage> pdsift = new PyramidDenseSIFT<FImage>(dsift, 6f, 4, 6,8,10);

            HardAssigner<byte[], float[], IntFloatPair> assigner =
                    trainQuantiser(GroupedUniformRandomisedSampler.sample(splits.getTrainingDataset(), 600), pdsift);

            HomogeneousKernelMap hkm = new HomogeneousKernelMap(HomogeneousKernelMap.KernelType.Chi2, HomogeneousKernelMap.WindowType.Rectangular);


            /**
             * Ex3
             *
             * This is all I will be working on in this class
             *
             * So, this took about 60 minutes to run and no output, and then I tried running it again and I got
             * an out of memory error. I dont think that my laptop can handle this but I'd say that this says when
             * you dont do a sensible sample performance takes a huge hit. Which makes sense I suppose.
             *
             *
             */



            IOUtils.writeToFile(assigner,
                    new File("C:\\Users\\davew\\OpenIMAJ-Tutorial01\\src\\main\\java\\uk\\ac\\soton\\dew1g18\\ch12\\features\\cache.txt") );



            HardAssigner<byte[], float[], IntFloatPair> assignerRED = IOUtils.readFromFile(
                    new File("C:\\Users\\davew\\OpenIMAJ-Tutorial01\\src\\main\\java\\uk\\ac\\soton\\dew1g18\\ch12\\features\\cache.txt"));


            FeatureExtractor<DoubleFV, Caltech101.Record<FImage>> extractor =  new PHOWExtractor(pdsift, assignerRED);


            FeatureExtractor<DoubleFV, Caltech101.Record<FImage>> ex2 = hkm.createWrappedExtractor(extractor);

//            HomogeneousKernelMap.ExtractorWrapper<PHOWExtractor> Hphow = new HomogeneousKernelMap.ExtractorWrapper(extractor, hkm);
//

            DiskCachingFeatureExtractor dcache = new DiskCachingFeatureExtractor(
                    new File("C:\\Users\\davew\\OpenIMAJ-Tutorial01\\src\\main\\java\\uk\\ac\\soton\\dew1g18\\ch12\\features\\cache.txt"),
                    ex2);




            LiblinearAnnotator<Caltech101.Record<FImage>, String> ann = new LiblinearAnnotator<Caltech101.Record<FImage>, String>(
                    dcache, LiblinearAnnotator.Mode.MULTICLASS, SolverType.L2R_L2LOSS_SVC, 1.0, 0.00001);
            ann.train(splits.getTrainingDataset());

            ClassificationEvaluator<CMResult<String>, String, Caltech101.Record<FImage>> eval =
                    new ClassificationEvaluator<CMResult<String>, String, Caltech101.Record<FImage>>(
                            ann, splits.getTestDataset(), new CMAnalyser<Caltech101.Record<FImage>, String>(CMAnalyser.Strategy.SINGLE));

            Map<Caltech101.Record<FImage>, ClassificationResult<String>> guesses = eval.evaluate();
            CMResult<String> result = eval.analyse(guesses);
            System.out.println(result.getSummaryReport());




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static HardAssigner<byte[], float[], IntFloatPair> trainQuantiser(
            Dataset<Caltech101.Record<FImage>> sample, PyramidDenseSIFT<FImage> pdsift)
    {
        List<LocalFeatureList<ByteDSIFTKeypoint>> allkeys = new ArrayList<LocalFeatureList<ByteDSIFTKeypoint>>();

        for (Caltech101.Record<FImage> rec : sample) {
            FImage img = rec.getImage();

            pdsift.analyseImage(img);
            allkeys.add(pdsift.getByteKeypoints(0.005f));
        }

        if (allkeys.size() > 10000)
            allkeys = allkeys.subList(0, 10000);

        ByteKMeans km = ByteKMeans.createKDTreeEnsemble(300);
        DataSource<byte[]> datasource = new LocalFeatureListDataSource<ByteDSIFTKeypoint, byte[]>(allkeys);
        ByteCentroidsResult result = km.cluster(datasource);

        return result.defaultHardAssigner();
    }

    static class PHOWExtractor implements FeatureExtractor<DoubleFV, Caltech101.Record<FImage>> {
        PyramidDenseSIFT<FImage> pdsift;
        HardAssigner<byte[], float[], IntFloatPair> assigner;

        public PHOWExtractor(PyramidDenseSIFT<FImage> pdsift, HardAssigner<byte[], float[], IntFloatPair> assigner)
        {
            this.pdsift = pdsift;
            this.assigner = assigner;
        }

        public DoubleFV extractFeature(Caltech101.Record<FImage> object) {
            FImage image = object.getImage();
            pdsift.analyseImage(image);

            BagOfVisualWords<byte[]> bovw = new BagOfVisualWords<byte[]>(assigner);

            PyramidSpatialAggregator <byte[], SparseIntFV> spatial = new PyramidSpatialAggregator<byte[], SparseIntFV>(
                    bovw, 2, 4);

            return spatial.aggregate(pdsift.getByteKeypoints(0.015f), image.getBounds()).normaliseFV();
        }
    }


}
