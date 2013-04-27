package me.twocoffee.common.article;

import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.ParserFeedback;

public class DuoduoParser extends Parser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4612562846125207148L;
	private static PrototypicalNodeFactory factory = null;
	// 注册自定义标签
	static {
		factory = new PrototypicalNodeFactory();
		factory.registerTag(new ArticleTag());
		factory.registerTag(new IframeTag());
	}

	public DuoduoParser() {
		super();
		setNodeFactory(factory);
	}

	public DuoduoParser(Lexer lexer, ParserFeedback fb) {
		super(lexer, fb);
		setNodeFactory(factory);
	}
}
