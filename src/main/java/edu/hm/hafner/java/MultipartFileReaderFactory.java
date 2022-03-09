package edu.hm.hafner.java;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.ReaderFactory;

/**
 * FIXME: comment class.
 *
 * @author Ullrich Hafner
 */
public class MultipartFileReaderFactory extends ReaderFactory {
    private final InputStreamSource file;
    private final String fileName;

    public MultipartFileReaderFactory(final InputStreamSource file, final String fileName, final Charset charset) {
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
