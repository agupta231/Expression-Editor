import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class ParserUserTester {
    private ExpressionParser _parser;

    @Before
    /**
     * Instantiates the actors and movies graphs
     */
    public void setUp () throws IOException {
        _parser = new SimpleExpressionParser();
    }

    @Test
    /**
     * Just verifies that the SimpleExpressionParser could be instantiated without crashing.
     */
    public void finishedLoading () {
        assertTrue(true);
        // Yay! We didn't crash
    }

    @Test
    public void singularX () throws ExpressionParseException {
        final String expressionStr = "x";
        final String parseTreeStr = "x\n";
        assertEquals(parseTreeStr, _parser.parse(expressionStr, false).convertToString(0).replace('*', '·'));
    }

    @Test
    public void multipleDigitLiteralSimpleAddition () throws ExpressionParseException {
        final String expressionStr = "69 + x";
        final String parseTreeStr = "+\n\t69\n\tx\n";
        assertEquals(parseTreeStr, _parser.parse(expressionStr, false).convertToString(0).replace('*', '·'));
    }

    @Test
    public void multipleDigitLiteralSimpleMultiplcation () throws ExpressionParseException {
        final String expressionStr = "420 * x";
        final String parseTreeStr = "·\n\t420\n\tx\n";
        assertEquals(parseTreeStr, _parser.parse(expressionStr, false).convertToString(0).replace('*', '·'));
    }
}
