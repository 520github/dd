package me.twocoffee.common.article;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TextNode;

class LTextNode {
	private int lLength;
	private Node lNode;

	public LTextNode(Node lnode) {
		this.lNode = lnode;
		this.lLength = 0;
	}

	public void addLLength(int length) {
		lLength += length;
	}

	public int getLLength() {
		return lLength;
	}

	public Node getLNode() {
		return lNode;
	}

	public void setLNode(TextNode node) {
		lNode = node;
	}

}

/**
 * 单行统计对象
 */
public class LineStats implements Comparable<LineStats> {
	private StringBuffer htmlText;// 内容
	private double length;// 行长度
	private int lineNum;// 行号
	private String lineSeparator;
	// private Node mostlyNode;//主要文本节点：该节点在本行比重最大
	private Map<Integer, LTextNode> lNodeMap;// 该行所包含的文本节点
	private StringBuffer plainText;// 纯文本
	private int startPos;// 起始位置
	private double textLength = 0;// 文本长度

	public LineStats(int startPos, int num) {
		this.startPos = startPos;
		this.lNodeMap = new HashMap<Integer, LTextNode>();
		this.lineNum = num;
		htmlText = new StringBuffer();
		plainText = new StringBuffer();
	}

	private LTextNode getNode(Node node) {
		LTextNode lnode = null;
		if (lNodeMap.get(node.hashCode()) == null) {
			lnode = new LTextNode(node);
			lNodeMap.put(node.hashCode(), lnode);
		}
		return lNodeMap.get(node.hashCode());
	}

	public int compareTo(LineStats o) {
		// TODO Auto-generated method stub
		if (this.textLength < o.textLength) {
			return 1;
		} else if (this.textLength > o.textLength) {
			return -1;
		}
		return 0;
	}

	public void gainText(Node node, String nodeText) {
		String ptext = nodeText.replaceAll("\t", "").replaceAll(" ", "");
		getNode(node).addLLength(ptext.length());
		textLength += ptext.length();
		plainText.append(ptext);
	}

	public String getHitHtml() {
		return htmlText.toString();
	}

	public double getLength() {
		return length;
	}

	public int getLineNum() {
		return lineNum;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	public Map<Integer, LTextNode> getLNodeMap() {
		return lNodeMap;
	}

	public String getPlainText() {
		return plainText.toString();
	}

	public int getStartPos() {
		return startPos;
	}

	public double getTextLength() {
		return this.plainText.length();
	}

	public Node mostlyNode() {
		Iterator it = lNodeMap.keySet().iterator();
		LTextNode max = null;
		while (it.hasNext()) {
			LTextNode lnode = lNodeMap.get(it.next());
			if (max == null || max.getLLength() < lnode.getLLength()) {
				max = lnode;
			}
		}
		return max.getLNode();
	}

	public void setLength(Page page, double length) {
		this.length = length;
		htmlText.append(ContextUtils.subString(page, startPos, length));
	}

	public void setLineNum(int num) {
		this.lineNum = num;
	}

	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public double textDensity() {// 文本密度
		if (length < 1 || textLength < 1) {
			return 0;
		} else {
			return (textLength - lineSeparator.length()) / length;// （textLength-lineSeparator.length()）：减去'\r\n'的长度
		}
	}

	public double textWeight() {// 文本权重=长度*密度
		return (plainText.length() * textDensity());
	}
}