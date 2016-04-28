// Generated from Shopping.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ShoppingParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, COUNT=2, UNIT=3, WEIGHT=4, LENGTH=5, MISC=6, WARE=7, WS=8;
	public static final int
		RULE_item = 0, RULE_ware = 1;
	public static final String[] ruleNames = {
		"item", "ware"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "' '"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "COUNT", "UNIT", "WEIGHT", "LENGTH", "MISC", "WARE", "WS"
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

	@Override
	public String getGrammarFileName() { return "Shopping.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ShoppingParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ItemContext extends ParserRuleContext {
		public WareContext ware() {
			return getRuleContext(WareContext.class,0);
		}
		public TerminalNode COUNT() { return getToken(ShoppingParser.COUNT, 0); }
		public TerminalNode UNIT() { return getToken(ShoppingParser.UNIT, 0); }
		public ItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).enterItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).exitItem(this);
		}
	}

	public final ItemContext item() throws RecognitionException {
		ItemContext _localctx = new ItemContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_item);
		int _la;
		try {
			setState(21);
			switch (_input.LA(1)) {
			case WARE:
				enterOuterAlt(_localctx, 1);
				{
				setState(4);
				ware();
				setState(5);
				match(T__0);
				setState(6);
				match(COUNT);
				setState(8);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(7);
					match(T__0);
					}
				}

				setState(10);
				match(UNIT);
				}
				break;
			case COUNT:
				enterOuterAlt(_localctx, 2);
				{
				setState(12);
				match(COUNT);
				setState(14);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(13);
					match(T__0);
					}
					break;
				}
				setState(17);
				_la = _input.LA(1);
				if (_la==UNIT) {
					{
					setState(16);
					match(UNIT);
					}
				}

				setState(19);
				match(T__0);
				setState(20);
				ware();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WareContext extends ParserRuleContext {
		public List<TerminalNode> WARE() { return getTokens(ShoppingParser.WARE); }
		public TerminalNode WARE(int i) {
			return getToken(ShoppingParser.WARE, i);
		}
		public List<WareContext> ware() {
			return getRuleContexts(WareContext.class);
		}
		public WareContext ware(int i) {
			return getRuleContext(WareContext.class,i);
		}
		public WareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ware; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).enterWare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).exitWare(this);
		}
	}

	public final WareContext ware() throws RecognitionException {
		WareContext _localctx = new WareContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ware);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(24); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(23);
				match(WARE);
				}
				}
				setState(26); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WARE );
			setState(32);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(28);
					match(T__0);
					setState(29);
					ware();
					}
					} 
				}
				setState(34);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\n&\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\3\2\3\2\5\2\13\n\2\3\2\3\2\3\2\3\2\5\2\21\n\2\3\2\5\2\24\n"+
		"\2\3\2\3\2\5\2\30\n\2\3\3\6\3\33\n\3\r\3\16\3\34\3\3\3\3\7\3!\n\3\f\3"+
		"\16\3$\13\3\3\3\2\2\4\2\4\2\2)\2\27\3\2\2\2\4\32\3\2\2\2\6\7\5\4\3\2\7"+
		"\b\7\3\2\2\b\n\7\4\2\2\t\13\7\3\2\2\n\t\3\2\2\2\n\13\3\2\2\2\13\f\3\2"+
		"\2\2\f\r\7\5\2\2\r\30\3\2\2\2\16\20\7\4\2\2\17\21\7\3\2\2\20\17\3\2\2"+
		"\2\20\21\3\2\2\2\21\23\3\2\2\2\22\24\7\5\2\2\23\22\3\2\2\2\23\24\3\2\2"+
		"\2\24\25\3\2\2\2\25\26\7\3\2\2\26\30\5\4\3\2\27\6\3\2\2\2\27\16\3\2\2"+
		"\2\30\3\3\2\2\2\31\33\7\t\2\2\32\31\3\2\2\2\33\34\3\2\2\2\34\32\3\2\2"+
		"\2\34\35\3\2\2\2\35\"\3\2\2\2\36\37\7\3\2\2\37!\5\4\3\2 \36\3\2\2\2!$"+
		"\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\5\3\2\2\2$\"\3\2\2\2\b\n\20\23\27\34\"";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}