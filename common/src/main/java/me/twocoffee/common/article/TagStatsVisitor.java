package me.twocoffee.common.article;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;

import org.htmlparser.Node;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.lexer.Page;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

public class TagStatsVisitor extends NodeVisitor {
	private final static char CR = 13;// '\r'
	private final static char LF = 10;// '\n'

	// 剔除字串中的数字
	private static String weedDigital(String value) {
		StringBuffer buff = new StringBuffer();
		if (value == null) {
			return buff.toString();
		}
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (!Character.isDigit(ch)) {
				buff.append(ch);
			}
		}
		return buff.toString();

	}

	/**
	 * 根据属性向量生成字串（id的值剔除字符串）
	 * 
	 * @param attributes
	 * @return
	 */
	public static String getAttributeString(Vector<PageAttribute> attributes) {
		PageAttribute element = null;
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < attributes.size(); i++) {
			element = attributes.get(i);
			if (element.getName() == null
					|| (element.getName() != null && element.getName()
							.endsWith("id"))) {// id属性（id或xxid）
				/*
				 * buff.append(element.toString().replaceAll( element.getValue()
				 * == null ? "" : element.getValue(),
				 * weedDigital(element.getValue())));
				 */
				// 这里如果id={#xxx}会导致正则解析异常，先排除
			} else {
				buff.append(element.toString());
				buff.append(" ");
			}
		}
		/*
		 * if(attributes.size()==1){//为了区分<div>和<div id=xx> buff.append("]"); }
		 */
		return buff.toString();
	}

	/**
	 * 根据属性向量生成字串（id的值剔除字符串）
	 * 
	 * @param attributes
	 * @return
	 */
	public static String getAttributeString1(Vector<PageAttribute> attributes) {
		String strAtt = attributes.toString();
		if (strAtt.indexOf("id") != -1) {// 有id属性
			/*
			 * 将id属性值中的数字去掉，如id=replay001，处理后：id=replay
			 */
			PageAttribute element = null;
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < attributes.size(); i++) {
				element = attributes.get(i);
				String name = element.getName();
				if (name != null && name.endsWith("id")) {// id属性（id或xxid）
					String stillPart = weedDigital(element.getValue());
					buff.append(element.toString().replaceAll(
							element.getValue() == null ? "" : element
									.getValue(), stillPart));
				} else {
					buff.append(element.toString());
				}
			}
			strAtt = buff.toString();
		}
		return strAtt;
	}

	/**
	 * 截取从位置start开始，长度为length的字串
	 * 
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subString(Page page, int start, double length) {
		String value = null;
		try {
			value = page.getSource().getString(start, (int) length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private Node body;
	private Map<String, NodeList> classTagsMap;// 分类后的复合标签存储器
	private StringBuffer context = new StringBuffer();
	private int cursor = 0;// 游标，记录当前处理行首位置
	private Node html;
	private int lineNumber = 1;// 当前处理行号

	private Map<Integer, LineStats> statsMap;// 行统计存储器
	private Node title;

	private LineStats titleLine;

	public TagStatsVisitor() {
		super(true);
		this.statsMap = new HashMap<Integer, LineStats>();
		this.classTagsMap = new HashMap<String, NodeList>();
	}

	// 游标前移offset位，行号加一
	private void advance(int offset) {
		cursor += offset;
		lineNumber++;
	}

	private void gainText(Node node, String text) {

		if (text.indexOf("&copy;") != -1 || text.indexOf("&#169;") != -1) {
			// 含有版权符号copyRight,这行文字不要
			return;
		}
		// 把&XXX;转化为replacement，大小为1，因为他们只代表一个字符
		Matcher m = ContextUtils.getMatcher(ContextUtils.REGEX_Html_ESC, text);
		// 抽取文字（只要英文和中文）
		m = ContextUtils.getMatcher(ContextUtils.REGEX_LETTER,
				m.replaceAll(ContextUtils.REPLACEMENT));
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			sb.append(m.group());
		}
		context.append(sb.toString());
		getLineStats().gainText(node, sb.toString());
	}

	// 得到行统计对象
	private LineStats getLineStats() {
		LineStats ls = statsMap.get(lineNumber);
		if (ls == null) {
			ls = new LineStats(cursor, lineNumber);
			statsMap.put(lineNumber, ls);
		}
		return ls;
	}

	// 处理含有分行符的文本节点
	private void handleTextNodeWithinSeparator(Node node, boolean script) {
		String value = ContextUtils.nodeString(node);// 截取该节点的字串
		int nodestart = node.getStartPosition();// 记录该节点的开始位置
		char chr;
		boolean isHrefChild = isHrefChild(node);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			chr = value.charAt(i);
			if (chr == CR) {// 回车'\r'
				if (i < value.length() - 1 && value.charAt(i + 1) == LF) {// 该情况必然是'\r'和'\n'一块出现(Windows换行表示)
					int length = nodestart + i + 2 - cursor;// length{偏移量(该行长度)}=nodestart{该节点的开始位置}+i{换行符('\n\r')之前字符长度}+2{换行符的长度2}-cursor{当前行首}
					typingEnter(node, node.getPage(), length, "\r\n");
					// 移动至新行
					advance(length);
					i++;// 跳过下一个字符'\n'
				}
			} else if (chr == LF) {// 换行'\n',,该情况必然是'\n'单独出现(Unix换行表示)
				int length = nodestart + i + 1 - cursor;// length{偏移量(该行长度)}=nodestart{该节点的开始位置}+i{换行符('\n')之前字符长度}+1{换行符的长度1}-cursor{当前行首}
				typingEnter(node, node.getPage(), length, "\n");
				// 移动至新行
				advance(length);
			} else {
				if (!script && !isHrefChild) {
					sb.append(chr);
				}
			}
		}
		gainText(node, sb.toString());

	}

	/**
	 * 该节点内容中是否含有分行符
	 * 
	 * @param node
	 * @return
	 */
	private boolean hasLineSeparator(Node node) {
		String value = ContextUtils.nodeString(node);
		return (value != null && value.indexOf(LF) != -1);
	}

	private boolean isHrefChild(Node node) {
		Node parent = node.getParent();
		if (parent != null && !(parent instanceof LinkTag)) {
			return isHrefChild(parent);
		} else if (parent instanceof LinkTag) {
			return true;
		} else {
			return false;
		}

	}

	private void typingEnter(Node node, Page page, int length,
			String lineSeparator) {
		// 本行善后工作
		LineStats ls = getLineStats();
		ls.setLength(page, length);// 设置长度
		ls.gainText(node, lineSeparator);// 行尾加上换行
		ls.setLineSeparator(lineSeparator);// 设置换行符
		updateStatsMap(ls);
	}

	private void updateStatsMap(LineStats ls) {
		statsMap.remove(lineNumber);
		statsMap.put(lineNumber, ls);
	}

	private void visitNode(Node node) {
		if (node instanceof TextNode) {
			Node parent = node.getParent();
			if (parent != null
					&& (parent instanceof ScriptTag
							|| parent instanceof StyleTag || parent instanceof TextareaTag)) {
				if (hasLineSeparator(node)) {// 跨行文本
					handleTextNodeWithinSeparator(node, true);
				}

				return;// <sryle>和<script>里面的文本，已作处理
			}
			if (hasLineSeparator(node)) {// 跨行文本
				handleTextNodeWithinSeparator(node, false);
			} else {// 行内文本
				if (!isHrefChild(node)) {
					gainText(node, node.toPlainTextString());
				}
			}
			if (title == null && parent instanceof TitleTag) {
				title = node;
				titleLine = getLineStats();
			}
		} else if (node instanceof RemarkNode) {// 注释文本处理
			if (hasLineSeparator(node)) {// 跨行注释
				handleTextNodeWithinSeparator(node, true);
			}
		}

	}

	public Node getBody() {
		return body;
	}

	public Map<String, NodeList> getClassTagsMap() {
		return classTagsMap;
	}

	public Node getHtml() {
		return html;
	}

	public double getPlainContextLength() {
		// System.out.println(context.toString());
		return context.length();
	}

	public Map<Integer, LineStats> getStatsMap() {
		return statsMap;
	}

	public Node getTitle() {
		return title;
	}

	public LineStats getTitleLine() {
		return titleLine;
	}

	public void reset() {
		cursor = 0;// 光标回到开始位置
		lineNumber = 1;// 行号归一
		title = null;
		body = null;
		if (statsMap != null) {
			statsMap.clear();// 清空容器
		}
		if (statsMap != null) {
			classTagsMap.clear();
		}
		this.context = new StringBuffer();
	}

	/*
	 * public double getHrefDensity(){ System.out.println(this.hrefTextLength);
	 * System.out.println(this.contextLength); return
	 * this.hrefTextLength/this.contextLength; }
	 */
	public void setStatsMap(Map<Integer, LineStats> statsMap) {
		this.statsMap = statsMap;
	}

	@Override
	public void visitRemarkNode(Remark remark) {
		visitNode(remark);
	}

	@Override
	public void visitStringNode(Text StringNode) {
		visitNode(StringNode);
	}

	@Override
	public void visitTag(Tag tag) {

		// 删除iframe
		if (tag instanceof IframeTag) {
			tag.getParent().getChildren().remove(tag);
			return;
		}
		if (tag == null || !(tag instanceof CompositeTag)) {
			return;// 复合标签
		}
		if (html == null && tag instanceof Html) {
			this.html = tag;
		}
		if (body == null && tag instanceof BodyTag) {
			this.body = tag;
			return;
		}
		// 排除：th,tr,li,ul,href,script,style,p,span（基本不可能成为帖子结点的标签，比较常见的干扰标签）
		if (tag instanceof TableHeader
				|| tag instanceof TableRow
				|| tag instanceof Bullet
				|| tag instanceof BulletList
				|| tag instanceof LinkTag
				|| tag instanceof ScriptTag
				|| tag instanceof StyleTag
		// 不带id的<p>
		// || (tag instanceof ParagraphTag &&
		// tag.getAttributesEx().toString().indexOf(" id")==-1)
		) {
			return;
		}
		Vector<PageAttribute> attributes = tag.getAttributesEx();// 得到属性
		if (tag.getChildren() == null) {
			return;
		}
		String strAtt = getAttributeString(attributes);
		NodeList classTags = this.classTagsMap.get(strAtt);
		if (classTags == null) {
			classTags = new NodeList();
		}
		classTags.add(tag);// 添加到该分类组中
		this.classTagsMap.put(strAtt, classTags);
	}

}
