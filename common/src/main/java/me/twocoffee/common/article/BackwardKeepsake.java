package me.twocoffee.common.article;

import org.htmlparser.util.NodeList;

/**
 * 回溯信物
 * 
 * @author fan
 * 
 */
public class BackwardKeepsake {

	private NodeList goalMultiple;
	private double hiddenTextLength = 0;

	private double highestRatio = 0.0;

	public void gainHiddenTextLength(int length) {
		hiddenTextLength += length;
	}

	public NodeList getGoalMultiple() {
		return goalMultiple;
	}

	public double getHiddenTextLength() {
		return hiddenTextLength;
	}

	public double getHighestRatio() {
		return highestRatio;
	}

	public void setGoalMultiple(NodeList goalMultiple) {
		this.goalMultiple = goalMultiple;
	}

	public void setHighestRatio(double highestRatio) {
		this.highestRatio = highestRatio;
	}
}
