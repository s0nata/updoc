package semantic;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Created by arianna on 31/07/17. */
public class GloveModelWrapper {

  private static GloveModelWrapper instance = null;

  private static WordVectors gloveTxtVectors = null;

  protected GloveModelWrapper() {
    // Exists only to defeat instantiation.
  }

  public static GloveModelWrapper getInstance() throws URISyntaxException {
    if (instance == null) {
      instance = new GloveModelWrapper();
      try {
        gloveTxtVectors = setUpGloveTxtVectors();
//        gloveTxtVectors = setUpBinVectors();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  private static WordVectors setUpGloveTxtVectors() throws Exception {
    String gloveTxtFolder = "glove-txt";
    String gloveTxtFile = "glove.6B.300d.txt";

    // Copy GloVe models in Toradocu jar to glove-txt folder and use them.
    String filePath = "/" + gloveTxtFile;
    InputStream gloveInputStream = GloveModelWrapper.class.getResourceAsStream(filePath);
    Path destinationFile = Paths.get(gloveTxtFolder, gloveTxtFile);

    Path folderPath = Paths.get(gloveTxtFolder);
    if (!Files.exists(folderPath)) {
      Files.createDirectory(folderPath);
    }
    if (Files.list(folderPath).count() == 0) {
      Files.copy(gloveInputStream, destinationFile);
    }
    WordVectors gloveVectors = null;
    try {
      File gloveFinalFile = destinationFile.toFile();
      gloveVectors = WordVectorSerializer.loadStaticModel(gloveFinalFile);
      gloveFinalFile.deleteOnExit();
      folderPath.toFile().deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gloveVectors;
  }

  private static WordVectors setUpBinVectors() throws Exception {
    String modelFolder = "google-bin";
    String modelFile = "GoogleNews-vectors-negative300.bin";

    // Copy GloVe models in Toradocu jar to glove-txt folder and use them.
    String filePath = "/" + modelFile;
    InputStream gloveInputStream = GloveModelWrapper.class.getResourceAsStream(filePath);
    Path destinationFile = Paths.get(modelFolder, modelFile);

    Path folderPath = Paths.get(modelFolder);
    if (!Files.exists(folderPath)) {
      Files.createDirectory(folderPath);
    }
    if (Files.list(folderPath).count() == 0) {
      Files.copy(gloveInputStream, destinationFile);
    }
    Word2Vec vec  = null;
    try {
      File modelFinalFile = destinationFile.toFile();
      vec = WordVectorSerializer.readWord2VecModel(modelFinalFile);
      modelFinalFile.deleteOnExit();
      folderPath.toFile().deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return vec;
  }

  public WordVectors getGloveTxtVectors() {
    return gloveTxtVectors;
  }
}
