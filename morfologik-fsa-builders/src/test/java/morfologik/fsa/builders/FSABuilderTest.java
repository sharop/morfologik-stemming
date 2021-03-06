package morfologik.fsa.builders;

import static morfologik.fsa.builders.FSATestUtils.*;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import morfologik.fsa.FSA;

import org.junit.BeforeClass;
import org.junit.Test;

public class FSABuilderTest extends TestBase {
  private static byte[][] input;
  private static byte[][] input2;

  @BeforeClass
  public static void prepareByteInput() {
    input = generateRandom(25000, new MinMax(1, 20), new MinMax(0, 255));
    input2 = generateRandom(40, new MinMax(1, 20), new MinMax(0, 3));
  }

  @Test
  public void testEmptyInput() {
    byte[][] input = {};
    checkCorrect(input, FSABuilder.build(input));
  }

  @Test
  public void testHashResizeBug() throws Exception {
    byte[][] input = { { 0, 1 }, { 0, 2 }, { 1, 1 }, { 2, 1 }, };

    FSA fsa = FSABuilder.build(input);
    checkCorrect(input, FSABuilder.build(input));
    checkMinimal(fsa);
  }

  @Test
  public void testSmallInput() throws Exception {
    byte[][] input = { "abc".getBytes("UTF-8"), "bbc".getBytes("UTF-8"), "d".getBytes("UTF-8"), };
    checkCorrect(input, FSABuilder.build(input));
  }

  @Test
  public void testLexicographicOrder() throws IOException {
    byte[][] input = { { 0 }, { 1 }, { (byte) 0xff }, };
    Arrays.sort(input, FSABuilder.LEXICAL_ORDERING);

    // Check if lexical ordering is consistent with absolute byte value.
    assertEquals(0, input[0][0]);
    assertEquals(1, input[1][0]);
    assertEquals((byte) 0xff, input[2][0]);

    final FSA fsa;
    checkCorrect(input, fsa = FSABuilder.build(input));

    int arc = fsa.getFirstArc(fsa.getRootNode());
    assertEquals(0, fsa.getArcLabel(arc));
    arc = fsa.getNextArc(arc);
    assertEquals(1, fsa.getArcLabel(arc));
    arc = fsa.getNextArc(arc);
    assertEquals((byte) 0xff, fsa.getArcLabel(arc));
  }

  @Test
  public void testRandom25000_largerAlphabet() {
    FSA fsa = FSABuilder.build(input);
    checkCorrect(input, fsa);
    checkMinimal(fsa);
  }

  @Test
  public void testRandom25000_smallAlphabet() throws IOException {
    FSA fsa = FSABuilder.build(input2);
    checkCorrect(input2, fsa);
    checkMinimal(fsa);
  }
}
