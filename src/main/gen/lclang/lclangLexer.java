// Generated from D:/IdeaProjects/LittleCat/src/main/java/lclang\lclang.g4 by ANTLR 4.9.1
package lclang;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class lclangLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, WS=23, METHOD=24, ID=25, 
		STRING=26, LONG=27, INTEGER=28;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "WS", "METHOD", "ID", "STRING", 
			"LONG", "INTEGER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\'", "'('", "','", "')'", "'()'", "'return'", "'['", "']'", 
			"'[]'", "'*'", "'='", "'.'", "';'", "'{'", "'}'", "'component'", "'class'", 
			"'@'", "':'", "'use'", "'from'", "'global'", null, "'method'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "WS", 
			"METHOD", "ID", "STRING", "LONG", "INTEGER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public lclangLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "lclang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\36\u00c0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3"+
		"\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\6\30\u0089\n\30"+
		"\r\30\16\30\u008a\3\30\3\30\3\30\3\30\3\30\6\30\u0092\n\30\r\30\16\30"+
		"\u0093\3\30\3\30\3\30\6\30\u0099\n\30\r\30\16\30\u009a\5\30\u009d\n\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\6\32\u00a9\n\32\r\32"+
		"\16\32\u00aa\3\33\3\33\6\33\u00af\n\33\r\33\16\33\u00b0\3\33\3\33\3\34"+
		"\6\34\u00b6\n\34\r\34\16\34\u00b7\3\34\3\34\3\35\6\35\u00bd\n\35\r\35"+
		"\16\35\u00be\5\u008a\u0093\u00b0\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36\3\2\6\4\2\f\f\17\17\5\2\13\f\17\17\""+
		"\"\5\2//C\\c|\3\2\62;\2\u00c8\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5=\3\2\2\2\7?\3\2\2\2\tA\3\2\2\2\13"+
		"C\3\2\2\2\rF\3\2\2\2\17M\3\2\2\2\21O\3\2\2\2\23Q\3\2\2\2\25T\3\2\2\2\27"+
		"V\3\2\2\2\31X\3\2\2\2\33Z\3\2\2\2\35\\\3\2\2\2\37^\3\2\2\2!`\3\2\2\2#"+
		"j\3\2\2\2%p\3\2\2\2\'r\3\2\2\2)t\3\2\2\2+x\3\2\2\2-}\3\2\2\2/\u009c\3"+
		"\2\2\2\61\u00a0\3\2\2\2\63\u00a8\3\2\2\2\65\u00ac\3\2\2\2\67\u00b5\3\2"+
		"\2\29\u00bc\3\2\2\2;<\7^\2\2<\4\3\2\2\2=>\7*\2\2>\6\3\2\2\2?@\7.\2\2@"+
		"\b\3\2\2\2AB\7+\2\2B\n\3\2\2\2CD\7*\2\2DE\7+\2\2E\f\3\2\2\2FG\7t\2\2G"+
		"H\7g\2\2HI\7v\2\2IJ\7w\2\2JK\7t\2\2KL\7p\2\2L\16\3\2\2\2MN\7]\2\2N\20"+
		"\3\2\2\2OP\7_\2\2P\22\3\2\2\2QR\7]\2\2RS\7_\2\2S\24\3\2\2\2TU\7,\2\2U"+
		"\26\3\2\2\2VW\7?\2\2W\30\3\2\2\2XY\7\60\2\2Y\32\3\2\2\2Z[\7=\2\2[\34\3"+
		"\2\2\2\\]\7}\2\2]\36\3\2\2\2^_\7\177\2\2_ \3\2\2\2`a\7e\2\2ab\7q\2\2b"+
		"c\7o\2\2cd\7r\2\2de\7q\2\2ef\7p\2\2fg\7g\2\2gh\7p\2\2hi\7v\2\2i\"\3\2"+
		"\2\2jk\7e\2\2kl\7n\2\2lm\7c\2\2mn\7u\2\2no\7u\2\2o$\3\2\2\2pq\7B\2\2q"+
		"&\3\2\2\2rs\7<\2\2s(\3\2\2\2tu\7w\2\2uv\7u\2\2vw\7g\2\2w*\3\2\2\2xy\7"+
		"h\2\2yz\7t\2\2z{\7q\2\2{|\7o\2\2|,\3\2\2\2}~\7i\2\2~\177\7n\2\2\177\u0080"+
		"\7q\2\2\u0080\u0081\7d\2\2\u0081\u0082\7c\2\2\u0082\u0083\7n\2\2\u0083"+
		".\3\2\2\2\u0084\u0085\7\61\2\2\u0085\u0086\7\61\2\2\u0086\u0088\3\2\2"+
		"\2\u0087\u0089\13\2\2\2\u0088\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u008b\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u009d\t\2"+
		"\2\2\u008d\u008e\7\61\2\2\u008e\u008f\7,\2\2\u008f\u0091\3\2\2\2\u0090"+
		"\u0092\13\2\2\2\u0091\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\3"+
		"\2\2\2\u0093\u0091\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\7,\2\2\u0096"+
		"\u009d\7\61\2\2\u0097\u0099\t\3\2\2\u0098\u0097\3\2\2\2\u0099\u009a\3"+
		"\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009d\3\2\2\2\u009c"+
		"\u0084\3\2\2\2\u009c\u008d\3\2\2\2\u009c\u0098\3\2\2\2\u009d\u009e\3\2"+
		"\2\2\u009e\u009f\b\30\2\2\u009f\60\3\2\2\2\u00a0\u00a1\7o\2\2\u00a1\u00a2"+
		"\7g\2\2\u00a2\u00a3\7v\2\2\u00a3\u00a4\7j\2\2\u00a4\u00a5\7q\2\2\u00a5"+
		"\u00a6\7f\2\2\u00a6\62\3\2\2\2\u00a7\u00a9\t\4\2\2\u00a8\u00a7\3\2\2\2"+
		"\u00a9\u00aa\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\64"+
		"\3\2\2\2\u00ac\u00ae\7$\2\2\u00ad\u00af\13\2\2\2\u00ae\u00ad\3\2\2\2\u00af"+
		"\u00b0\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b3\7$\2\2\u00b3\66\3\2\2\2\u00b4\u00b6\t\5\2\2\u00b5\u00b4"+
		"\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8"+
		"\u00b9\3\2\2\2\u00b9\u00ba\7N\2\2\u00ba8\3\2\2\2\u00bb\u00bd\t\5\2\2\u00bc"+
		"\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf\3\2"+
		"\2\2\u00bf:\3\2\2\2\13\2\u008a\u0093\u009a\u009c\u00aa\u00b0\u00b7\u00be"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}