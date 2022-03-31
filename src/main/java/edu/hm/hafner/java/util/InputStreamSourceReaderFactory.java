package edu.hm.hafner.java.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import org.springframework.core.io.InputStreamSource;

import edu.hm.hafner.analysis.ReaderFactory;

/**
 * A {@link ReaderFactory} for {@link InputStreamSource} instances.
 *
 * @author Ullrich Hafner
 */
public class InputStreamSourceReaderFactory extends ReaderFactory {
    private final InputStreamSource file;
    private final String fileName;

    /**
     * Creates a new instance of {@link InputStreamSourceReaderFactory}.
     *
     * @param file
     *         the input file to read
     * @param fileName
     *         the filename to use
     * @param charset
     *         the charset to use
     */
    public InputStreamSourceReaderFactory(final InputStreamSource file, final String fileName, final Charset charset) {
        super(charset);

        this.file = file;
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public Reader create() {
        try {
            return new InputStreamReader(file.getInputStream(), getCharset());
        }
        catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
