package morfologik.fsa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Standard FSA file header, as described in <code>fsa</code> package
 * documentation.
 */
public final class FSAHeader {
  /**
   * FSA magic (4 bytes).
   */
  final static int FSA_MAGIC = 
      ('\\' << 24) | 
      ('f'  << 16) | 
      ('s'  << 8)  | 
      ('a');

  /**
   * Maximum length of the header block.
   */
  static final int MAX_HEADER_LENGTH = 4 + 8;

  /** FSA version number. */
  final byte version;

  FSAHeader(byte version) {
    this.version = version;
  }

  /**
   * Read FSA header and version from a stream, consuming read bytes.
   * 
   * @param in The input stream to read data from.
   * @return Returns a valid {@link FSAHeader} with version information.
   * @throws IOException If the stream ends prematurely or if it contains invalid data.
   */
  public static FSAHeader read(InputStream in) throws IOException {
    if (in.read() != ((FSA_MAGIC >>> 24)       ) ||
        in.read() != ((FSA_MAGIC >>> 16) & 0xff) ||
        in.read() != ((FSA_MAGIC >>>  8) & 0xff) ||
        in.read() != ((FSA_MAGIC       ) & 0xff)) {
      throw new IOException("Invalid file header, probably not an FSA.");
    }

    int version = in.read();
    if (version == -1) {
      throw new IOException("Truncated file, no version number.");
    }

    return new FSAHeader((byte) version);
  }

  /**
   * Writes FSA magic bytes and version information.
   * 
   * @param os The stream to write to.
   * @param version Automaton version.
   * @throws IOException Rethrown if writing fails.
   */
  public static void write(OutputStream os, byte version) throws IOException {
    os.write(FSA_MAGIC >> 24);
    os.write(FSA_MAGIC >> 16);
    os.write(FSA_MAGIC >>  8);
    os.write(FSA_MAGIC);
    os.write(version);
  }
}
