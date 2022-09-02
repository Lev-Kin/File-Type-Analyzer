package analyzer;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 2) throw new InputMismatchException("Wrong amount of arguments!");

        File patternsBase = new File(args[1]);
        ArrayList<Pair<String, String>> patterns;
        try (InputStream inputStream = new FileInputStream(patternsBase)) {
            String[] parsedContent = new String(inputStream.readAllBytes()).split("\n");

            patterns = new ArrayList<>(parsedContent.length);
            for (int i = parsedContent.length-1; i >= 0; i--) {
                String[] parsedRaw = parsedContent[i].split(";");
                patterns.add(new Pair<>(parsedRaw[1].substring(1, parsedRaw[1].length() - 1),
                        parsedRaw[2].substring(1, parsedRaw[2].length() - 1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File directory = new File(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        HashMap<String, Future<String>> fileCheckers = new HashMap<>();

        for (File f : Objects.requireNonNull(directory.listFiles())) {
            try (InputStream inputStream = new FileInputStream(f)) {
                String content = new String(inputStream.readAllBytes());
                Future search = executor.submit(new DefineClassTypeWithKMPByDict(content, patterns));
                fileCheckers.put(f.getName(), search);
            } catch (IOException | OutOfMemoryError e) {
                System.out.println("Error while opening file: " + args[0] + "\n" + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        var filenames = (String[]) fileCheckers.keySet().toArray(new String[0]);
        Arrays.sort(filenames);
        for (String fileName : filenames) {
            System.out.println(fileName + ": " + fileCheckers.get(fileName).get());
        }
        executor.shutdown();
    }
}

record Pair<T, U>(T first, U last) {

}


