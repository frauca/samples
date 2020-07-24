package manomano.gaming;

import org.springframework.boot.SpringApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class GamingApplication {

    public static void main(String[] args) {
        System.err.println("Just testing");
    }

    static String locateUniverseFormula() {
        File root = new File("/tmp/documents");
        return findUniverseFormula(root)
                .map(f->f.getAbsolutePath())
                .orElse(null);
    }

    static File findFormula(File file){
        Files.walk(file.toPath())
                .filter(GamingApplication::isUniverseFormula)
                .findFirst()
                .orElse(null);
        if(file.isDirectory()){
            return Arrays.stream(file.listFiles())
                    .map(f -> Optional.ofNullable(
                            findFormula(f)
                    ))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElse(null);
        }else if (isUniverseFormula(file)){
            return file;
        }
        return null;
    }

    private static boolean isUniverseFormula(Path f){
        File file = f.toFile();
        return !file.isDirectory()
                 && "universe-formula".equals(file.getName());
    }

    static Optional<File> findUniverseFormula(File dir){
        if(dir.isDirectory()){
            Optional<File> formula = findFormulaInDir(dir);
            if(formula.isPresent()){
                return formula;
            }
            return findFormulaInSubdirs(dir);
        }
        if(isUniverseFormula(dir)){
            return Optional.of(dir);
        }
        return Optional.empty();
    }

    private static Optional<File> findFormulaInSubdirs(File dir){
        return Arrays.asList(dir.listFiles()).stream()
                .filter(f->f.isDirectory())
                .flatMap(d ->{
                    Optional<File> formula = findFormulaInDir(d);
                    if(formula.isPresent()){
                        return Stream.of(formula.get());
                    }
                    return Stream.empty();
                })
                .findFirst();
    }

    private static Optional<File> findFormulaInDir(File dir){
        return Arrays.asList(dir.listFiles()).stream()
                .filter(f->!f.isDirectory())
                .filter(f -> isUniverseFormula(f))
                .findFirst();
    }

    private static boolean isUniverseFormula(File f){
        return "universe-formula".equals(f.getName());
    }

}
