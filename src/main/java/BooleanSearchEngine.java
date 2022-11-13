import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanSearchEngine implements SearchEngine {

    Map<String, Integer> freqs = new HashMap<>();

    Map<String, List<PageEntry>> listOfIndexes = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        PageEntryBuilder pageEntryBuilder = new PageEntryBuilder();
        List<PdfDocument> listOfDocs = new ArrayList<>();
        File[] listOfFiles = pdfsDir.listFiles();
        assert listOfFiles != null;
        for (File file1 : listOfFiles) {
            if (file1.isFile()) {
                var doc = new PdfDocument(new PdfReader(file1));
                doc.getDocumentInfo().setTitle(file1.getName());
                listOfDocs.add(doc);
            }
        }
        for (int i = 0; i < listOfDocs.size(); i++) {
            PdfDocument doc = listOfDocs.get(i);
            pageEntryBuilder.setPdfName(doc.getDocumentInfo().getTitle());
            for (int j = 1; j < doc.getNumberOfPages(); j++) {
                pageEntryBuilder.setPage(j);
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(j));
                var words = text.split("\\P{IsAlphabetic}+");
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    pageEntryBuilder.setCount(freqs.get(word));
                    PageEntry pageEntry = pageEntryBuilder.build();
                    if (!listOfIndexes.containsKey(word)) {
                        List<PageEntry> collection = new ArrayList<>();
                        collection.add(pageEntry);
                        listOfIndexes.put(word, collection);
                    } else {
                        List<PageEntry> collection = listOfIndexes.get(word);
                        collection.add(pageEntry);
                        listOfIndexes.put(word, collection);
                    }
                }
                freqs.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> resultList = listOfIndexes.get(word);
        return resultList.stream()
                .sorted(Comparator.comparing(PageEntry::getCount).reversed())
                .collect(Collectors.toList());
    }
}
