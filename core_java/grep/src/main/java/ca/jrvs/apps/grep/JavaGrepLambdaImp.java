package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp extends JavaGrepImp {
  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  public static void main(String[] args) {
    if (args.length !=3) {
      throw new IllegalArgumentException("View correct usage: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    //creating JavaGrepLambdaImp instead of JavaGrepImp
    //JavaGrepLambdaImp inherits all methods except two override methods
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      //calling the parent method, but it will call override method (in this class)
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("An error occurred: Failed to process files", ex);
    }
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<File>();
    try (Stream<Path> walk = Files.walk(Paths.get(rootDir))) {
      files = walk.filter(Files::isRegularFile).map(path -> path.toFile()).collect(Collectors.toList());
    } catch (IOException e) {
      logger.error("An error occurred: Failed to read file", e);
    }
    return files;
  }

  @Override
  public List<String> readLines(File inputFile) throws IOException{
    List<String> flines = new ArrayList<>();
    try (Stream<String> stream = Files.lines(Paths.get(inputFile.getPath()))) {
      flines = stream.collect(Collectors.toList());
    } catch (IOException e) {
      logger.error("An error occurred: Failed to retrieve files from root directory", e);
    }
    return flines;
  }
}
