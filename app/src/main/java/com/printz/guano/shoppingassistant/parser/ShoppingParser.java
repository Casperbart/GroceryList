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
		T__0=1, INT=2, UNIT=3, WEIGHT=4, LENGTH=5, MISC=6, WARE=7, WS=8;
	public static final int
		RULE_item = 0, RULE_ware = 1, RULE_amount = 2, RULE_unit = 3;
	public static final String[] ruleNames = {
		"item", "ware", "amount", "unit"
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
		public AmountContext amount() {
			return getRuleContext(AmountContext.class,0);
		}
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
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
			setState(27);
			switch (_input.LA(1)) {
			case WARE:
				enterOuterAlt(_localctx, 1);
				{
				setState(8);
				ware();
				setState(9);
				match(T__0);
				setState(10);
				amount();
				setState(12);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(11);
					match(T__0);
					}
				}

				setState(15);
				_la = _input.LA(1);
				if (_la==UNIT) {
					{
					setState(14);
					unit();
					}
				}

				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(17);
				amount();
				setState(19);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(18);
					match(T__0);
					}
					break;
				}
				setState(22);
				_la = _input.LA(1);
				if (_la==UNIT) {
					{
					setState(21);
					unit();
					}
				}

				setState(24);
				match(T__0);
				setState(25);
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
			setState(30); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(29);
				match(WARE);
				}
				}
				setState(32); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WARE );
			setState(38);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(34);
					match(T__0);
					setState(35);
					ware();
					}
					} 
				}
				setState(40);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public static class AmountContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(ShoppingParser.INT, 0); }
		public AmountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_amount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).enterAmount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).exitAmount(this);
		}
	}

	public final AmountContext amount() throws RecognitionException {
		AmountContext _localctx = new AmountContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_amount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(INT);
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

	public static class UnitContext extends ParserRuleContext {
		public TerminalNode UNIT() { return getToken(ShoppingParser.UNIT, 0); }
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).enterUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ShoppingListener ) ((ShoppingListener)listener).exitUnit(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(UNIT);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\n\60\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\5\2\17\n\2\3\2\5\2\22\n\2\3\2\3\2"+
		"\5\2\26\n\2\3\2\5\2\31\n\2\3\2\3\2\3\2\5\2\36\n\2\3\3\6\3!\n\3\r\3\16"+
		"\3\"\3\3\3\3\7\3\'\n\3\f\3\16\3*\13\3\3\4\3\4\3\5\3\5\3\5\2\2\6\2\4\6"+
		"\b\2\2\62\2\35\3\2\2\2\4 \3\2\2\2\6+\3\2\2\2\b-\3\2\2\2\n\13\5\4\3\2\13"+
		"\f\7\3\2\2\f\16\5\6\4\2\r\17\7\3\2\2\16\r\3\2\2\2\16\17\3\2\2\2\17\21"+
		"\3\2\2\2\20\22\5\b\5\2\21\20\3\2\2\2\21\22\3\2\2\2\22\36\3\2\2\2\23\25"+
		"\5\6\4\2\24\26\7\3\2\2\25\24\3\2\2\2\25\26\3\2\2\2\26\30\3\2\2\2\27\31"+
		"\5\b\5\2\30\27\3\2\2\2\30\31\3\2\2\2\31\32\3\2\2\2\32\33\7\3\2\2\33\34"+
		"\5\4\3\2\34\36\3\2\2\2\35\n\3\2\2\2\35\23\3\2\2\2\36\3\3\2\2\2\37!\7\t"+
		"\2\2 \37\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#(\3\2\2\2$%\7\3\2\2"+
		"%\'\5\4\3\2&$\3\2\2\2\'*\3\2\2\2(&\3\2\2\2()\3\2\2\2)\5\3\2\2\2*(\3\2"+
		"\2\2+,\7\4\2\2,\7\3\2\2\2-.\7\5\2\2.\t\3\2\2\2\t\16\21\25\30\35\"(";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}