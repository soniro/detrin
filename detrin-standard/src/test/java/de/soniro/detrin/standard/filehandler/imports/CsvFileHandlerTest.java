package de.soniro.detrin.standard.filehandler.imports;

import de.soniro.detrin.exception.InvalidInstanceException;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Instance;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CsvFileHandlerTest {

    private CsvFileHandler csvFileHandler = new CsvFileHandler();

    @Test
    @Ignore
    public void testCsvFileHandler() throws InvalidInstanceException {
        File file = new File(getClass().getClassLoader().getResource("inputFile.csv").getFile());
        Dataset dataset = csvFileHandler.handleFile(file);
        assertEquals("attribute1", dataset.getAttributes().get(0).getName());
        assertEquals("attribute2", dataset.getAttributes().get(1).getName());
        assertEquals("attribute3", dataset.getAttributes().get(2).getName());

        Attribute attribute = dataset.getAttributes().get(0);
        Instance instance = dataset.getInstances().get(0);
        assertTrue(instance.containsKey(attribute));
        assertEquals("value1", instance.getValueForAttribute(attribute));
    }
}
