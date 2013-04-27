package me.twocoffee.common.article;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;

import me.twocoffee.common.BasicHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.scanners.ScriptScanner;
import org.htmlparser.tags.AppletTag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.FrameSetTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ObjectTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 正文抽取
 * 
 * @author fan
 * 
 */
public class ArticleExtractor {

	public static class Article {
		private String content;
		private String title;

		public String getContent() {
			return content;
		}

		public String getTitle() {
			return title;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

	public static class LNode implements Comparable<LNode> {
		private int level;
		private Node node;

		@Override
		public int compareTo(LNode arg0) {
			if (extractPlainTextLength(0, arg0.node) > extractPlainTextLength(
					0, this.node)) {
				return 1;
			}
			if (extractPlainTextLength(0, arg0.node) < extractPlainTextLength(
					0, this.node)) {
				return -1;
			}
			return 0;
		}

		public int getLevel() {
			return level;
		}

		public Node getNode() {
			return node;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public void setNode(Node node) {
			this.node = node;
		}
	}

	/**
	 * 比率：纯文本(除去链接文本后的文本)/全部文本
	 */
	private final static double RATIO_OF_PLAINTEXT_TO_ALLTEXT = 0.1;
	public static boolean EXTRACT_COMMIT = true;// 抽取评论
	public final static String REGEX_DIGITAL = "[\t]|[\r]|[\n]|[ ]|[\\d]";// 匹配空格、tab、换行、回车、数字
	public final static String REGEX_Html_ESC = "(&[^(&|;)]+;)+";// 匹配html转义符的正则(&copy;：版权符号除外)

	public final static String REGEX_HTML_HIDDEN_STYLE = "display:none|visibility:hidden";// 网页隐藏属性

	public final static String REGEX_LETTER = "[\u4e00-\u9fa5a-zA-Z]+";// 匹配文字（汉字或英文）

	public final static String REGEX_SPACE = "[\t]|[\r]|[\n]|[ ]";// 匹配空格、tab、换行、回车

	public final static String REPLACEMENT = "_";

	public final static double THRESHOLD_SEEN_RATIO = 0.6;// 已发现比重

	public final static double THRESHOLD_SIMILARITY_RATIO = 0.4;// 相似度阈值

	/**
	 * 抽取纯文本，去处对象：超链、脚本、表单、label、frame、ul
	 * 
	 * @param plainText
	 * @param node
	 */
	public static void extractPlainText(StringBuffer plainText, Node node) {
		if (node instanceof ScriptTag || node instanceof StyleTag) {
			return;
		}
		// 第一次进来可能为TextNode
		if (node instanceof TextNode) {
			plainText.append(node.toPlainTextString());
		}
		NodeList children = node.getChildren();
		if (children == null) {
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			Node child = children.elementAt(i);
			if (child instanceof TextNode || child instanceof ImageTag) {// 文本
				Matcher m = ContextUtils.getMatcher(
						ContextUtils.REGEX_Html_ESC, child.toPlainTextString());
				m = ContextUtils.getMatcher(ContextUtils.REGEX_LETTER,
						m.replaceAll(ContextUtils.REPLACEMENT));
				while (m.find()) {
					plainText.append(m.group());
				}
				continue;
			}
			// 脚本，链接，form,ul中文字排除
			if (child instanceof ScriptTag || child instanceof StyleTag
					|| child instanceof LinkTag || child instanceof FrameTag
					|| child instanceof FrameSetTag
					|| child instanceof LabelTag
					|| child instanceof InputTag || child instanceof OptionTag
					|| child.getText().indexOf("/a") != -1) {
				continue;
			}
			extractPlainText(plainText, child);
		}
	}

	/**
	 * 抽取纯文本，去处对象：超链、脚本、表单、label、frame、ul
	 * 
	 * @param plainText
	 * @param node
	 */
	public static int extractPlainTextLength(int length, Node node) {
		if (node instanceof ScriptTag || node instanceof StyleTag) {
			return length;
		}
		// 第一次进来可能为TextNode
		if (node instanceof TextNode) {
			length += node.toPlainTextString().length();
		}
		NodeList children = node.getChildren();
		if (children == null) {
			return length;
		}
		for (int i = 0; i < children.size(); i++) {
			Node child = children.elementAt(i);
			if (child instanceof TextNode || child instanceof ImageTag) {// 文本
				Matcher m = ContextUtils.getMatcher(
						ContextUtils.REGEX_Html_ESC, child.toPlainTextString());
				m = ContextUtils.getMatcher(ContextUtils.REGEX_LETTER,
						m.replaceAll(ContextUtils.REPLACEMENT));
				while (m.find()) {
					length += m.group().length();
				}
				continue;
			}
			// 脚本，链接，form,ul中文字排除
			if (child instanceof ScriptTag || child instanceof StyleTag
					|| child instanceof LinkTag || child instanceof FrameTag
					|| child instanceof FrameSetTag
					|| child instanceof LabelTag
					|| child instanceof InputTag || child instanceof OptionTag
					|| child.getText().indexOf("/a") != -1) {
				continue;
			}
			length += extractPlainTextLength(length, child);
		}
		return length;
	}

	/**
	 * 抽取含有链接纯文本，去处对象：脚本、frame
	 * 
	 * @param textWithLink
	 * @param node
	 */
	public static void extractTextWithLink(StringBuffer textWithLink, Node node) {
		if (node instanceof ScriptTag || node instanceof StyleTag) {
			return;
		}
		// 第一次进来可能为TextNode
		if (node instanceof TextNode) {
			textWithLink.append(node.toPlainTextString());
		}
		NodeList children = node.getChildren();
		if (children == null) {
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			Node child = children.elementAt(i);
			if (child instanceof TextNode) {// 文本
				Matcher m = ContextUtils.getMatcher(
						ContextUtils.REGEX_Html_ESC, child.toPlainTextString());
				m = ContextUtils.getMatcher(ContextUtils.REGEX_LETTER,
						m.replaceAll(ContextUtils.REPLACEMENT));
				while (m.find()) {
					textWithLink.append(m.group());
				}
				continue;
			}
			// 脚本，链接，form中文字排除
			if (child instanceof ScriptTag || child instanceof StyleTag
					|| child instanceof FrameTag
					|| child instanceof FrameSetTag) {
				continue;
			}
			extractTextWithLink(textWithLink, child);
		}

	}

	// 通过路径得到字符串内容
	public static String getContext(String url, String charset) {
		String resource = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpReponse = BasicHttpClient.getHttpClient().execute(
					httpGet);
			int statusCode = httpReponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = httpReponse.getEntity();

				resource = EntityUtils.toString(httpEntity, charset);
			} else {
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return resource;
	}

	// 通过路径得到字符串内容
	public static String getFileContext(String path, String charset) {
		FileInputStream input;
		String ret = null;
		try {
			input = new FileInputStream(new File(path));
			byte[] b = new byte[input.available()];
			input.read(b);
			ret = new String(b, charset == null ? "UTF-8" : charset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args) {
		String html = getContext(
				"http://www.chinanews.com/gn/2012/06-06/3941335.shtml",
				"gbk");
		Article article = new ArticleExtractor().getArticle(html);
		System.out.println(article.getContent());

	}

	private Map<Integer, String> exclusion = new HashMap<Integer, String>();// 排除者;//
	// 选中不要要输出的节点
	// 多胞胎存储器，key：特征值，value：节点组合
	private Map<String, NodeList> multiplesMap = null;
	private Parser parser;

	private Map<Integer, String> selected = new HashMap<Integer, String>();// 选中要输出的节点

	// 统计行队列
	private List<LineStats> statsList = null;

	// 统计行存储器
	private Map<Integer, LineStats> statsMap = null;

	private Map<String, Integer> unSelected = new HashMap<String, Integer>();

	// 观察者
	private TagStatsVisitor visitor = null;

	/**
	 * 回溯搜索多胞胎
	 * 
	 * @param bks
	 * @param classTagsMap
	 * @param node
	 */
	private void backwardSearchMultiples(BackwardKeepsake bks,
			Map<String, NodeList> classTagsMap, Node node) {
		Node parent = node.getParent();// 父母
		if (parent == null || parent instanceof BodyTag) {
			return;
		}

		Tag parentTag = (Tag) (parent);// node-->tag
		if (!(parentTag instanceof ParagraphTag)
				&& !(parentTag instanceof Span)) {
			String strAtt = TagStatsVisitor.getAttributeString(parentTag
					.getAttributesEx());// 节点特征值字串
			if (exclusion.containsKey(strAtt.hashCode())) {// 该节点未被排除
				/**
				 * 已被排除的，无须再次向上回溯，因为 该节点已在回溯过程中碰到过， 它可能已被选为目标多胞胎节点或者没有(其父类可能被选)，
				 * 所以此类回溯都排除
				 */
				return;
			}
			if (ContextUtils.getMatcher(REGEX_HTML_HIDDEN_STYLE,
					strAtt).find()) {
				/**
				 * 是否含有隐藏样式 含有隐藏样式的节点，前面找到的目标多胞胎都要清空，停止回溯 统计该节点所包含的文字数(隐藏文字数)
				 */
				bks.setGoalMultiple(null);
				StringBuffer hiddenText = new StringBuffer();
				extractPlainText(hiddenText, parentTag);
				bks.gainHiddenTextLength(hiddenText.length());
				hiddenText = null;
				return;
			}
			NodeList nodelist = classTagsMap.get(strAtt);
			if (nodelist != null) {
				/**
				 * 对比每一层的比率=文本长度/节点数，找到比率对大的节点选为目标多胞胎节点
				 */
				double ratioThis = ratioOfTextLengthToNodeCount(parent);
				if (ratioThis > bks.getHighestRatio()) {
					bks.setHighestRatio(ratioThis);
					bks.setGoalMultiple(nodelist);
					/**
					 * 第二次遇到大比率
					 */
					// if(originRatio>0)return;
				}
				/**
				 * 避免下次重复回溯
				 */
				exclusion.put(strAtt.hashCode(), null);
			}
		}

		backwardSearchMultiples(bks, classTagsMap, parent);// 继续回溯
	}

	/**
	 * 抽取纯文本+图片
	 * 
	 * @param plainText
	 * @param node
	 */
	private void extractPlainTextAndImg(StringBuffer plainText, Node node) {
		if (node instanceof ScriptTag || node instanceof StyleTag) {
			return;
		}
		// 第一次进来可能为TextNode
		if (node instanceof TextNode) {
			plainText.append(node.toPlainTextString());
		}
		if (node instanceof ImageTag) {
			plainText.append(node.toHtml());
		}
		NodeList children = node.getChildren();
		if (children == null) {
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			Node child = children.elementAt(i);
			if (child instanceof TextNode) {// 文本
				Matcher m = ContextUtils.getMatcher(
						ContextUtils.REGEX_Html_ESC, child.toPlainTextString());
				m = ContextUtils.getMatcher(ContextUtils.REGEX_LETTER,
						m.replaceAll(ContextUtils.REPLACEMENT));
				while (m.find()) {
					plainText.append(m.group());
				}
				continue;
			}
			if (child instanceof ImageTag) {
				plainText.append(node.toHtml());
			}
			// 脚本，链接，form,ul中文字排除
			if (child instanceof ScriptTag || child instanceof StyleTag
					|| child instanceof LinkTag || child instanceof FrameTag
					|| child instanceof FrameSetTag
					|| child instanceof LabelTag
					|| child instanceof InputTag || child instanceof OptionTag
					|| child.getText().indexOf("/a") != -1) {
				continue;
			}
			extractPlainTextAndImg(plainText, child);
		}
	}

	/**
	 * 输出被选节点
	 * 
	 * @param context
	 * @param seleted
	 */
	private void extractSelectedForOutput(StringBuffer context, Node select) {

		if (select instanceof ScriptTag || select instanceof StyleTag) {
			return;
		}

		NodeList children = select.getChildren();
		if (children == null) {
			return;
		}
		// 不需要输出
		if (unSelect(select)) {
			return;
		}
		if (removeUnoutputNode(select.getParent(), select)) {
			context.append(select.toHtml());
		}
	}

	/**
	 * 寻找最小的公共节点
	 * 
	 * @param node
	 * @param goalMultiple
	 */
	private void findLeastCommonNode(Node node, NodeList goalMultiple) {
		int contained = 0;// reset
		Node parent = node.getParent();
		if (parent == null || parent instanceof BodyTag) {
			return;
		}
		for (int j = 0; j < goalMultiple.size(); j++) {
			Node n = goalMultiple.elementAt(j);
			if (isContained(n, parent)) {
				contained++;
			}
		}
		if ((float) contained / (float) goalMultiple.size() > 0.7) {// 如果包含了70%的节点，则选为候选节点。不再判断其他节点
			goalMultiple.removeAll();
			goalMultiple.add(parent);
			return;
		} else {
			findLeastCommonNode(parent, goalMultiple);
		}
	}

	/*
	 * public NodeList reduceGoalMultiple(NodeList goalMultiple) { if
	 * (goalMultiple == null) { return null; }
	 *//**
	 * 如果大部分多胞胎结果相邻（同一节点的子类），选其父类节点为目标多胞胎
	 */
	/*
	 * int contained = 0; int all = goalMultiple.size(); if (all == 1) { return
	 * goalMultiple; } boolean merged = false; List<LNode> lNodes =
	 * loadInLNodes(goalMultiple); for (LNode lNode : lNodes) { //
	 * 判断每个候选节点的父节点是否含有了大部分(70%)的候选节点 Node parent = lNode.getNode().getParent();
	 * contained = 0;// reset if (parent != null) { for (int j = 0; j <
	 * goalMultiple.size(); j++) { Node node = goalMultiple.elementAt(j); if
	 * (isContained(node, parent)) { contained++; } } if ((float) contained /
	 * (float) all > 0.7) {// 如果包含了70%的节点，则选为候选节点。不再判断其他节点
	 * goalMultiple.removeAll(); goalMultiple.add(parent); merged = true; break;
	 * } } } //如果在所有候选节点的所有父类中没有找到公共的，则继续往上寻找 if(!merged){
	 * 
	 * }
	 * 
	 * return goalMultiple; }
	 */

	/**
	 * 返回该节点包含的节点的数目
	 * 
	 * @param node
	 * @return
	 */
	private double getCount(Node node) {
		double count = 0;
		NodeList children = node.getChildren();// 兄弟节点
		if (children == null) {
			return count;
		}
		for (int i = 0; i < children.size(); i++) {
			Node child = children.elementAt(i);
			if (child instanceof FrameTag || child instanceof FrameSetTag) {
				count++;
			} else {
				count++;
				count += getCount(child);
			}
		}
		return count;
	}

	private int getLevel(Node node, int level) {
		if (node == null || node instanceof BodyTag) {
			return level;
		}
		return getLevel(node.getParent(), ++level);
	}

	// 多胞胎的节点内容是否相似
	private boolean hasSimilarContent(NodeList multiples) {
		if (multiples == null) {
			throw new IllegalArgumentException(
					"NodeList 'multiples' must not be null");
		}
		int size = multiples.size();
		if (size <= 1) {
			return false;// 大小为0或1时无需比对
		}
		Node first = multiples.elementAt(0);

		StringBuffer strA = new StringBuffer();
		extractTextWithLink(strA, first);
		/**
		 * 长度大于100的不再对比相似度 相似度的对比主要是筛选论坛中的发帖人信息、固定链接（如“回复此发言”）等
		 */
		if (strA.length() > 100) {
			return false;
		}
		StringBuffer strB = null;
		Node to = null;
		/**
		 * 由于strA取得是第一个节点， 所以保险起见，只比较奇数位的节点(strA位于奇数位)
		 */
		if (size < 3) {
			// 全部比较
			for (int i = 0; i < size; i++) {
				strB = new StringBuffer();
				to = multiples.elementAt(i);
				extractTextWithLink(strB, to);
				if (Similarity.compute(strA.toString(), strB.toString()) < THRESHOLD_SIMILARITY_RATIO) {
					return false;
				}
			}
		} else if (size <= 6) {
			// 奇数位比较
			for (int i = 2; i < size; i = i + 2) {
				strB = new StringBuffer();
				to = multiples.elementAt(i);
				extractTextWithLink(strB, to);
				if (Similarity.compute(strA.toString(), strB.toString()) < THRESHOLD_SIMILARITY_RATIO) {
					return false;
				}
			}
		} else {
			// 中间
			strB = new StringBuffer();
			to = multiples.elementAt(size / 2);
			extractTextWithLink(strB, to);
			if (Similarity.compute(strA.toString(), strB.toString()) < THRESHOLD_SIMILARITY_RATIO) {
				return false;
			}

			// 最尾端
			strB = new StringBuffer();
			to = multiples.elementAt((size - 2));
			extractTextWithLink(strB, to);
			if (Similarity.compute(strA.toString(), strB.toString()) < THRESHOLD_SIMILARITY_RATIO) {
				return false;
			}
		}
		return true;
	}

	private boolean isContained(Node node, Node goal) {
		if (node.getParent() instanceof BodyTag) {
			return false;
		}
		if (node.getParent().equals(goal)) {
			return true;
		} else {
			return isContained(node.getParent(), goal);
		}
	}

	/**
	 * 迭代所有，输出所有选中的节点，包括标签
	 * 
	 * @param context
	 * @param tag
	 */
	private void iteratorNodeForOutput(StringBuffer context, Node tag) {
		if (tag == null) {
			return;
		}
		NodeList children = tag.getChildren();
		if (children == null) {
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			Node sibling = children.elementAt(i);
			if (sibling instanceof Tag) {
				if (sibling instanceof FrameTag
						|| sibling instanceof FrameSetTag) {
					continue;
				}
				if (selected.containsKey(sibling.hashCode())) {
					extractSelectedForOutput(context, sibling);
				} else {
					iteratorNodeForOutput(context, sibling);
				}
			}
		}
	}

	private List<LNode> loadInLNodes(NodeList nl) {
		List<LNode> list = new ArrayList<LNode>();
		for (int i = 0; i < nl.size(); i++) {
			int lvl = getLevel(nl.elementAt(i), 0);
			LNode lNode = new LNode();
			lNode.setLevel(lvl);
			lNode.setNode(nl.elementAt(i));
			list.add(lNode);
		}
		Collections.sort(list);
		return list;
	}

	private void lookup() {
		double wholeLength = visitor.getPlainContextLength();// 全文的纯文本长度
		double seenLength = 0;// 已抽取的长度
		for (LineStats mostlyLine : statsList) {
			/**
			 * 为了支持36kr，把这个判断条件去掉，之后有问题再优化 if
			 * (mostlyLine.equals(visitor.getTitleLine())) { continue;// 排除标题 }
			 **/
			if (mostlyLine.getTextLength() < 10) {
				break;// 当行长（纯文本）小于10时，停止
			}
			if (mostlyLine.getPlainText().indexOf("&#169;") != -1
					|| mostlyLine.getPlainText().indexOf("&copy;") != -1) {
				continue;
			}
			// 回溯搜索目标多胞胎
			BackwardKeepsake bks = new BackwardKeepsake();
			backwardSearchMultiples(bks, multiplesMap, mostlyLine.mostlyNode());
			NodeList goalMultiples = reduceGoalMultiple(bks.getGoalMultiple());
			if (goalMultiples == null) {
				// System.err.println("本次回溯未选中节点：");
			} else {
				for (int i = 0; i < goalMultiples.size(); i++) {
					StringBuffer allText = new StringBuffer();
					extractTextWithLink(allText, goalMultiples.elementAt(i));
					StringBuffer plainText = new StringBuffer();
					extractPlainText(plainText,
							goalMultiples.elementAt(i));
					double pLength = plainText.length();

					double aLength = allText.length();
					seenLength += allText.length();
					if (goalMultiples.elementAt(i) instanceof ScriptTag
							|| goalMultiples.elementAt(i) instanceof StyleTag
							|| goalMultiples.elementAt(i) instanceof FrameTag
							|| goalMultiples.elementAt(i) instanceof FrameSetTag
							|| goalMultiples.elementAt(i) instanceof FormTag
							|| goalMultiples.elementAt(i) instanceof TextareaTag
							|| goalMultiples.elementAt(i) instanceof ObjectTag
							|| goalMultiples.elementAt(i) instanceof SelectTag
							|| goalMultiples.elementAt(i) instanceof OptionTag
							|| goalMultiples.elementAt(i) instanceof AppletTag) {
						continue;
					}
					if (pLength / aLength > RATIO_OF_PLAINTEXT_TO_ALLTEXT) {// 纯文本大小>超链文本
						selected.put(goalMultiples.elementAt(i).hashCode(),
								null);
					}

				}
				if (bks.getHighestRatio() > 0 && bks.getHighestRatio() < 1) {
					break;
				}
			}
			/**
			 * 去除已发现的隐藏文字,总文字数要去除隐藏文字才能使统计结果准确
			 */
			wholeLength -= bks.getHiddenTextLength();
			if ((seenLength / wholeLength) > THRESHOLD_SEEN_RATIO) {// 已抽取了X%的内容
				break;
			}
		}
	}

	private void mapSortToList() {
		statsList = new ArrayList<LineStats>(statsMap.size());
		Iterator<Integer> keyIt = statsMap.keySet().iterator();
		while (keyIt.hasNext()) {
			statsList.add(statsMap.get(keyIt.next()));
		}
		Collections.sort(statsList);// 排序
	}

	/**
	 * 输出title
	 * 
	 * @return
	 */
	private String outputTitle() {
		String title = visitor.getTitle() != null ? visitor.getTitle()
				.getText() : "";
		int underlinePos = title.indexOf('_') == -1 ? title.lastIndexOf("-")
				: title.lastIndexOf('_');
		title = underlinePos != -1 ? title.substring(0, underlinePos) : title;
		return title;
	}

	private void parseHtml(String context) {
		try {
			ScriptScanner.STRICT = false;// 应对引号内的'<'
			Lexer.STRICT_REMARKS = false;// 应对<!--- --->
			// parser = new Parser();
			parser = new DuoduoParser();
			visitor = new TagStatsVisitor();
			Page page = new Page(context);
			Lexer lexer = new Lexer();
			lexer.setPage(page);
			parser.setLexer(lexer);
			parser.visitAllNodesWith(visitor);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回该节点 比率=文本长度/所包含节点数目
	 * 
	 * @param node
	 * @return
	 */
	private double ratioOfTextLengthToNodeCount(Node node) {
		NodeList siblings = node.getChildren();// 兄弟节点
		double siblingCount = getCount(node);
		double textLength = 0.0;// 文本节点点数
		for (int i = 0; i < siblings.size(); i++) {
			StringBuffer sb = new StringBuffer();
			extractPlainText(sb, siblings.elementAt(i));
			textLength += sb.length();
		}
		return textLength / siblingCount;
	}

	private boolean removeUnoutputNode(Node node, Node select) {
		NodeList nl = node.getChildren();
		if (nl == null) {
			return true;
		}
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < nl.size(); i++) {
			Node n = nl.elementAt(i);
			if (n instanceof ScriptTag || n instanceof StyleTag
					|| n instanceof FrameTag
					|| n instanceof FrameSetTag
					|| n instanceof FormTag || n instanceof TextareaTag
					|| n instanceof ObjectTag || n instanceof SelectTag
					|| n instanceof OptionTag
					|| n instanceof AppletTag
					|| n instanceof InputTag) {
				if (select != null && n.equals(select)) {
					return false;
				}
				toRemove.add(i);
			} else {
				StringBuffer sb = new StringBuffer();
				extractTextWithLink(sb, n);
				int linkLength = sb.length();
				sb = new StringBuffer();
				extractPlainTextAndImg(sb, n);
				int plainText = sb.length();
				if (linkLength != 0
						&& (float) plainText / (float) linkLength < 0.5) {
					if (select != null && n.equals(select)) {
						return false;
					}
					toRemove.add(i);
				}
			}
		}
		Collections.reverse(toRemove);// 通过索引删出得从最大的索引开始
		for (Integer i : toRemove) {
			nl.remove(i);
		}

		for (int i = 0; i < nl.size(); i++) {
			removeAttribute(nl.elementAt(i));
			removeUnoutputNode(nl.elementAt(i), null);
		}

		return true;
	}

	private void removeAttribute(Node node) {
		if (node instanceof Tag) {
			Vector<PageAttribute> vc = ((Tag) node).getAttributesEx();
			for (int j = 0; j < vc.size(); j++) {
				PageAttribute attri = vc.get(j);
				String name = attri.getName();
				String value = attri.getValue();
				if (name != null && !name.equals("") && value != null
						&& !value.equals("")) {
					if (ContextUtils.attriMap.get(name.toLowerCase()) == null) {
						((Tag) node).removeAttribute(name);
					}
				}
			}

		}
	}

	private void reviewSelectedNode(Tag tag) {
		Iterator<String> it = multiplesMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			NodeList nl = multiplesMap.get(key);

		}
	}

	/**
	 * 装载html给htmlparser
	 * 
	 * @param html
	 */
	private void setup(String html) {
		parseHtml(html);
		multiplesMap = visitor.getClassTagsMap();
		statsMap = visitor.getStatsMap();
		mapSortToList();
	}

	/**
	 * 不能输出的节点
	 * 
	 * @param strAtt
	 * @return
	 */
	private boolean unSelect(Node select) {
		/**
		 * unSelected容器中存储不需要输出的节点， 0：不输出 1：输出
		 */
		String strAtt = TagStatsVisitor.getAttributeString(((Tag) select)
				.getAttributesEx());// 节点特征值字串
		Integer result = unSelected.get(strAtt);
		if (result != null && result == 0) {
			return true;
		}
		// 隐藏样式
		if (ContextUtils.getMatcher(ContextUtils.REGEX_HTML_HIDDEN_STYLE,
				strAtt).find()) {
			unSelected.put(strAtt, 0);// 含有隐藏样式，不输出
			return true;
		}
		boolean flag = false;
		if (result == null) {
			NodeList multiples = multiplesMap.get(strAtt);
			if (multiples != null && hasSimilarContent(multiples)) {
				unSelected.put(strAtt, 0);// 相似，不输出
				flag = true;
			} else {
				unSelected.put(strAtt, 1);// 不输出
			}
		}
		return flag;
	}

	public Article getArticle(String html) {
		setup(html);
		lookup();
		StringBuffer context = new StringBuffer();
		iteratorNodeForOutput(context,
				visitor.getBody() == null ? visitor.getHtml()
						: visitor.getBody());
		Article article = new Article();
		article.setTitle(outputTitle());
		// article.setContent(context.toString().replaceAll(
		// "(?is)<script.*?>.*?</script>", ""));
		article.setContent(context.toString());
		return article;
	}

	public NodeList reduceGoalMultiple(NodeList goalMultiple) {
		if (goalMultiple == null) {
			return null;
		}
		if (goalMultiple.size() == 1) {
			return goalMultiple;
		}
		/**
		 * 如果大部分多胞胎结果相邻（同一节点的子类），选其父类(或父类的父类)节点为目标多胞胎
		 */
		List<LNode> lNodes = loadInLNodes(goalMultiple);
		// 判断lNodes中的第一个节点父节点(或祖先节点)是否含有了大部分(70%)的候选节点
		// 从lNodes中取出的第一个节点是排好序的（纯文本长度最长的）
		findLeastCommonNode(lNodes.get(0).getNode(), goalMultiple);
		return goalMultiple;
	}

}
