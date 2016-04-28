// Generated from Shopping.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ShoppingLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, INT=2, UNIT=3, WEIGHT=4, LENGTH=5, MISC=6, WARE=7, WS=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "INT", "UNIT", "WEIGHT", "LENGTH", "MISC", "WARE", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "' '"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "INT", "UNIT", "WEIGHT", "LENGTH", "MISC", "WARE", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ShoppingLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Shopping.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\nu\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\3\6\3"+
		"\27\n\3\r\3\16\3\30\3\4\3\4\3\4\5\4\36\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\5\5+\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6?\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7h\n\7\3\b\6"+
		"\bk\n\b\r\b\16\bl\3\t\6\tp\n\t\r\t\16\tq\3\t\3\t\2\2\n\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\3\2\f\3\2\62;\4\2MMmm\4\2IIii\4\2OOoo\4\2EEee\4\2"+
		"UUuu\4\2RRrr\4\2DDdd\4\2C\\c|\4\2\13\f\17\17\u0085\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\3\23\3\2\2\2\5\26\3\2\2\2\7\35\3\2\2\2\t*\3\2\2\2\13>\3\2\2\2"+
		"\rg\3\2\2\2\17j\3\2\2\2\21o\3\2\2\2\23\24\7\"\2\2\24\4\3\2\2\2\25\27\t"+
		"\2\2\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\6\3"+
		"\2\2\2\32\36\5\t\5\2\33\36\5\13\6\2\34\36\5\r\7\2\35\32\3\2\2\2\35\33"+
		"\3\2\2\2\35\34\3\2\2\2\36\b\3\2\2\2\37 \t\3\2\2 +\7i\2\2!\"\t\3\2\2\""+
		"#\7k\2\2#$\7n\2\2$+\7q\2\2%+\t\4\2\2&\'\t\4\2\2\'(\7t\2\2()\7c\2\2)+\7"+
		"o\2\2*\37\3\2\2\2*!\3\2\2\2*%\3\2\2\2*&\3\2\2\2+\n\3\2\2\2,?\t\5\2\2-"+
		".\t\5\2\2./\7g\2\2/\60\7v\2\2\60\61\7g\2\2\61?\7t\2\2\62\63\t\6\2\2\63"+
		"?\7o\2\2\64\65\t\6\2\2\65\66\7g\2\2\66\67\7p\2\2\678\7v\2\289\7k\2\29"+
		":\7o\2\2:;\7g\2\2;<\7v\2\2<=\7g\2\2=?\7t\2\2>,\3\2\2\2>-\3\2\2\2>\62\3"+
		"\2\2\2>\64\3\2\2\2?\f\3\2\2\2@A\t\7\2\2AB\7v\2\2BC\7{\2\2Ch\7m\2\2DE\t"+
		"\7\2\2EF\7v\2\2FG\7{\2\2GH\7m\2\2Hh\7u\2\2IJ\t\b\2\2JK\7q\2\2KL\7u\2\2"+
		"LM\7g\2\2Mh\7t\2\2NO\t\3\2\2OP\7c\2\2PQ\7t\2\2QR\7v\2\2RS\7q\2\2ST\7p"+
		"\2\2TU\7g\2\2Uh\7t\2\2VW\t\3\2\2WX\7c\2\2XY\7u\2\2YZ\7u\2\2Z[\7g\2\2["+
		"h\7t\2\2\\]\t\b\2\2]^\7c\2\2^_\7m\2\2_`\7m\2\2`a\7g\2\2ah\7t\2\2bc\t\t"+
		"\2\2cd\7w\2\2de\7p\2\2ef\7f\2\2fh\7v\2\2g@\3\2\2\2gD\3\2\2\2gI\3\2\2\2"+
		"gN\3\2\2\2gV\3\2\2\2g\\\3\2\2\2gb\3\2\2\2h\16\3\2\2\2ik\t\n\2\2ji\3\2"+
		"\2\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2\2m\20\3\2\2\2np\t\13\2\2on\3\2\2\2pq"+
		"\3\2\2\2qo\3\2\2\2qr\3\2\2\2rs\3\2\2\2st\b\t\2\2t\22\3\2\2\2\n\2\30\35"+
		"*>glq\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}