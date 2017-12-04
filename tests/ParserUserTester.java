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
    /**
     * Verifies that a specific expression is parsed into the correct parse tree.
     */
    public void singularX () throws ExpressionParseException {
        final String expressionStr = "x";
        final String parseTreeStr = "x";
        assertEquals(parseTreeStr, _parser.parse(expressionStr, false).convertToString(0).replace('*', '·'));
    }


}
